package com.thoughtworks.wallet.healthyVerifier.service.impl;

import com.thoughtworks.wallet.gen.tables.records.TblHealthyVerificationClaimRecord;
import com.thoughtworks.wallet.healthyVerifier.dto.ChangeHealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.dto.HealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.dto.HealthVerificationResponse;
import com.thoughtworks.wallet.healthyVerifier.exception.HealthVerificationAlreadyExistException;
import com.thoughtworks.wallet.healthyVerifier.exception.HealthVerificationNotFoundException;
import com.thoughtworks.wallet.healthyVerifier.exception.InsertIntoDatabaseErrorException;
import com.thoughtworks.wallet.healthyVerifier.model.HealthVerificationClaim;
import com.thoughtworks.wallet.healthyVerifier.model.HealthVerificationClaimContract;
import com.thoughtworks.wallet.healthyVerifier.repository.HealthVerificationDAO;
import com.thoughtworks.wallet.healthyVerifier.service.IHealthyVerifierService;
import com.thoughtworks.wallet.healthyVerifier.utils.ClaimUtil;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.thoughtworks.wallet.gen.Tables.TBL_HEALTHY_VERIFICATION_CLAIM;

@Slf4j
@Service
public class HealthyVerifierService implements IHealthyVerifierService {
    private final DSLContext                      dslContext;
    private final ClaimUtil                       claimUtil;
    private final HealthyClaimContractService     healthyClaimContractService;
    private final HealthVerificationClaimContract healthClaimContract;
    private final HealthVerificationDAO           healthVerificationDAO;
    private final ModelMapper                     modelMapper = new ModelMapper();

    public HealthyVerifierService(DSLContext dslContext, ClaimUtil claimUtil, HealthyClaimContractService healthyClaimContractService, HealthVerificationClaimContract healthClaimContract, HealthVerificationDAO healthVerificationDAO) {
        this.dslContext = dslContext;
        this.claimUtil = claimUtil;
        this.healthyClaimContractService = healthyClaimContractService;
        this.healthClaimContract = healthClaimContract;
        this.healthVerificationDAO = healthVerificationDAO;
    }

    @Override
    public HealthVerificationResponse createHealthVerification(HealthVerificationRequest healthVerification) {
        HealthVerificationClaim claim = claimUtil.generateHealthyVerificationClaim(healthVerification.getDid(), healthVerification.getPhone(), healthClaimContract.getIssuerAddress());

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

        healthyClaimContractService.createHealthVerification(healthClaimContract.getIssuerAddress(), claim.getId(), healthVerification.getDid(), claim.getIss());

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
    public HealthVerificationResponse changeHealthVerification(ChangeHealthVerificationRequest changeHealthVCRequest) {
        TblHealthyVerificationClaimRecord tblHealthyVerificationClaimRecord = Optional.ofNullable(dslContext
                .selectFrom(TBL_HEALTHY_VERIFICATION_CLAIM)
                .where(TBL_HEALTHY_VERIFICATION_CLAIM.OWNER.equal(changeHealthVCRequest.getOwnerId()))
                .fetchOne()).orElseThrow(() -> new HealthVerificationNotFoundException(changeHealthVCRequest.getOwnerId()));
        HealthVerificationClaim claim = claimUtil.changeHealthyVerificationClaim(tblHealthyVerificationClaimRecord, changeHealthVCRequest.getHealthyStatus().getStatus());
        healthVerificationDAO.updateHealthVerificationClaim(claim);
        return modelMapper.map(claim, HealthVerificationResponse.class);

    }
}
