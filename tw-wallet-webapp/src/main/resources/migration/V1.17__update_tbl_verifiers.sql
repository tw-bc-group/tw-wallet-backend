DROP TABLE IF EXISTS tbl_verifiers;
create table tbl_verifiers
(
    id              serial          not null,
    name            varchar(128)    not null,
    private_key     varchar(256)    not null,
    vc_types        text[]          not null,

    constraint tbl_verifiers_pkey
                primary key(id)
);