package com.thoughtworks.wallet.healthy.service.impl.v2;

import com.thoughtworks.wallet.healthy.dto.v2.IssuerRequest;
import com.thoughtworks.wallet.healthy.dto.v2.IssuerResponse;
import com.thoughtworks.wallet.healthy.dto.v2.VcTypeResponse;
import com.thoughtworks.wallet.healthy.model.Issuer;
import com.thoughtworks.wallet.healthy.repository.IssuerDAO;
import com.thoughtworks.wallet.healthy.repository.VcTypeDAO;
import com.thoughtworks.wallet.healthy.service.v2.IIssuerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IssuerService implements IIssuerService {
    private final IssuerDAO issuerDAO;
    private final VcTypeDAO vcTypeDAO;

    public IssuerService(IssuerDAO issuerDAO, VcTypeDAO vcTypeDAO) {
        this.issuerDAO = issuerDAO;
        this.vcTypeDAO = vcTypeDAO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IssuerResponse createIssuer(IssuerRequest issuerRequest) {
        Integer id = issuerDAO.insertIssuer(Issuer.builder()
                .name(issuerRequest.getName()).build());
        return IssuerResponse.builder()
                .id(id)
                .name(issuerRequest.getName())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IssuerResponse getIssuerById(Integer id) {
        Issuer issuer = issuerDAO.getIssuerById(id);
        List<VcTypeResponse> vcTypes = vcTypeDAO.getVcTypesByIssuer(issuer.getId()).stream()
                .map(vcType -> VcTypeResponse.builder()
                        .id(vcType.getId())
                        .name(vcType.getName())
                        .issuerId(vcType.getIssuerId())
                        .content(vcType.getContent())
                        .url(vcType.getUrl())
                        .build())
                .collect(Collectors.toList());
        return IssuerResponse.builder()
                .id(issuer.getId())
                .name(issuer.getName())
                .vcTypes(vcTypes)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<IssuerResponse> getAllIssuers() {
        return issuerDAO.getAllIssuers().stream()
                .map(Issuer::getId)
                .map(this::getIssuerById)
                .collect(Collectors.toList());
    }
}
