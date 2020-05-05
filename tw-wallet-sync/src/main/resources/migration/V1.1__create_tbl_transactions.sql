DROP TABLE IF EXISTS tbl_transactions;
-- auto-generated definition
create table tbl_transactions
(
    hash          varchar(255)              default ''''::character varying not null,
    tx_type       varchar(255)                                              ,
    create_time   timestamp with time zone default CURRENT_TIMESTAMP       ,
    height        bigint                                                  not null,
    amount        decimal(60)                                           ,
    asset_name    varchar(255)             default ''''::character varying not null,
    from_address  varchar(255)             default ''''::character varying not null,
    to_address    varchar(255)             default ''''::character varying not null,
    tx_index      bigint                                                  ,
    event_type    smallint                                                 ,
    contract_hash varchar(255)              default ''''::character varying ,
    constraint tbl_transactions_pkey
        primary key (height, tx_index)
);
create index tbl_transactions_hash_index
    on tbl_transactions (hash);

--
-- INSERT INTO public.tbl_transactions (hash, tx_type, height, amount, asset_name, from_address, to_address, tx_index, event_type, contract_hash)
-- VALUES ('9eff6287e55ea56b2abcf8d84a1a151e8a00e0f482ea0ee0448fef9f5d3ebad4', 1, 1, '123456789', 'ETH', 'ed9d02e382b34818e88b88a309c7fe71e65f419d', 'ca843569e3427144cead5e4d5999a3d0ccf92b8e', 1, 1, '9eff6287e55ea56b2abcf8d84a1a151e8a00e0f482ea0ee0448fef9f5d3ebad4');