# 12. 健康码 vc 新的json-ld格式

Date: 2020-12-23

## Status

2020-12-23 proposed

## Context

### 原来的格式
本次改进需要对健康码增加一些属性

原来的健康码是基于jwt格式，参考《健康认证的技术方案》，关键load这样

````json
{
   "@context": ["https://www.w3.org/2018/credentials/v1",
   "https://blockchain.thoughtworks.cn/credentials/v1"],
   "id": "xyzxyzxyz",
   "ver": "0.7.0",
   "iss": "did:tw:xxx",
   "iat": 1588059342,
   "exp": 1651131342,
   "typ": ["VerifiableCredential", "HealthyCredential"],
   "sub": {
     "id": "did:tw:xxx",
     "healthyStatus": {
       "typ": "HealthyStatus",
       "val": "HEALTHY"
     }
   }
}
````

### 问题

有如下问题
1. `https://blockchain.thoughtworks.cn/credentials/v1` 是一个占位符，并没有用。
2. 健康码信息太简单，不是很专业，应该用COVID-19词汇表

### 新冠VC的上下文

针对第一个问题，可以自己建立对象类型，提供一个真实的接口

CCI这个组织以及建立一个对象，我们可以复用一下
https://github.com/w3c-ccg/vc-examples/blob/master/docs/covid-19/v2/v2.jsonld

```json
{
  "@context": {
    "@version": 1.1,
    "@protected": true,
    "name": "http://schema.org/name",
    "description": "http://schema.org/description",
    "image": {
      "@id": "http://schema.org/image",
      "@type": "@id"
    },
    "location": {
      "@id": "http://schema.org/location",
      "@type": "@id"
    },
    "url": {
      "@id": "http://schema.org/url",
      "@type": "@id"
    },
    "qSARS-CoV-2-Rapid-Test-Credential": {
      "@id": "https://w3c-ccg.github.io/vc-examples/covid-19/v2/v2#qSARS-CoV-2-Rapid-Test-Credential",
      "@context": {
        "@version": 1.1,
        "@protected": true,
        "id": "@id",
        "type": "@type",
        "covid-19": "https://w3c-ccg.github.io/vc-examples/covid-19/v2/v2#",
        "catalogNumber": {
          "@id": "covid-19:catalogNumber"
        },
        "ifu": {
          "@id": "covid-19:ifu"
        },
        "assay": {
          "@id": "covid-19:assay"
        }
      }
    },
    "qSARS-CoV-2-Travel-Badge-Credential": {
      "@id": "https://w3c-ccg.github.io/vc-examples/covid-19/v2/v2#qSARS-CoV-2-Travel-Badge-Credential",
      "@context": {
        "@version": 1.1,
        "@protected": true,
        "id": "@id",
        "type": "@type",
        "image": {
          "@id": "http://schema.org/image",
          "@type": "@id"
        }
      }
    },
    "Person": "http://schema.org/Person"
  }
}
```

### 新冠病毒检测VC

针对第二个问题，可以使用新定义的对象的词汇，现在改进的VC格式，用json-ld表示：
```json

{
  "@context": [
    "https://www.w3.org/2018/credentials/v1",
    "https://w3c-ccg.github.io/vc-examples/covid-19/v2/v2.jsonld"
  ],
  "id": "http://example.com/credential/123",
  "type": [
    "VerifiableCredential",
    "qSARS-CoV-2-Rapid-Test-Credential"
  ],
  "issuer": {
    "id": "did:elem:ropsten:EiBJJPdo-ONF0jxqt8mZYEj9Z7FbdC87m2xvN0_HAbcoEg",
    "location": {
      "@type": "CovidTestingFacility",
      "name": "Stanford Health Care",
      "url": "https://stanfordhealthcare.org/"
    }
  },
  "issuanceDate": "2019-12-11T03:50:55Z",
  "expirationDate": "2020-12-11T03:50:55Z",
  "name": "qSARS-CoV-2 IgG/IgM Rapid Test Credential",
  "description": "Results from antibody testing should not be used as the sole basis to diagnose or exclude SARS-CoV-2 infection. False positive results may occur due to cross-reacting antibodies from previous infections, such as other coronaviruses, or from other causes Samples with positive results should be confirmed with alternative testing method(s) and clinical findings before a diagnostic determination is made.",
  "credentialSubject": {
    "id": "did:key:z6MkjRagNiMu91DduvCvgEsqLZDVzrJzFrwahc4tXLt9DoHd",
    "type": [
      "qSARS-CoV-2-Rapid-Test-Credential"
    ],
    "catalogNumber": "5515C025, 5515C050, 5515C100",
    "ifu": "https://cellexcovid.com/wp-content/uploads/2020/04/Cellex-rapid-ifu.pdf",
    "assay": "Negative"
  },
  "proof": {
    "type": "Ed25519Signature2018",
    "created": "2020-04-18T18:35:13Z",
    "verificationMethod": "did:elem:ropsten:EiBJJPdo-ONF0jxqt8mZYEj9Z7FbdC87m2xvN0_HAbcoEg#xqc3gS1gz1vch7R3RvNebWMjLvBOY-n_14feCYRPsUo",
    "proofPurpose": "assertionMethod",
    "jws": "eyJhbGciOiJFZERTQSIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..xnB7m8M6TcAFmz2efqb74IyJECUTAMpCkJAudfmVkLC3CPmCrMznvlD2E7WCCkzF9nnrZlJw0VpHdXJpjEU-AQ"
  }
}

```

### 新冠检测 jwt 格式VC

```jwt
eyJraWQiOiJfUXEwVUwyRnE2NTFRMEZqZDZUdm5ZRS1mYUhpT3BSbFBWUWNZXy10QTRBIiwiYWxnIjoiRWREU0EifQ.eyJzdWIiOiJkaWQ6a2V5Ono2TWtqUmFnTmlNdTkxRGR1dkN2Z0VzcUxaRFZ6ckp6RnJ3YWhjNHRYTHQ5RG9IZCIsImlzcyI6ImRpZDp3ZWI6dmMudHJhbnNtdXRlLndvcmxkIiwibmJmIjoxNTc2MDM2MjU1LCJleHAiOjE2MDc2NTg2NTUsInZjIjp7IkBjb250ZXh0IjpbImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL3YxIiwiaHR0cHM6Ly93M2MtY2NnLmdpdGh1Yi5pby92Yy1leGFtcGxlcy9jb3ZpZC0xOS92Mi92Mi5qc29ubGQiXSwiaWQiOiJodHRwOi8vZXhhbXBsZS5jb20vY3JlZGVudGlhbC8xMjMiLCJ0eXBlIjpbIlZlcmlmaWFibGVDcmVkZW50aWFsIiwicVNBUlMtQ29WLTItUmFwaWQtVGVzdC1DcmVkZW50aWFsIl0sImlzc3VlciI6eyJpZCI6ImRpZDp3ZWI6dmMudHJhbnNtdXRlLndvcmxkIiwibG9jYXRpb24iOnsiQHR5cGUiOiJDb3ZpZFRlc3RpbmdGYWNpbGl0eSIsIm5hbWUiOiJTdGFuZm9yZCBIZWFsdGggQ2FyZSIsInVybCI6Imh0dHBzOi8vc3RhbmZvcmRoZWFsdGhjYXJlLm9yZy8ifX0sImlzc3VhbmNlRGF0ZSI6IjIwMTktMTItMTFUMDM6NTA6NTVaIiwiZXhwaXJhdGlvbkRhdGUiOiIyMDIwLTEyLTExVDAzOjUwOjU1WiIsIm5hbWUiOiJxU0FSUy1Db1YtMiBJZ0cvSWdNIFJhcGlkIFRlc3QgQ3JlZGVudGlhbCIsImRlc2NyaXB0aW9uIjoiUmVzdWx0cyBmcm9tIGFudGlib2R5IHRlc3Rpbmcgc2hvdWxkIG5vdCBiZSB1c2VkIGFzIHRoZSBzb2xlIGJhc2lzIHRvIGRpYWdub3NlIG9yIGV4Y2x1ZGUgU0FSUy1Db1YtMiBpbmZlY3Rpb24uIEZhbHNlIHBvc2l0aXZlIHJlc3VsdHMgbWF5IG9jY3VyIGR1ZSB0byBjcm9zcy1yZWFjdGluZyBhbnRpYm9kaWVzIGZyb20gcHJldmlvdXMgaW5mZWN0aW9ucywgc3VjaCBhcyBvdGhlciBjb3JvbmF2aXJ1c2VzLCBvciBmcm9tIG90aGVyIGNhdXNlcyBTYW1wbGVzIHdpdGggcG9zaXRpdmUgcmVzdWx0cyBzaG91bGQgYmUgY29uZmlybWVkIHdpdGggYWx0ZXJuYXRpdmUgdGVzdGluZyBtZXRob2QocykgYW5kIGNsaW5pY2FsIGZpbmRpbmdzIGJlZm9yZSBhIGRpYWdub3N0aWMgZGV0ZXJtaW5hdGlvbiBpcyBtYWRlLiIsImNyZWRlbnRpYWxTdWJqZWN0Ijp7ImlkIjoiZGlkOmtleTp6Nk1ralJhZ05pTXU5MURkdXZDdmdFc3FMWkRWenJKekZyd2FoYzR0WEx0OURvSGQiLCJ0eXBlIjpbInFTQVJTLUNvVi0yLVJhcGlkLVRlc3QtQ3JlZGVudGlhbCJdLCJjYXRhbG9nTnVtYmVyIjoiNTUxNUMwMjUsIDU1MTVDMDUwLCA1NTE1QzEwMCIsImlmdSI6Imh0dHBzOi8vY2VsbGV4Y292aWQuY29tL3dwLWNvbnRlbnQvdXBsb2Fkcy8yMDIwLzA0L0NlbGxleC1yYXBpZC1pZnUucGRmIiwiYXNzYXkiOiJOZWdhdGl2ZSJ9fX0.JGIML6Yjg8ka6EjYGbK0I4VApjaVzL88JNe-Wl8NtptNGHMu27U9viVnTD2ZBPffPC7izDpWaCg-sQFr6LrICw
```

可以在这边测试 https://jwt.io/

### 通行证 VC

```json
{
  "@context": [
    "https://www.w3.org/2018/credentials/v1",
    "https://w3c-ccg.github.io/vc-examples/covid-19/v2/v2.jsonld"
  ],
  "id": "http://example.com/credential/123",
  "type": [
    "VerifiableCredential",
    "qSARS-CoV-2-Travel-Badge-Credential"
  ],
  "issuer": {
    "id": "did:elem:ropsten:EiBJJPdo-ONF0jxqt8mZYEj9Z7FbdC87m2xvN0_HAbcoEg",
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
  },
  "proof": {
    "type": "Ed25519Signature2018",
    "created": "2020-04-18T18:35:13Z",
    "verificationMethod": "did:elem:ropsten:EiBJJPdo-ONF0jxqt8mZYEj9Z7FbdC87m2xvN0_HAbcoEg#xqc3gS1gz1vch7R3RvNebWMjLvBOY-n_14feCYRPsUo",
    "proofPurpose": "assertionMethod",
    "jws": "eyJhbGciOiJFZERTQSIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..pbZdiR4OtS6dqW2Dw8-oiQMXJiAsXJHufaAQrhYBNVL7bcXlVd_mCkr4fkigDR7X6O4J64Yqn2mBhbIgupDVBw"
  }
}

```

### jwt格式的通行证VC

```
eyJraWQiOiJfUXEwVUwyRnE2NTFRMEZqZDZUdm5ZRS1mYUhpT3BSbFBWUWNZXy10QTRBIiwiYWxnIjoiRWREU0EifQ.eyJzdWIiOiJkaWQ6a2V5Ono2TWtqUmFnTmlNdTkxRGR1dkN2Z0VzcUxaRFZ6ckp6RnJ3YWhjNHRYTHQ5RG9IZCIsImlzcyI6ImRpZDp3ZWI6dmMudHJhbnNtdXRlLndvcmxkIiwibmJmIjoxNTc2MDM2MjU1LCJleHAiOjE2MDc2NTg2NTUsInZjIjp7IkBjb250ZXh0IjpbImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL3YxIiwiaHR0cHM6Ly93M2MtY2NnLmdpdGh1Yi5pby92Yy1leGFtcGxlcy9jb3ZpZC0xOS92Mi92Mi5qc29ubGQiXSwiaWQiOiJodHRwOi8vZXhhbXBsZS5jb20vY3JlZGVudGlhbC8xMjMiLCJ0eXBlIjpbIlZlcmlmaWFibGVDcmVkZW50aWFsIiwicVNBUlMtQ29WLTItVHJhdmVsLUJhZGdlLUNyZWRlbnRpYWwiXSwiaXNzdWVyIjp7ImlkIjoiZGlkOndlYjp2Yy50cmFuc211dGUud29ybGQiLCJsb2NhdGlvbiI6eyJAdHlwZSI6IkNvdmlkVGVzdGluZ0ZhY2lsaXR5IiwibmFtZSI6IlN0YW5mb3JkIEhlYWx0aCBDYXJlIiwidXJsIjoiaHR0cHM6Ly9zdGFuZm9yZGhlYWx0aGNhcmUub3JnLyJ9fSwiaXNzdWFuY2VEYXRlIjoiMjAxOS0xMi0xMVQwMzo1MDo1NVoiLCJleHBpcmF0aW9uRGF0ZSI6IjIwMjAtMTItMTFUMDM6NTA6NTVaIiwibmFtZSI6InFTQVJTLUNvVi0yIFRyYXZlbCBCYWRnZSBDcmVkZW50aWFsIiwiZGVzY3JpcHRpb24iOiJUaGlzIGNhcmQgaXMgYWNjZXB0ZWQsIHVuZGVyIHNwZWNpZmllZCBjb25kaXRpb25zLCBhcyBwcm9vZiBvZiBtZWRpY2FsIGNsZWFyYW5jZSBhbmQgZm9yIGlkZW50aWZpY2F0aW9uIG9mIHRoZSBob2xkZXLigJlzIG1lZGljYWwgY29uZGl0aW9uLiBTZWUgaHR0cHM6Ly93d3cud2hvLmludC9pdGgvbW9kZV9vZl90cmF2ZWwvdHJhdmVsbGVycy9lbi8uIiwiY3JlZGVudGlhbFN1YmplY3QiOnsiaWQiOiJkaWQ6a2V5Ono2TWtqUmFnTmlNdTkxRGR1dkN2Z0VzcUxaRFZ6ckp6RnJ3YWhjNHRYTHQ5RG9IZCIsInR5cGUiOlsiUGVyc29uIl0sImltYWdlIjoiaHR0cHM6Ly9jZG4ucGl4YWJheS5jb20vcGhvdG8vMjAxNC8xMC8yNi8yMS80Mi9tYW4tNTA0NDUzXzEyODAuanBnIn19fQ.jaVoZC0lShp6ahzs-yI7hmE-M3OhkY1ialf4pCkEVCv1aVDHXLGOdipgTIGnQ045IIpF5rRc7-2MiItWCfRZBw
```

## Decision

采用上述格式的新冠VC和通行证

## Consequences

Consequences here...
