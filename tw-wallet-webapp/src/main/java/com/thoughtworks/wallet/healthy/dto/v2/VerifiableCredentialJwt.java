package com.thoughtworks.wallet.healthy.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * {
 * "iss": "did:web:vc.transmute.world", //颁发者id，
 * "ver": "0.7.0",
 * "iat": 1576036255,
 * "exp": 1607658655,
 * "vc": {
 * "@context": [
 * "https://www.w3.org/2018/credentials/v1",
 * "https://w3c-ccg.github.io/vc-examples/covid-19/v2/v2.jsonld"
 * ],
 * "id": "http://example.com/credential/123", // 证书的id
 * "typ": [
 * "VerifiableCredential",
 * "qSARS-CoV-2-Rapid-Test-Credential"
 * ],
 * "issuer": {
 * "location": {
 * "@type": "CovidTestingFacility",
 * "name": "Stanford Health Care",
 * "url": "https://stanfordhealthcare.org/"
 * }
 * },
 * "name": "qSARS-CoV-2 IgG/IgM Rapid Test Credential",
 * "desc": "Results from antibody testing should not be used as the sole basis to diagnose or exclude SARS-CoV-2 infection. False positive results may occur due to cross-reacting antibodies from previous infections, such as other coronaviruses, or from other causes Samples with positive results should be confirmed with alternative testing method(s) and clinical findings before a diagnostic determination is made.",
 * "sub": {
 * "id": "did:key:z6MkjRagNiMu91DduvCvgEsqLZDVzrJzFrwahc4tXLt9DoHd", //可信声明接收者的ID
 * "type": [
 * "qSARS-CoV-2-Rapid-Test-Credential"
 * ],
 * "catalogNumber": "5515C025, 5515C050, 5515C100",
 * "ifu": "https://cellexcovid.com/wp-content/uploads/2020/04/Cellex-rapid-ifu.pdf",
 * "assay": "Negative"
 * }
 * }
 * }
 */

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifiableCredentialJwt {

    // 颁发者 ID
    private String iss;
    private String ver;
    private long iat;
    private long exp;
    private VC vc;

    @JsonIgnore
    private String token;
}
