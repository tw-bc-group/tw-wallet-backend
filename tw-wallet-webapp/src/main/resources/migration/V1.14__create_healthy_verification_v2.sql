DROP TABLE IF EXISTS tbl_healthy_verification_claim_v2;

CREATE TABLE tbl_healthy_verification_claim_v2
(
    id      varchar(100)               not null,
    payload  json               not null,

    constraint tbl_healthy_verification_claim_v2_pkey
        primary key(id)
);