DROP TABLE IF EXISTS tbl_dcep;
create table tbl_dcep
(
    serial_number varchar(255)                                       not null PRIMARY KEY,
    owner         varchar(255)                                       not null,
    signature     varchar(255)                                       not null,
    money_type    varchar(20)                                        not null,
    create_time   timestamp with time zone default CURRENT_TIMESTAMP not null
);

CREATE INDEX IF NOT EXISTS tbl_blocks_owner_index ON tbl_dcep (owner);