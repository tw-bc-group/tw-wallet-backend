package com.thoughtworks.wallet.healthy.service;

import com.thoughtworks.wallet.healthy.dto.VerifierRequest;
import com.thoughtworks.wallet.healthy.dto.VerifierResponse;

public interface IVerifierService {
    VerifierResponse createVerifier(VerifierRequest verifierRequest);
    VerifierResponse getVerifierById(Integer id);
}
