package com.thoughtworks.wallet.healthy.service.impl.v2;

import com.thoughtworks.wallet.healthy.dto.JwtResponse;
import com.thoughtworks.wallet.healthy.dto.VerifyJwtRequest;
import com.thoughtworks.wallet.healthy.dto.VerifyJwtResponse;
import com.thoughtworks.wallet.healthy.dto.VerifyResultEnum;
import com.thoughtworks.wallet.healthy.dto.v2.CreateVCRequest;
import com.thoughtworks.wallet.healthy.dto.v2.VerifyJwtTokensRequest;
import com.thoughtworks.wallet.healthy.model.HealthVerificationClaimContract;
import com.thoughtworks.wallet.healthy.model.Verifier;
import com.thoughtworks.wallet.healthy.repository.HealthVerificationDAOV2;
import com.thoughtworks.wallet.healthy.repository.VerifierDAO;
import com.thoughtworks.wallet.healthy.utils.ClaimIdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest()
@ActiveProfiles("test")
public class VCServiceV2Test {

    @InjectMocks
    private VCServiceV2 vcServiceV2;

    @Mock
    private VerifierDAO verifierDAO;

    @Mock
    private HealthVerificationDAOV2 healthVerificationDAOV2;

    @Mock
    private ClaimIdUtil claimIdUtil;

    @Mock
    private HealthVerificationClaimContract healthVerificationClaimContract;

    final String MOCK_DID = "did:tw:mockdid";
    final String MOCK_PHONE_NUMBER = "17611111111";
    final String MOCK_NAME = "mockname";
    final String ISSUER_ADDRESS = "00BYsuEwyc9M2mQpjN12sykxwHuzbJM58U3jsFpc67";
    final String MOCK_PRIVATE_KEY = "4762e04d10832808a0aebdaa79c12de54afbe006bfffd228b3abcc494fe986f9";
    final String HEALTH_VC_TYPE_ID = "qSARS-CoV-2-Rapid-Test-Credential";

    @Test
    public void should_create_verifiable_credential() {
        CreateVCRequest createVCRequest = new CreateVCRequest(
                MOCK_DID,
                MOCK_PHONE_NUMBER,
                MOCK_NAME
        );
        when(healthVerificationDAOV2.insertHealthVerificationClaim(any())).thenReturn(1);
        when(claimIdUtil.generateClaimId(MOCK_DID)).thenReturn(
                new ClaimIdUtil().generateClaimId(MOCK_DID)
        );
        when(healthVerificationClaimContract.getIssuerAddress()).thenReturn(ISSUER_ADDRESS);
        when(healthVerificationClaimContract.getIssuerPrivateKey()).thenReturn(MOCK_PRIVATE_KEY);

        JwtResponse jwtResponse = vcServiceV2.createHealthVerification(createVCRequest);
        Assertions.assertNotNull(jwtResponse.getToken());

        jwtResponse = vcServiceV2.createImmunoglobulinDetectionVC(createVCRequest);
        Assertions.assertNotNull(jwtResponse.getToken());

        jwtResponse = vcServiceV2.createPassportVC(createVCRequest);
        Assertions.assertNotNull(jwtResponse.getToken());
    }

    @Test
    public void should_pass_verify_and_return_travel_badge() {
        CreateVCRequest createVCRequest = new CreateVCRequest(
                MOCK_DID,
                MOCK_PHONE_NUMBER,
                MOCK_NAME
        );
        when(healthVerificationDAOV2.insertHealthVerificationClaim(any())).thenReturn(1);
        when(claimIdUtil.generateClaimId(MOCK_DID)).thenReturn(
                new ClaimIdUtil().generateClaimId(MOCK_DID)
        );
        when(healthVerificationClaimContract.getIssuerAddress()).thenReturn(ISSUER_ADDRESS);
        when(healthVerificationClaimContract.getIssuerPrivateKey()).thenReturn(MOCK_PRIVATE_KEY);
        JwtResponse vc = vcServiceV2.createHealthVerification(createVCRequest);
        when(verifierDAO.getVerifierById(MOCK_DID)).thenReturn(Verifier.builder()
                .id(MOCK_DID)
                .name(MOCK_NAME)
                .vcTypes(Arrays.asList(HEALTH_VC_TYPE_ID))
                .build()
        );

        JwtResponse travelBadge = vcServiceV2.VerifyHealthVerification(new VerifyJwtTokensRequest(
                MOCK_DID,
                Arrays.asList(vc.getToken())
        ), true);
        Assertions.assertNotNull(travelBadge.getToken());

        VerifyJwtResponse verifyResult = vcServiceV2.VerifyTravelBadgeVC(new VerifyJwtRequest(MOCK_DID, travelBadge.getToken()));
        Assertions.assertEquals(VerifyResultEnum.TRUE, verifyResult.getVerifySignature());
        Assertions.assertEquals(VerifyResultEnum.FALSE, verifyResult.getOverdue());
    }
}
