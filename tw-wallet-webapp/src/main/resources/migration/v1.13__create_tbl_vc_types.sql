DROP TABLE IF EXISTS tbl_vc_types;
create table tbl_vc_types
(
    id      serial          not null primary key,
    name    varchar(255)    not null,
    issuer  int             not null,
    content text[]          not null,

    FOREIGN KEY(issuer) REFERENCES tbl_issuers(id) on delete cascade on update cascade
);