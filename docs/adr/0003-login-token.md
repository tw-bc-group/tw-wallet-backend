# 3. login token

Date: 2020-05-22

## Status

2020-05-22 proposed

## Context

tw-wallet backend should have a convenient mechanism to protect API.

0. DID API should be protected by 
    - phone verification 
    - kyc process
    - OAuth2: WeChat etc... 
    - CAPTCHA to avoid DDoS
1. tw-wallet client get a DID
    - wallet stores private key
    - DID smart contract store public key and DID
2. use private key to sign a message "wallet request login" and send it to server.
    - signature
    - message: "wallet request login"
    - DID
    - nonce: 7 numbers
    - UTC timestamp
3. Server verify signature if success return a JWT Token.
4. Client use JWT Token to request APIs.
5. Client should refresh JWT token if it expired

## Decision

Decision here...

## Consequences

Consequences here...
