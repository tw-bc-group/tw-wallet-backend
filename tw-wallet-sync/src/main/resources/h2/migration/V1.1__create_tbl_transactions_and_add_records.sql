DROP TABLE IF EXISTS tbl_transactions;
-- auto-generated definition
create table tbl_transactions
(
    hash          varchar(255) default ''''              not null,
    tx_type       varchar(255),
    create_time   timestamp    default CURRENT_TIMESTAMP,
    height        bigint                                 not null,
    amount        decimal(60),
    asset_name    varchar(255) default ''''              not null,
    from_address  varchar(255) default ''''              not null,
    to_address    varchar(255) default ''''              not null,
    tx_index      bigint,
    event_type    smallint,
    contract_hash varchar(255) default '''',
    constraint tbl_transactions_pkey
        primary key (height, tx_index)
);