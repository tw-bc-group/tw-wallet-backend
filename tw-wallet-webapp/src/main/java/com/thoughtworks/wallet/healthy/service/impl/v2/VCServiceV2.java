package com.thoughtworks.wallet.healthy.service.impl.v2;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.common.crypto.Base64;
import com.thoughtworks.common.crypto.CryptoFacade;
import com.thoughtworks.common.crypto.Curve;
import com.thoughtworks.common.crypto.SignatureScheme;
import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.common.util.Jwt;
import com.thoughtworks.wallet.healthy.dto.*;
import com.thoughtworks.wallet.healthy.dto.v2.*;
import com.thoughtworks.wallet.healthy.exception.*;
import com.thoughtworks.wallet.healthy.model.HealthVerificationClaimContract;
import com.thoughtworks.wallet.healthy.model.Verifier;
import com.thoughtworks.wallet.healthy.repository.HealthVerificationDAO;
import com.thoughtworks.wallet.healthy.repository.HealthVerificationDAOV2;
import com.thoughtworks.wallet.healthy.repository.VerifierDAO;
import com.thoughtworks.wallet.healthy.service.impl.HealthyClaimContractService;
import com.thoughtworks.wallet.healthy.service.impl.SuspectedPatientService;
import com.thoughtworks.wallet.healthy.service.v2.IVCService;
import com.thoughtworks.wallet.healthy.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class VCServiceV2 implements IVCService {
    private final ClaimIdUtil claimIdUtil;
    private final HealthVerificationClaimContract healthVerificationClaimContract;
    private final HealthVerificationDAOV2 healthVerificationDAOV2;
    public final static String didSchema = "DID:TW:";
    private final String version = "0.1";
    Duration expiredDuration = Duration.ofMinutes(30);

    private final VerifierDAO verifierDAO;

    public VCServiceV2(DSLContext dslContext, ClaimIdUtil claimIdUtil, HealthyClaimContractService healthyClaimContractService, HealthVerificationClaimContract healthVerificationClaimContract, SuspectedPatientService suspectedPatientService, HealthVerificationDAO healthVerificationDAO, HealthVerificationDAOV2 healthVerificationDAOV2, VerifierDAO verifierDAO) {
        this.claimIdUtil = claimIdUtil;
        this.healthVerificationClaimContract = healthVerificationClaimContract;
        this.healthVerificationDAOV2 = healthVerificationDAOV2;
        this.verifierDAO = verifierDAO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JwtResponse createHealthVerification(CreateVCRequest healthVerification) {
        VerifiableCredentialJwt claim = generateHealthyVerificationClaim(healthVerification);
        String token = sign(healthVerification.getDid(), claim);
        insertClaim2DB(healthVerification, claim, token);
        return JwtResponse.of(token);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JwtResponse createImmunoglobulinDetectionVC(CreateVCRequest createVCRequest) {
        VerifiableCredentialJwt claim = generateImmunoglobulinDetectionVC(createVCRequest);
        String token = sign(createVCRequest.getDid(), claim);
        insertClaim2DB(createVCRequest, claim, token);
        return JwtResponse.of(token);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JwtResponse createPassportVC(CreateVCRequest createVCRequest) {
        VerifiableCredentialJwt claim = generatePassportVC(createVCRequest);
        String token = sign(createVCRequest.getDid(), claim);
        insertClaim2DB(createVCRequest, claim, token);
        return JwtResponse.of(token);
    }

    private VerifiableCredentialJwt generatePassportVC(CreateVCRequest createVCRequest) {
        final String holderDid = createVCRequest.getDid();
        final String claimId = claimIdUtil.generateClaimId(version);
        String issuerDid = didSchema + healthVerificationClaimContract.getIssuerAddress().substring(2);
        final Instant now = Instant.now();
        final long currentTime = now.getEpochSecond();
        final long expiredTime = now.plus(expiredDuration).getEpochSecond();
        // 填充subject
        return VerifiableCredentialJwt.builder()
                .ver(version)
                .iss(issuerDid)
                .iat(currentTime)
                .exp(expiredTime)
                .vc(VC.of(
                        ConstPassportVC.context,
                        ConstPassportVC.credentialType,
                        claimId,
                        Issuer.of(Location.of(
                                ConstPassportVC.ISSUER_TYPE,
                                ConstPassportVC.ISSUER_NAME,
                                ConstPassportVC.ISSUER_URL)
                        ),
                        ConstPassportVC.VC_NAME,
                        ConstPassportVC.VC_DESC,
                        PassportSub.of(
                                holderDid,
                                "your name",
                                "your family name",
                                "中国"
                        )
                ))
                .build();
    }


    private VerifiableCredentialJwt generateImmunoglobulinDetectionVC(CreateVCRequest healthVerification) {
        final String holderDid = healthVerification.getDid();
        final String claimId = claimIdUtil.generateClaimId(version);
        String issuerDid = didSchema + healthVerificationClaimContract.getIssuerAddress().substring(2);
        final Instant now = Instant.now();
        final long currentTime = now.getEpochSecond();
        final long expiredTime = now.plus(expiredDuration).getEpochSecond();
        // 填充subject
        return VerifiableCredentialJwt.builder()
                .ver(version)
                .iss(issuerDid)
                .iat(currentTime)
                .exp(expiredTime)
                .vc(VC.of(
                        ConstImmunoglobulinDetectionVC.context,
                        ConstImmunoglobulinDetectionVC.credentialType,
                        claimId,
                        Issuer.of(Location.of(
                                ConstImmunoglobulinDetectionVC.ISSUER_TYPE,
                                ConstImmunoglobulinDetectionVC.ISSUER_NAME,
                                ConstImmunoglobulinDetectionVC.ISSUER_URL)
                        ),
                        ConstImmunoglobulinDetectionVC.VC_NAME,
                        ConstImmunoglobulinDetectionVC.VC_DESC,
                        ImmunoglobulinDetectionSub.of(
                                holderDid,
                                ConstImmunoglobulinDetectionVC.SUB_TYPE,
                                true,
                                true
                        )
                ))
                .build();
    }

    @Override
    public List<String> getHealthVerification(String ownerId) {
        return healthVerificationDAOV2.getHealthVerificationClaim(ownerId);
    }

    private void insertClaim2DB(CreateVCRequest healthVerification, VerifiableCredentialJwt claim, String token) {
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

    private String sign(String did, Object claim) {
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

    public JwtResponse createTravelBadgeVC(String holderDid) {
        VerifiableCredentialJwt claim = generateTravelBadgeVC(holderDid);
        String token = sign(holderDid, claim);
        return JwtResponse.of(token);
    }

    public JwtResponse createSimpleTravelBadgeVC(String holderDid) {
        final Instant now = Instant.now();
        final long currentTime = now.getEpochSecond();
        final long expiredTime = now.plus(expiredDuration).getEpochSecond();
        String token = sign(holderDid, TravelBadge.builder()
                .exp(expiredTime)
                .build());
        return JwtResponse.of(token);
    }

    private VerifiableCredentialJwt generateTravelBadgeVC(String holderDid) {
        final String claimId = claimIdUtil.generateClaimId(version);
        String issuerDid = didSchema + healthVerificationClaimContract.getIssuerAddress().substring(2);
        final Instant now = Instant.now();
        final long currentTime = now.getEpochSecond();
        final long expiredTime = now.plus(expiredDuration).getEpochSecond();
        // 填充subject
        return VerifiableCredentialJwt.builder()
                .ver(version)
                .iss(issuerDid)
                .iat(currentTime)
                .exp(expiredTime)
                .vc(VC.of(
                        ConstTravelBadgeVC.context,
                        ConstTravelBadgeVC.credentialType,
                        claimId,
                        Issuer.of(Location.of(
                                ConstTravelBadgeVC.ISSUER_TYPE,
                                ConstTravelBadgeVC.ISSUER_NAME,
                                ConstTravelBadgeVC.ISSUER_URL)
                        ),
                        ConstTravelBadgeVC.VC_NAME,
                        ConstTravelBadgeVC.VC_DESC,
                        TravelBadgeSub.of(
                                holderDid,
                                ConstTravelBadgeVC.SUB_TYPE,
                                ConstTravelBadgeVC.IMAGE_URL
                        )
                ))
                .build();
    }

    @Override
    public JwtResponse VerifyHealthVerification(VerifyJwtTokensRequest verifyJwtRequest, Boolean isSimple) {
        try {
            String holderDID = "";
            Verifier verifier = verifierDAO.getVerifierById(verifyJwtRequest.getVerifierId());
            List<String> vcTypes = verifier.getVcTypes();
            log.info("verifier getVcTypes: {}", vcTypes);
            int needVcNums = vcTypes.size();
            for (String token : verifyJwtRequest.getTokens()) {
                String[] strings = token.split("\\.");
                Jwt.Header header = JacksonUtil.jsonStrToBean(Base64.decode(strings[0]), Jwt.Header.class);
                VerifiableCredentialJwt payload = JacksonUtil.jsonStrToBean(Base64.decode(strings[1]), VerifiableCredentialJwt.class);

                log.info("payload.getVc().getTyp(): {}", payload.getVc().getTyp());

                // 查找是否有符合要求的vc，保证所有类型都有
                boolean containsType = false;
                for (String vcType : vcTypes) {
                    containsType = payload.getVc().getTyp().contains(vcType);
                    if (containsType) {
                        // 删除已经匹配到的vc，防止传递两个一样的vc
                        vcTypes.remove(vcType);
                        break;
                    }
                }

                if (!containsType) {
                    // 发送了不符合要求的vc
                    throw new VerifyHealthyVCJwtException("VC 类型不符合");
                }

                holderDID = payload.getVc().getSub().getId();

                String signature = Base64.decode(strings[2]);
                String headerPayload = String.format("%s.%s", strings[0], strings[1]);

                Jwt jwt = Jwt.builder().header(new Jwt.Header(header.getAlg(), header.getTyp()))
                        .cryptoFacade(CryptoFacade.fromPrivateKey(healthVerificationClaimContract.getIssuerPrivateKey(), SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1))
                        .build();

                boolean verifySignature = jwt.getCryptoFacade().verifySignature(headerPayload, signature);
                boolean inDate = payload.getExp() > Instant.now().getEpochSecond();
                if (verifySignature && inDate) {
                    // 找到一个可以用的vc，减去数量，保证vc数量要求符合要求
                    needVcNums--;
                    continue;
                } else {
                    throw new VerifyHealthyVCJwtException("过期或者验证签名失败");
                }
            }
            if (needVcNums == 0) {
                if (isSimple) {
                    return createSimpleTravelBadgeVC(holderDID);
                }
                return createTravelBadgeVC(holderDID);
            } else {
                throw new VerifyHealthyVCJwtException("所需要的VC数量与提供的不一致");
            }
        } catch (VerifyHealthyVCJwtException verifyHealthyVCJwtException) {
            throw verifyHealthyVCJwtException;
        } catch (Exception e) {
            throw new VerifyJwtTokensException(verifyJwtRequest.getTokens());
        }
    }

    @Override
    public VerifyJwtResponse VerifyTravelBadgeVC(VerifyJwtRequest verifyJwtRequest) {
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
            boolean overdue = payload.getExp() < Instant.now().getEpochSecond();

            return VerifyJwtResponse.builder()
                    .verifySignature(verifySignature ? VerifyResultEnum.TRUE : VerifyResultEnum.FALSE)
                    .onchain(VerifyResultEnum.NOT_SUPPORT)
                    .overdue(overdue ? VerifyResultEnum.TRUE : VerifyResultEnum.FALSE)
                    .revoked(VerifyResultEnum.NOT_SUPPORT)
                    .verifyHolder(VerifyResultEnum.NOT_SUPPORT)
                    .build();
        } catch (Exception e) {
            throw new VerifyJwtException(verifyJwtRequest.getToken());
        }
    }

    /**
     * @param healthVerification
     * @return
     */
    private VerifiableCredentialJwt generateHealthyVerificationClaim(CreateVCRequest healthVerification) {
        final String holderDid = healthVerification.getDid();
        final String claimId = claimIdUtil.generateClaimId(version);
        log.info("holder id : {} - claimId : {}", holderDid, claimId);

        //TODO: issuer did 应该是注册在链上的，这里是V1版本的简化实现
        String issuerDid = didSchema + healthVerificationClaimContract.getIssuerAddress().substring(2);

        final Instant now = Instant.now();
        final long currentTime = now.getEpochSecond();
        final long expiredTime = now.plus(expiredDuration).getEpochSecond();

        // 填充subject
        return VerifiableCredentialJwt.builder()
                .ver(version)
                .iss(issuerDid)
                .iat(currentTime)
                .exp(expiredTime)
                .vc(VC.of(
                        ConstCoV2RapidTestCredential.context,
                        ConstCoV2RapidTestCredential.credentialType,
                        claimId,
                        Issuer.of(Location.of(
                                ConstCoV2RapidTestCredential.ISSUER_TYPE,
                                ConstCoV2RapidTestCredential.ISSUER_NAME,
                                ConstCoV2RapidTestCredential.ISSUER_URL)
                        ),
                        ConstCoV2RapidTestCredential.VC_NAME,
                        ConstCoV2RapidTestCredential.VC_DESC,
                        HealthySub.of(
                                holderDid, ImmutableList.of(ConstCoV2RapidTestCredential.TEST_TYPE),
                                ConstCoV2RapidTestCredential.CATALOG_NUMBER,
                                ConstCoV2RapidTestCredential.IFU,
                                AssayStatus.Negative)
                ))
                .build();
    }
}
