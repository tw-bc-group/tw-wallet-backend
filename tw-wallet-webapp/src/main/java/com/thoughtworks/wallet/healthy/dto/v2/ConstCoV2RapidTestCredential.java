package com.thoughtworks.wallet.healthy.dto.v2;

import com.google.common.collect.ImmutableList;

public class ConstCoV2RapidTestCredential {
    public static final ImmutableList<String> context = ImmutableList.of("https://www.w3.org/2018/credentials/v1", "https://w3c-ccg.github.io/vc-examples/covid-19/v2/v2.jsonld");
    public static final String TEST_TYPE = "qSARS-CoV-2-Rapid-Test-Credential";
    public static final String CATALOG_NUMBER = "5515C025, 5515C050, 5515C100";
    public static final String IFU = "https://cellexcovid.com/wp-content/uploads/2020/04/Cellex-rapid-ifu.pdf";
    public static final ImmutableList<String> credentialType = ImmutableList.of("VerifiableCredential", TEST_TYPE);
    public static final String ISSUER_TYPE = "CovidTestingFacility";
    public static final String ISSUER_NAME = "Stanford Health Care";
    public static final String ISSUER_URL = "https://stanfordhealthcare.org/";
    public static final String VC_NAME = "qSARS-CoV-2 IgG/IgM Rapid Test Credential";
    public static final String VC_DESC = "Results from antibody testing should not be used as the sole basis to diagnose or exclude SARS-CoV-2 infection. False positive results may occur due to cross-reacting antibodies from previous infections, such as other coronaviruses, or from other causes Samples with positive results should be confirmed with alternative testing method(s) and clinical findings before a diagnostic determination is made.";
}
