# 4. enhance claim verification

Date: 2020-05-22

## Status

2020-05-22 proposed

## Context

ref "0001-verifiable-claim.md"

We have not implemented all verification in the architecture doc "健康认证的技术方案.pages".
https://dev.tw-wallet.in2e.com/v1/health-certifications/verify just support expired claim and signature verification.

```$xslt
{
  "code": 0,
  "msg": "SUCCESS",
  "result": {
    "outdate": "TRUE",
    "revoked": "NOT_SUPPORT",
    "onchain": "NOT_SUPPORT",
    "verify_issuer": "NOT_SUPPORT",
    "verify_signature": "TRUE",
    "verify_holder": "NOT_SUPPORT"
  }
}
```

Yin suggest support "revoked","onchain","verify_issuer" and "verify_holder" verification

### revoked

Issuer call Claims Smart Contract to revoke:
- claimId
- reason
- signature of tx

Smart Contract should store:
- claimId
- issuerId
- holderId
- revoked
- reason

### on chain

Query in smart contract by claimId .

### verify holder

avoid use others' claim.

1. get DID in jwt token. ref "0003-login-token.md"
2. compare DID
3. verify signature


### verify issuer

1. use issuer's public key to verify signature and message

> issuers' public key store in DID, 
> but in backend server, we have healthy claim issuer's private key, 
> so server do not need to query DID smart contract
> this is a temporary mock


## Decision

Decision here...

## Consequences

Consequences here...
