package com.thoughtworks.wallet.healthy.service.impl;

import com.thoughtworks.wallet.healthy.dto.VerifierRequest;
import com.thoughtworks.wallet.healthy.dto.VerifierResponse;
import com.thoughtworks.wallet.healthy.model.Verifier;
import com.thoughtworks.wallet.healthy.repository.VcTypeDAO;
import com.thoughtworks.wallet.healthy.repository.VerifierDAO;
import com.thoughtworks.wallet.healthy.service.IVerifierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class VerifierService implements IVerifierService {
    private final VerifierDAO verifierDAO;
    private final VcTypeDAO vcTypeDAO;

    public VerifierService(VerifierDAO verifierDAO, VcTypeDAO vcTypeDAO) {
        this.verifierDAO = verifierDAO;
        this.vcTypeDAO = vcTypeDAO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VerifierResponse createVerifier(VerifierRequest verifierRequest) {
        verifierRequest.getVcTypes().forEach(vcTypeDAO::getVcTypeById);
        Integer id = verifierDAO.insertVerifier(Verifier.builder()
                .name(verifierRequest.getName())
                .privateKey(verifierRequest.getPrivateKey())
                .vcTypes(verifierRequest.getVcTypes())
                .build());
        return getVerifierById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VerifierResponse getVerifierById(Integer id) {
        Verifier verifier = verifierDAO.getVerifierById(id);
        return VerifierResponse.builder()
                .id(verifier.getId())
                .name(verifier.getName())
                .vcTypes(verifier.getVcTypes())
                .build();
    }
}
