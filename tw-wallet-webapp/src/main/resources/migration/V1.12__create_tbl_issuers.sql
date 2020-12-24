DROP TABLE IF EXISTS tbl_issuers;
create table tbl_issuers
(
    id      serial          not null,
    name    varchar(128)    not null,

    constraint tbl_issuers_pkey
            primary key(id)
);