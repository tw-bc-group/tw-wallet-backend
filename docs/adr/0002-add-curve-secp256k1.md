# 2. add curve SECP256k1

Date: 2020-05-20

## Status

2020-05-20 completed

## Context

we want to support ECDSA with Curve SECP256k1

## Decision

- wrapper bouncycastle to support more algorithms, SECP256k1 etc...
- generate JWT signature algorithms: `signature := Base64(sign(Base64(header).Base64(payload)))`


## Consequences

- support ECDSA with Curve：
    - SECP256k1 
    - P-256 etc...
    - SM2
