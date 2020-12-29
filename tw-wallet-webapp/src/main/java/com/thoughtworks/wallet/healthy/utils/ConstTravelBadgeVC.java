package com.thoughtworks.wallet.healthy.utils;

import com.google.common.collect.ImmutableList;

/**
 "sub": "did:key:z6MkjRagNiMu91DduvCvgEsqLZDVzrJzFrwahc4tXLt9DoHd",
 "iss": "did:web:vc.transmute.world",
 "nbf": 1576036255,
 "exp": 1607658655,
 "vc": {
 "@conte{xt": [
 "https://www.w3.org/2018/credentials/v1",
 "https://w3c-ccg.github.io/vc-examples/covid-19/v2/v2.jsonld"
 ],
 "id": "http://example.com/credential/123",
 "type": [
 "VerifiableCredential",
 "qSARS-CoV-2-Travel-Badge-Credential"
 ],
 "issuer": {
 "id": "did:web:vc.transmute.world",
 "location": {
 "@type": "CovidTestingFacility",
 "name": "Stanford Health Care",
 "url": "https://stanfordhealthcare.org/"
 }
 },
 "issuanceDate": "2019-12-11T03:50:55Z",
 "expirationDate": "2020-12-11T03:50:55Z",
 "name": "qSARS-CoV-2 Travel Badge Credential",
 "description": "This card is accepted, under specified conditions, as proof of medical clearance and for identification of the holder’s medical condition. See https://www.who.int/ith/mode_of_travel/travellers/en/.",
 "credentialSubject": {
 "id": "did:key:z6MkjRagNiMu91DduvCvgEsqLZDVzrJzFrwahc4tXLt9DoHd",
 "type": [
 "Person"
 ],
 "image": "https://cdn.pixabay.com/photo/2014/10/26/21/42/man-504453_1280.jpg"
 }
 }
 }
 */

public class ConstTravelBadgeVC {
    public static final ImmutableList<String> context = ImmutableList.of("https://www.w3.org/2018/credentials/v1", "https://w3c-ccg.github.io/vc-examples/covid-19/v2/v2.jsonld");
    public static final String TEST_TYPE = "qSARS-CoV-2-Travel-Badge-Credential";
    public static final ImmutableList<String> credentialType = ImmutableList.of("VerifiableCredential", TEST_TYPE);
    public static final ImmutableList<String> SUB_TYPE = ImmutableList.of("Person");
    public static final String ISSUER_TYPE = "CovidTestingFacility";
    public static final String ISSUER_NAME = "Stanford Health Care";
    public static final String ISSUER_URL = "https://stanfordhealthcare.org/";
    public static final String IMAGE_URL = "https://cdn.pixabay.com/photo/2014/10/26/21/42/man-504453_1280.jpg";
    public static final String VC_NAME = "qSARS-CoV-2 Travel Badge Credential";
    public static final String VC_DESC = "This card is accepted, under specified conditions, as proof of medical clearance and for identification of the holder’s medical condition. See https://www.who.int/ith/mode_of_travel/travellers/en/.";
}