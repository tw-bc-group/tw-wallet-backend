DROP TABLE tbl_healthy_verification;

DROP TABLE IF EXISTS tbl_healthy_verification_claim;

CREATE TABLE tbl_healthy_verification_claim
(
    id      varchar(100)               not null,
    ver     varchar(10)                not null,
    context varchar(100)               not null,
    iss     varchar(100)               not null,
    iat     BIGINT                     not null,
    exp     BIGINT                     not null,
    typ     varchar(50)                not null,
    sub     varchar(512)               not null,

    constraint tbl_healthy_verification_claim_pkey
        primary key(id)
);