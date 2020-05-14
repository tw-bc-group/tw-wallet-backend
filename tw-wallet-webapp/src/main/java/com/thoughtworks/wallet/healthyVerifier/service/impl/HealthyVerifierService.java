package com.thoughtworks.wallet.healthyVerifier.service.impl;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.wallet.gen.tables.records.TblHealthyVerificationClaimRecord;
import com.thoughtworks.wallet.healthyVerifier.dto.ChangeHealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.dto.HealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.dto.HealthVerificationResponse;
import com.thoughtworks.wallet.healthyVerifier.exception.HealthVerificationAlreadyExistException;
import com.thoughtworks.wallet.healthyVerifier.exception.HealthVerificationNotFoundException;
import com.thoughtworks.wallet.healthyVerifier.exception.InsertIntoDatabaseErrorException;
import com.thoughtworks.wallet.healthyVerifier.model.HealthVerificationClaim;
import com.thoughtworks.wallet.healthyVerifier.model.HealthVerificationClaimContract;
import com.thoughtworks.wallet.healthyVerifier.model.HealthyCredential;
import com.thoughtworks.wallet.healthyVerifier.model.HealthyStatus;
import com.thoughtworks.wallet.healthyVerifier.model.HealthyStatusWrapper;
import com.thoughtworks.wallet.healthyVerifier.model.Result;
import com.thoughtworks.wallet.healthyVerifier.repository.HealthVerificationDAO;
import com.thoughtworks.wallet.healthyVerifier.service.IHealthyVerifierService;
import com.thoughtworks.wallet.healthyVerifier.utils.ClaimIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static com.thoughtworks.wallet.gen.Tables.TBL_HEALTHY_VERIFICATION_CLAIM;
import static com.thoughtworks.wallet.healthyVerifier.model.Result.YES;

@Slf4j
@Service
public class HealthyVerifierService implements IHealthyVerifierService {
    private final       DSLContext                      dslContext;
    private final       ClaimIdUtil                     claimIdUtil;
    private final       HealthyClaimContractService     healthyClaimContractService;
    private final       HealthVerificationClaimContract healthVerificationClaimContract;
    private final       SuspectedPatientService         suspectedPatientService;
    private final       HealthVerificationDAO           healthVerificationDAO;
    private final       ModelMapper                     modelMapper           = new ModelMapper();
    public final static String                          didSchema             = "DID:TW:";
    private final       String                          version               = "0.1";
    private final       float                           maxHealthyTemperature = 37.3F;
    final               ImmutableList<String>           context               = ImmutableList.of("https://blockchain.thoughtworks.cn/credentials/v1/");
    final               ImmutableList<String>           credentialType        = ImmutableList.of("HealthyCredential");

    // TODO 目前假设 claim 5 mins 过期
    Duration expiredDuration = Duration.ofMinutes(5);

    public HealthyVerifierService(DSLContext dslContext, ClaimIdUtil claimIdUtil, HealthyClaimContractService healthyClaimContractService, HealthVerificationClaimContract healthVerificationClaimContract, SuspectedPatientService suspectedPatientService, HealthVerificationDAO healthVerificationDAO) {
        this.dslContext = dslContext;
        this.claimIdUtil = claimIdUtil;
        this.healthyClaimContractService = healthyClaimContractService;
        this.healthVerificationClaimContract = healthVerificationClaimContract;
        this.suspectedPatientService = suspectedPatientService;
        this.healthVerificationDAO = healthVerificationDAO;
    }

    @Override
    public HealthVerificationResponse createHealthVerification(HealthVerificationRequest healthVerification) {
        HealthVerificationClaim claim     = generateHealthyVerificationClaim(healthVerification);
        String                  issuerDid = generateIssuerDid();

        final int insertedNumber;
        try {
            insertedNumber = healthVerificationDAO.insertHealthVerificationClaim(claim);
        } catch (DataIntegrityViolationException e) {
            log.error("Healthy verification of owner:{} is already existed.", healthVerification.getDid());
            throw new HealthVerificationAlreadyExistException(healthVerification.getDid());
        }

        if (insertedNumber != 1) {
            log.error("Insert into database error: can not insert healthy verification of owner: {}.", healthVerification.getDid());
            throw new InsertIntoDatabaseErrorException(healthVerification.getDid());
        }

        healthyClaimContractService.createHealthVerification(healthVerificationClaimContract.getIssuerAddress(), claim.getId(), healthVerification.getDid(), issuerDid);

        return HealthVerificationResponse.of(
                claim.getContext(),
                claim.getId(),
                claim.getVer(),
                claim.getIss(),
                claim.getIat(),
                claim.getExp(),
                claim.getTyp(),
                claim.getSub()
        );
    }

    @Override
    public HealthVerificationResponse getHealthVerification(String ownerId) {
        TblHealthyVerificationClaimRecord tblHealthyVerificationClaimRecord = Optional.ofNullable(dslContext
                .selectFrom(TBL_HEALTHY_VERIFICATION_CLAIM)
                .where(TBL_HEALTHY_VERIFICATION_CLAIM.OWNER.equal(ownerId))
                .orderBy(TBL_HEALTHY_VERIFICATION_CLAIM.IAT.desc())
                .fetchOne()).orElseThrow(() -> new HealthVerificationNotFoundException(ownerId));

        HealthVerificationClaim claim = new HealthVerificationClaim(tblHealthyVerificationClaimRecord);
        return modelMapper.map(claim, HealthVerificationResponse.class);
    }

    @Override
    public HealthVerificationResponse changeHealthVerification(ChangeHealthVerificationRequest changeHealthVerificationRequest) {
        TblHealthyVerificationClaimRecord tblHealthyVerificationClaimRecord = Optional.ofNullable(dslContext
                .selectFrom(TBL_HEALTHY_VERIFICATION_CLAIM)
                .where(TBL_HEALTHY_VERIFICATION_CLAIM.OWNER.equal(changeHealthVerificationRequest.getOwnerId()))
                .fetchOne()).orElseThrow(() -> new HealthVerificationNotFoundException(changeHealthVerificationRequest.getOwnerId()));

        HealthVerificationClaim claim = new HealthVerificationClaim(tblHealthyVerificationClaimRecord);
        claim.getSub().getHealthyStatus().setVal(changeHealthVerificationRequest.getHealthyStatus().getStatus());
        final Instant now         = Instant.now();
        final long    expiredTime = now.plus(expiredDuration).getEpochSecond();
        claim.setExp(expiredTime);
        healthVerificationDAO.updateHealthVerificationClaim(claim);
        return modelMapper.map(claim, HealthVerificationResponse.class);

    }

    private HealthVerificationClaim generateHealthyVerificationClaim(HealthVerificationRequest healthVerification) {
        final String did         = healthVerification.getDid();
        final String phone       = healthVerification.getPhone();
        final float  temperature = healthVerification.getTemperature();
        final Result contact     = Result.of(healthVerification.getContact());
        final Result symptoms    = Result.of(healthVerification.getSymptoms());

        final String claimId = claimIdUtil.generateClaimId(did, version);
        log.info("Claim Id of did:{} is {}.", did, claimId);
        String issuerDid = generateIssuerDid();

        final Instant now         = Instant.now();
        final long    currentTime = now.getEpochSecond();
        final long    expiredTime = now.plus(expiredDuration).getEpochSecond();

        final HealthyStatusWrapper healthyStatus = generateHealthyStatus(phone, temperature, symptoms);

        return HealthVerificationClaim.builder()
                .context(context)
                .id(claimId)
                .ver(version)
                .iss(issuerDid)
                .iat(currentTime)
                .exp(expiredTime)
                .typ(credentialType)
                .sub(HealthyCredential.of(did, phone, temperature, contact, symptoms, healthyStatus))
                .build();
    }

    @NotNull
    private String generateIssuerDid() {
        return didSchema + healthVerificationClaimContract.getIssuerAddress().substring(2);
    }

    public HealthyStatusWrapper generateHealthyStatus(String phone, float temperature, Result symptoms) {
        //TODO: mock healthy status:
        //   1. Temperature > 37.3 -> UNHEALTHY
        //   2. Has symptoms -> UNHEALTHY
        //   3. Phone number in suspected list -> UNHEALTHY
        //   4. Otherwise -> HEALTHY
        final boolean isSuspectedPatient = suspectedPatientService.isSuspectedPatient(phone);

        final boolean isHighTemperature = Float.compare(temperature, maxHealthyTemperature) >= 0;

        final boolean hasSymptoms = symptoms.equals(YES);

        if (isHighTemperature || isSuspectedPatient || hasSymptoms) {
            return HealthyStatusWrapper.of(HealthyStatus.UNHEALTHY.getStatus());
        }
        return HealthyStatusWrapper.of(HealthyStatus.HEALTHY.getStatus());
    }
}
