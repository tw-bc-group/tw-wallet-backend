DROP TABLE IF EXISTS tbl_transactions;
-- auto-generated definition
create table tbl_transactions
(
    hash          varchar(64)              default ''''::character varying not null,
    tx_type       smallint                                                 not null,
    create_time   timestamp with time zone default CURRENT_TIMESTAMP       not null,
    height        integer                                                  not null,
    amount        money                                                    not null,
    asset_name    varchar(255)             default ''''::character varying not null,
    from_address  varchar(255)             default ''''::character varying not null,
    to_address    varchar(255)             default ''''::character varying not null,
    block_index   integer                                                  not null,
    tx_index      integer                                                  not null,
    confirm_flag  smallint                                                 not null,
    event_type    smallint                                                 not null,
    contract_hash varchar(64)              default ''''::character varying not null,
    constraint tbl_transactions_pkey
        primary key (hash, tx_index)
);

INSERT INTO public.tbl_transactions (hash, tx_type, height, amount, asset_name, from_address, to_address, block_index, tx_index, confirm_flag, event_type, contract_hash)
VALUES ('9eff6287e55ea56b2abcf8d84a1a151e8a00e0f482ea0ee0448fef9f5d3ebad4', 1, 1, '$1.11111111', 'ETH', 'ed9d02e382b34818e88b88a309c7fe71e65f419d', 'ca843569e3427144cead5e4d5999a3d0ccf92b8e', 1, 1, 1, 1, '9eff6287e55ea56b2abcf8d84a1a151e8a00e0f482ea0ee0448fef9f5d3ebad4');