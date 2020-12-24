DROP TABLE IF EXISTS tbl_issuers;
create table tbl_issuers
(
    id      varchar(128)    not null,
    name    varchar(128)    not null,

    constraint tbl_issuers_pkey
            primary key(id)
);