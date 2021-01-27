DROP TABLE IF EXISTS tbl_verifiers;
create table tbl_verifiers
(
    id              varchar(128)    not null,
    name            varchar(128)    not null,
    vc_types        text[]          not null,

    constraint tbl_verifiers_pkey
                primary key(id)
);