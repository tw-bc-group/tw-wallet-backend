# 5. refine blacklist mechanism

Date: 2020-05-22

## Status

2020-05-22 proposed

## Context

blacklist is a module of healthy claim issuer, when create claim, the persons in the blacklist will get unhealthy claim.

But in the backend server, when we remove/add a person in blacklist, it's claim do not change until we call `/v1/health-certifications/{ownerId}`

should be refined as:

1. just have created API
2. client gets a new claim from healthy claim issuer after threshold


## Decision

Decision here...

## Consequences

Consequences here...
