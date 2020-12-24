DROP TABLE IF EXISTS tbl_vc_types;
create table tbl_vc_types
(
    id      serial          not null,
    name    varchar(128)    not null,
    issuer  int             not null,
    content text[]          not null,

    constraint tbl_vc_types_pkey
                primary key(id),
    FOREIGN KEY(issuer) REFERENCES tbl_issuers(id) on delete cascade on update cascade
);