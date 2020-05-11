package com.thoughtworks.wallet.healthyVerifier.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.gen.tables.records.TblHealthyVerificationClaimRecord;
import com.thoughtworks.wallet.healthyVerifier.dto.ChangeHealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.dto.HealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.dto.HealthVerificationResponse;
import com.thoughtworks.wallet.healthyVerifier.exception.HealthVerificationAlreadyExistException;
import com.thoughtworks.wallet.healthyVerifier.exception.InsertIntoDatabaseErrorException;
import com.thoughtworks.wallet.healthyVerifier.model.HealthVerificationClaim;
import com.thoughtworks.wallet.healthyVerifier.model.HealthVerificationClaimContract;
import com.thoughtworks.wallet.healthyVerifier.model.HealthyCredential;
import com.thoughtworks.wallet.healthyVerifier.model.HealthyStatus;
import com.thoughtworks.wallet.healthyVerifier.model.HealthyStatusWrapper;
import com.thoughtworks.wallet.healthyVerifier.repository.HealthVerificationDAO;
import com.thoughtworks.wallet.healthyVerifier.utils.ClaimIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static com.thoughtworks.wallet.gen.Tables.TBL_HEALTHY_VERIFICATION_CLAIM;

@Slf4j
@Service
public class HealthyVerifierService implements IHealthyVerifierService {
    private final DSLContext dslContext;
    private final ClaimIdUtil claimIdUtil;
    private final HealthyClaimContractService healthyClaimContractService;
    private final HealthVerificationClaimContract healthVerificationClaimContract;
    private final HealthVerificationDAO healthVerificationDAO;
    private final ModelMapper modelMapper = new ModelMapper();

    public final static String didSchema = "DID:TW:";
    private final String version = "0.1";
    final ImmutableList<String> context = ImmutableList.of("https://blockchain.thoughtworks.cn/credentials/v1/");
    final ImmutableList<String> credentialType = ImmutableList.of("HealthyCredential");

    // TODO 目前假设 claim 5 mins 过期
    Duration expiredDuration = Duration.ofMinutes(5);

    public HealthyVerifierService(DSLContext dslContext, ClaimIdUtil claimIdUtil, HealthyClaimContractService healthyClaimContractService, HealthVerificationClaimContract healthVerificationClaimContract, HealthVerificationDAO healthVerificationDAO) {
        this.dslContext = dslContext;
        this.claimIdUtil = claimIdUtil;
        this.healthyClaimContractService = healthyClaimContractService;
        this.healthVerificationClaimContract = healthVerificationClaimContract;
        this.healthVerificationDAO = healthVerificationDAO;
    }

    @Override
    public HealthVerificationResponse createHealthVerification(HealthVerificationRequest healthVerification) {
        HealthVerificationClaim claim = generateHealthyVerificationClaim(healthVerification.getDid(), healthVerification.getPhone());
        String issuerDid = generateIssuerDid();

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
        TblHealthyVerificationClaimRecord tblHealthyVerificationClaimRecord = dslContext
                .selectFrom(TBL_HEALTHY_VERIFICATION_CLAIM)
                .where(TBL_HEALTHY_VERIFICATION_CLAIM.OWNER.equal(ownerId))
                .orderBy(TBL_HEALTHY_VERIFICATION_CLAIM.IAT.desc())
                .fetchOne();

        HealthVerificationClaim claim = new HealthVerificationClaim(tblHealthyVerificationClaimRecord);
        //TODO: why use HealthVerificationResponse?
        return modelMapper.map(claim, HealthVerificationResponse.class);
    }

    @Override
    public HealthVerificationResponse changeHealthVerification(ChangeHealthVerificationRequest changeHealthVerificationRequest) {
        TblHealthyVerificationClaimRecord tblHealthyVerificationClaimRecord = dslContext
                .selectFrom(TBL_HEALTHY_VERIFICATION_CLAIM)
                .where(TBL_HEALTHY_VERIFICATION_CLAIM.OWNER.equal(changeHealthVerificationRequest.getOwnerId()))
                .fetchOne();

        HealthVerificationClaim claim = new HealthVerificationClaim(tblHealthyVerificationClaimRecord);

        claim.getSub().getHealthyStatus().setVal(changeHealthVerificationRequest.getHealthyStatus().getStatus());
        healthVerificationDAO.updateHealthVerificationClaim(claim);
        return modelMapper.map(claim, HealthVerificationResponse.class);

    }

    private HealthVerificationClaim generateHealthyVerificationClaim(String did, String phone) {
        final String claimId = claimIdUtil.generateClaimId(did, version);
        log.info("Claim Id of did:{} is {}.", did, claimId);
        String issuerDid = generateIssuerDid();

        final Instant now = Instant.now();
        final long currentTime = now.getEpochSecond();
        final long expiredTime = now.plus(expiredDuration).getEpochSecond();

        final HealthyStatusWrapper healthyStatus = generateHealthyStatus(phone);

        return HealthVerificationClaim.of(
                context,
                claimId,
                version,
                issuerDid,
                currentTime,
                expiredTime,
                credentialType,
                HealthyCredential.of(did, phone, healthyStatus));
    }

    @NotNull
    private String generateIssuerDid() {
        return didSchema + healthVerificationClaimContract.getIssuerAddress().substring(2);
    }

    private HealthyStatusWrapper generateHealthyStatus(String phone) {
        // TODO: mock healthy status: last number of phone is odd number, then it's UNHEALTHY, Otherwise its HEALTHY
        Map<Integer, String> healthyStatus = ImmutableMap.of(0, HealthyStatus.HEALTHY.getStatus(), 1, HealthyStatus.UNHEALTHY.getStatus());
        String lastChar = phone.substring(phone.length() - 1);
        final int lastNumber = Integer.parseInt(lastChar);
        int no = lastNumber % 2;
        return HealthyStatusWrapper.of(healthyStatus.get(no));
    }
}
