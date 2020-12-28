DROP TABLE IF EXISTS tbl_healthy_verification_claim_v2;

CREATE TABLE tbl_healthy_verification_claim_v2
(
    owner_id  varchar(100) not null,
    claim_id  varchar(100) not null,
    issuer_id varchar(100) not null,
    payload   json         not null,
    jwt       varchar(2048) not null,
    constraint tbl_healthy_verification_claim_v2_pkey
        primary key (claim_id)
);