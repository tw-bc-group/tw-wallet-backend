# 5. refine blacklist mechanism

Date: 2020-05-22

## Status

2020-05-22 proposed

## Context

blacklist is a module of healthy claim issuer, when create claim, the persons in the blacklist will get unhealthy claim.

In the backend server, when we remove/add a person in blacklists, claim do not change until we call `/v1/health-certifications/{ownerId}`


## Decision

should be refined as:

1. just have created API


## Consequences

Consequences here...
