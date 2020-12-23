DROP TABLE IF EXISTS tbl_issuers;
create table tbl_issuers
(
    id      serial          not null primary key,
    name    varchar(255)    not null
);