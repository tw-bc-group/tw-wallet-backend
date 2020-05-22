# 2. add curve SECP256k1

Date: 2020-05-20

## Status

2020-05-20 completed

## Context

- Use Eth lib to generate private key and public key
- Claim use JWT format
- JWT lib do not support SECP256k1
- we want to support ECDSA with SECP256k1

## Decision

- wrapper bouncycastle to support more algos
- generate JWT signature algo: `signature := Base64(sign(Base64(header).Base64(payload)))`


## Consequences

- define sequence and algorithm in specification
- write crypto lib
- support ECDSA with Curve：
    - SECP256k1 
    - P-256 etc...
    - SM2
