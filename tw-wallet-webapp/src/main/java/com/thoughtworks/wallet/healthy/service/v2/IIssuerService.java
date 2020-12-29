package com.thoughtworks.wallet.healthy.service.v2;

import com.thoughtworks.wallet.healthy.dto.v2.IssuerRequest;
import com.thoughtworks.wallet.healthy.dto.v2.IssuerResponse;

import java.util.List;

public interface IIssuerService {
    IssuerResponse createIssuer(IssuerRequest issuerRequest);
    IssuerResponse getIssuerById(Integer id);
    List<IssuerResponse> getAllIssuers();
}
