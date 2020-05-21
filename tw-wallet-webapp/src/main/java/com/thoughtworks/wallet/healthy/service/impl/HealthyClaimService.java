package com.thoughtworks.wallet.healthy.service.impl;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.common.crypto.Base64;
import com.thoughtworks.common.crypto.CryptoFacade;
import com.thoughtworks.common.crypto.Curve;
import com.thoughtworks.common.crypto.SignatureScheme;
import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.wallet.gen.tables.records.TblHealthyVerificationClaimRecord;
import com.thoughtworks.wallet.healthy.dto.*;
import com.thoughtworks.common.util.Jwt;
import com.thoughtworks.wallet.healthy.exception.*;
import com.thoughtworks.wallet.healthy.model.*;
import com.thoughtworks.wallet.healthy.repository.HealthVerificationDAO;
import com.thoughtworks.wallet.healthy.service.IHealthyClaimService;
import com.thoughtworks.wallet.healthy.utils.ClaimIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static com.thoughtworks.wallet.gen.Tables.TBL_HEALTHY_VERIFICATION_CLAIM;
import static com.thoughtworks.wallet.healthy.model.Result.YES;

@Slf4j
@Service
public class HealthyClaimService implements IHealthyClaimService {
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

    public HealthyClaimService(DSLContext dslContext, ClaimIdUtil claimIdUtil, HealthyClaimContractService healthyClaimContractService, HealthVerificationClaimContract healthVerificationClaimContract, SuspectedPatientService suspectedPatientService, HealthVerificationDAO healthVerificationDAO) {
        this.dslContext = dslContext;
        this.claimIdUtil = claimIdUtil;
        this.healthyClaimContractService = healthyClaimContractService;
        this.healthVerificationClaimContract = healthVerificationClaimContract;
        this.suspectedPatientService = suspectedPatientService;
        this.healthVerificationDAO = healthVerificationDAO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JwtResponse createHealthVerification(HealthVerificationRequest healthVerification) {
        HealthVerificationClaim claim     = generateHealthyVerificationClaim(healthVerification);
        String                  issuerDid = generateIssuerDid();

        HealthVerificationResponse healthVerificationResponse = HealthVerificationResponse.builder()
                .context(claim.getContext())
                .id(claim.getId())
                .ver(claim.getVer())
                .iss(claim.getIss())
                .iat(claim.getIat())
                .exp(claim.getExp())
                .typ(claim.getTyp())
                .sub(claim.getSub())
                .build();

        String token = sign(healthVerification, healthVerificationResponse);

        insertClaim2DB(healthVerification, claim, token);

        // 默认不需要上链
        if (healthVerification.isOnChain()) {
            healthyClaimContractService.createHealthVerification(healthVerificationClaimContract.getIssuerAddress(), claim.getId(), healthVerification.getDid(), issuerDid);
        }

        return JwtResponse.of(token);
    }

    private void insertClaim2DB(HealthVerificationRequest healthVerification, HealthVerificationClaim claim, String token) {
        final int insertedNumber;
        try {
            claim.setToken(token);
            insertedNumber = healthVerificationDAO.insertHealthVerificationClaim(claim);
        } catch (DataIntegrityViolationException e) {
            log.error("Healthy verification of owner:{} is already existed.", healthVerification.getDid());
            throw new HealthVerificationAlreadyExistException(healthVerification.getDid());
        }

        if (insertedNumber != 1) {
            log.error("Insert into database error: can not insert healthy verification of owner: {}.", healthVerification.getDid());
            throw new InsertIntoDatabaseErrorException(healthVerification.getDid());
        }
    }

    private String sign(HealthVerificationRequest healthVerification, HealthVerificationResponse healthVerificationResponse) {
        String token = "";
        try {
            Jwt jwt = Jwt.builder()
                    .header(new Jwt.Header("ES256", "JWT"))
                    .payLoad(healthVerificationResponse)
                    .cryptoFacade(CryptoFacade.fromPrivateKey(healthVerificationClaimContract.getIssuerPrivateKey(), SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1))
                    .build();
            token = jwt.genJwtString();
        } catch (Exception e) {
            throw new SignJwtException(healthVerification.getDid());
        }
        return token;
    }

    @Override
    public HealthVerificationResponse getHealthVerification(String ownerId) {
        TblHealthyVerificationClaimRecord tblHealthyVerificationClaimRecord = Optional.ofNullable(dslContext
                .selectFrom(TBL_HEALTHY_VERIFICATION_CLAIM)
                .where(TBL_HEALTHY_VERIFICATION_CLAIM.OWNER.equal(ownerId))
                .orderBy(TBL_HEALTHY_VERIFICATION_CLAIM.IAT.desc())
                .fetchAny()).orElseThrow(() -> new HealthVerificationNotFoundException(ownerId));

        HealthVerificationClaim claim = new HealthVerificationClaim(tblHealthyVerificationClaimRecord);

        // TODO: 需要重新生成签名
        if (suspectedPatientService.isSuspectedPatient(claim.getSub().getPhone())) {
            claim.getSub().setHealthyStatus(HealthyStatusWrapper.of(HealthyStatus.UNHEALTHY.getStatus()));
        }

        return modelMapper.map(claim, HealthVerificationResponse.class);
    }

    @Override
    public HealthVerificationResponse changeHealthVerification(ChangeHealthVerificationRequest changeHealthVerificationRequest) {
        TblHealthyVerificationClaimRecord tblHealthyVerificationClaimRecord = Optional.ofNullable(dslContext
                .selectFrom(TBL_HEALTHY_VERIFICATION_CLAIM)
                .where(TBL_HEALTHY_VERIFICATION_CLAIM.OWNER.equal(changeHealthVerificationRequest.getOwnerId()))
                .fetchAny()).orElseThrow(() -> new HealthVerificationNotFoundException(changeHealthVerificationRequest.getOwnerId()));

        HealthVerificationClaim claim       = new HealthVerificationClaim(tblHealthyVerificationClaimRecord);
        final Instant           now         = Instant.now();
        final long              expiredTime = now.plus(expiredDuration).getEpochSecond();
        claim.setExp(expiredTime);
        claim.getSub().getHealthyStatus().setVal(changeHealthVerificationRequest.getHealthyStatus().getStatus());
        healthVerificationDAO.updateHealthVerificationClaim(claim);
        return modelMapper.map(claim, HealthVerificationResponse.class);

    }

    @Override
    public VerifyJwtResponse VerifyHealthVerification(VerifyJwtRequest verifyJwtRequest) {

        try {
            String[]                   strings       = verifyJwtRequest.getToken().split("\\.");
            Jwt.Header                 header        = JacksonUtil.jsonStrToBean(Base64.decode(strings[0]), Jwt.Header.class);
            HealthVerificationResponse payload       = JacksonUtil.jsonStrToBean(Base64.decode(strings[1]), HealthVerificationResponse.class);
            String                     signature     = Base64.decode(strings[2]);
            String                     headerPayload = String.format("%s.%s", strings[0], strings[1]);

            Jwt jwt = Jwt.builder().header(new Jwt.Header(header.getAlg(), header.getTyp()))
                    .cryptoFacade(CryptoFacade.fromPrivateKey(healthVerificationClaimContract.getIssuerPrivateKey(), SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1))
                    .build();

            boolean verifySignature = jwt.getCryptoFacade().verifySignature(headerPayload, signature);
            boolean outdate         = payload.getExp() > Instant.now().getEpochSecond();

            return VerifyJwtResponse.builder()
                    .verifySignature(verifySignature ? VerifyResultEnum.TRUE : VerifyResultEnum.FALSE)
                    .onchain(VerifyResultEnum.NOT_SUPPORT)
                    .outdate(outdate ? VerifyResultEnum.TRUE : VerifyResultEnum.FALSE)
                    .revoked(VerifyResultEnum.NOT_SUPPORT)
                    .verifyHolder(VerifyResultEnum.NOT_SUPPORT)
                    .verifyIssuer(VerifyResultEnum.NOT_SUPPORT)
                    .build();
        } catch (Exception e) {
            throw new VerifyJwtException(verifyJwtRequest.getToken());
        }

    }

    private HealthVerificationClaim generateHealthyVerificationClaim(HealthVerificationRequest healthVerification) {
        final String did         = healthVerification.getDid();
        final String phone       = healthVerification.getPhone();
        final float  temperature = healthVerification.getTemperature();
        final Result contact     = healthVerification.getContact();
        final Result symptoms    = healthVerification.getSymptoms();

        final String claimId = claimIdUtil.generateClaimId(did, version);
        log.info("Claim Id of did:{} is {}.", did, claimId);
        String issuerDid = generateIssuerDid();

        final Instant now         = Instant.now();
        final long    currentTime = now.getEpochSecond();
        final long    expiredTime = now.plus(expiredDuration).getEpochSecond();

        final HealthyStatusWrapper healthyStatus = generateHealthyStatus(phone, temperature, contact, symptoms);

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

    public HealthyStatusWrapper generateHealthyStatus(String phone, float temperature, Result contact, Result symptoms) {
        //TODO: mock healthy status:
        //   1. Temperature > 37.3 -> UNHEALTHY
        //   2. Has symptoms -> UNHEALTHY
        //   3. Phone number in suspected list -> UNHEALTHY
        //   4. Otherwise -> HEALTHY
        final boolean isSuspectedPatient = suspectedPatientService.isSuspectedPatient(phone);

        final boolean isHighTemperature = Float.compare(temperature, maxHealthyTemperature) >= 0;

        final boolean hasContact  = YES.equals(contact);
        final boolean hasSymptoms = YES.equals(symptoms);

        if (isHighTemperature || isSuspectedPatient || hasContact || hasSymptoms) {
            return HealthyStatusWrapper.of(HealthyStatus.UNHEALTHY.getStatus());
        }
        return HealthyStatusWrapper.of(HealthyStatus.HEALTHY.getStatus());
    }
}
