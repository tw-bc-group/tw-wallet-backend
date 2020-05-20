# 2. add curve SECP256k1

Date: 2020-05-20

## Status

2020-05-20 completed

## Context

- We Use Eth lib to generate private key and public key
- Claim use JWT format
- JWT lib do not support SECP256k1
- we want to support more ECDSA algo

## Decision

- wrapper bouncycastle to support more algo
- generate JWT signature algo: `signature := Base64(sign(Base64(header).Base64(payload)))`


## Consequences

- define sequence and algorithm in specification
- write crypto lib
