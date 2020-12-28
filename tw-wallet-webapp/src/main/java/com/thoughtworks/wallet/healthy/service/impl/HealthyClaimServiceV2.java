package com.thoughtworks.wallet.healthy.service.impl;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.common.crypto.Base64;
import com.thoughtworks.common.crypto.CryptoFacade;
import com.thoughtworks.common.crypto.Curve;
import com.thoughtworks.common.crypto.SignatureScheme;
import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.common.util.Jwt;
import com.thoughtworks.wallet.healthy.dto.*;
import com.thoughtworks.wallet.healthy.exception.*;
import com.thoughtworks.wallet.healthy.model.*;
import com.thoughtworks.wallet.healthy.model.V2.*;
import com.thoughtworks.wallet.healthy.model.V2.Issuer;
import com.thoughtworks.wallet.healthy.repository.HealthVerificationDAO;
import com.thoughtworks.wallet.healthy.repository.HealthVerificationDAOV2;
import com.thoughtworks.wallet.healthy.service.IHealthyClaimServiceV2;
import com.thoughtworks.wallet.healthy.utils.ClaimIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
public class HealthyClaimServiceV2 implements IHealthyClaimServiceV2 {
    private final ClaimIdUtil claimIdUtil;
    private final HealthVerificationClaimContract healthVerificationClaimContract;
    private final HealthVerificationDAOV2 healthVerificationDAOV2;
    private final ModelMapper modelMapper = new ModelMapper();
    public final static String didSchema = "DID:TW:";
    private final String version = "0.1";
    final ImmutableList<String> context = ImmutableList.of("https://www.w3.org/2018/credentials/v1", "https://w3c-ccg.github.io/vc-examples/covid-19/v2/v2.jsonld");
    final String TEST_TYPE = "qSARS-CoV-2-Rapid-Test-Credential";
    final String CATALOG_NUMBER = "5515C025, 5515C050, 5515C100";
    final String IFU = "https://cellexcovid.com/wp-content/uploads/2020/04/Cellex-rapid-ifu.pdf";
    final ImmutableList<String> credentialType = ImmutableList.of("VerifiableCredential", TEST_TYPE);
    final String ISSUER_TYPE = "CovidTestingFacility";
    final String ISSUER_NAME = "Stanford Health Care";
    final String ISSUER_URL = "https://stanfordhealthcare.org/";
    final String VC_NAME = "qSARS-CoV-2 IgG/IgM Rapid Test Credential";
    final String VC_DESC = "Results from antibody testing should not be used as the sole basis to diagnose or exclude SARS-CoV-2 infection. False positive results may occur due to cross-reacting antibodies from previous infections, such as other coronaviruses, or from other causes Samples with positive results should be confirmed with alternative testing method(s) and clinical findings before a diagnostic determination is made.";


    // TODO: 目前假设 claim 1 mins 过期。可以设置在配置文件
    Duration expiredDuration = Duration.ofMinutes(1);

    public HealthyClaimServiceV2(DSLContext dslContext, ClaimIdUtil claimIdUtil, HealthyClaimContractService healthyClaimContractService, HealthVerificationClaimContract healthVerificationClaimContract, SuspectedPatientService suspectedPatientService, HealthVerificationDAO healthVerificationDAO, HealthVerificationDAOV2 healthVerificationDAOV2) {
        this.claimIdUtil = claimIdUtil;
        this.healthVerificationClaimContract = healthVerificationClaimContract;
        this.healthVerificationDAOV2 = healthVerificationDAOV2;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JwtResponse createHealthVerification(HealthVerificationRequestV2 healthVerification) {
        HealthVerificationClaimV2 claim = generateHealthyVerificationClaim(healthVerification);
        String token = sign(healthVerification.getDid(), claim);
        insertClaim2DB(healthVerification, claim, token);

        return JwtResponse.of(token);
    }

    private void insertClaim2DB(HealthVerificationRequestV2 healthVerification, HealthVerificationClaimV2 claim, String token) {
        final int insertedNumber;
        try {
            claim.setToken(token);
            insertedNumber = healthVerificationDAOV2.insertHealthVerificationClaim(claim);
        } catch (DataIntegrityViolationException e) {
            log.error("Healthy verification of owner:{} is already existed.", healthVerification.getDid());
            throw new HealthVerificationAlreadyExistException(healthVerification.getDid());
        }

        if (insertedNumber != 1) {
            log.error("Insert into database error: can not insert healthy verification of owner: {}.", healthVerification.getDid());
            throw new InsertIntoDatabaseErrorException(healthVerification.getDid());
        }
    }

    private String sign(String did, HealthVerificationClaimV2 claim) {
        String token = "";
        try {
            Jwt jwt = Jwt.builder()
                    .header(new Jwt.Header("ES256", "JWT"))
                    .payLoad(claim)
                    .cryptoFacade(CryptoFacade.fromPrivateKey(healthVerificationClaimContract.getIssuerPrivateKey(), SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1))
                    .build();
            token = jwt.genJwtString();
        } catch (Exception e) {
            throw new SignJwtException(did);
        }
        return token;
    }


    @Override
    public VerifyJwtResponse VerifyHealthVerification(VerifyJwtRequest verifyJwtRequest) {

        try {
            String[] strings = verifyJwtRequest.getToken().split("\\.");
            Jwt.Header header = JacksonUtil.jsonStrToBean(Base64.decode(strings[0]), Jwt.Header.class);
            HealthVerificationResponse payload = JacksonUtil.jsonStrToBean(Base64.decode(strings[1]), HealthVerificationResponse.class);
            String signature = Base64.decode(strings[2]);
            String headerPayload = String.format("%s.%s", strings[0], strings[1]);

            Jwt jwt = Jwt.builder().header(new Jwt.Header(header.getAlg(), header.getTyp()))
                    .cryptoFacade(CryptoFacade.fromPrivateKey(healthVerificationClaimContract.getIssuerPrivateKey(), SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1))
                    .build();

            boolean verifySignature = jwt.getCryptoFacade().verifySignature(headerPayload, signature);
            boolean outdate = payload.getExp() > Instant.now().getEpochSecond();

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

    /**
     * @param healthVerification
     * @return
     */
    private HealthVerificationClaimV2 generateHealthyVerificationClaim(HealthVerificationRequestV2 healthVerification) {
        final String holderDid = healthVerification.getDid();
        final String claimId = claimIdUtil.generateClaimId(holderDid, version);
        log.info("holder id : {} - claimId : {}", holderDid, claimId);

        //TODO: issuer did 应该是注册在链上的，这里是V1版本的简化实现
        String issuerDid = didSchema + healthVerificationClaimContract.getIssuerAddress().substring(2);

        final Instant now = Instant.now();
        final long currentTime = now.getEpochSecond();
        final long expiredTime = now.plus(expiredDuration).getEpochSecond();

        // 填充subject
        return HealthVerificationClaimV2.builder()
                .ver(version)
                .iss(issuerDid)
                .iat(currentTime)
                .exp(expiredTime)
                .vc(VC.of(
                        context,
                        credentialType,
                        claimId,
                        Issuer.of(Location.of(
                                ISSUER_TYPE,
                                ISSUER_NAME,
                                ISSUER_URL)
                        ),
                        VC_NAME,
                        VC_DESC,
                        Sub.of(
                                holderDid, ImmutableList.of(TEST_TYPE),
                                CATALOG_NUMBER,
                                IFU,
                                AssayStatus.Negative)
                ))
                .build();
    }
}
