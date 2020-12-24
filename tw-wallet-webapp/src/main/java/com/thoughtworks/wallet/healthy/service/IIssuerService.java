package com.thoughtworks.wallet.healthy.service;

import com.thoughtworks.wallet.healthy.dto.IssuerRequest;
import com.thoughtworks.wallet.healthy.dto.IssuerResponse;

import java.util.List;

public interface IIssuerService {
    IssuerResponse createIssuer(IssuerRequest issuerRequest);
    IssuerResponse getIssuerById(Integer id);
    List<IssuerResponse> getAllIssuers();
}
