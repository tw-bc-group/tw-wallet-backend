package com.thoughtworks.wallet.healthy.service;

import com.thoughtworks.wallet.healthy.dto.*;

import java.io.IOException;

public interface IVerifierService {
    VerifierResponse createVerifier(VerifierRequest verifierRequest);
    VerifierResponse getVerifierById(Integer id);
    VerifierResponse updateVerifierVcTypes(Integer id, VerifierVcTypesRequest request);
    VerifierVcResponse getVerifierVc(Integer id) throws IOException;
}
