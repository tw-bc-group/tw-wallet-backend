DROP TABLE IF EXISTS tbl_txn_detail;
CREATE TABLE tbl_txn_detail
(
    txn_hash      varchar(64)  NOT NULL DEFAULT '''',
    txn_type      smallint     NOT NULL,
    txn_time      timestamp    NOT NULL,
    height        int          NOT NULL,
    amount        money        NOT NULL,
    asset_name    varchar(255) NOT NULL DEFAULT '''',
    from_address  varchar(255) NOT NULL DEFAULT '''',
    to_address    varchar(255) NOT NULL DEFAULT '''',
    block_index   int          NOT NULL,
    txn_index     int          NOT NULL,
    confirm_flag  smallint NOT NULL,
    event_type    smallint     NOT NULL,
    contract_hash varchar(64)  NOT NULL DEFAULT '''',
    PRIMARY KEY (txn_hash, txn_index)
);

INSERT INTO public.tbl_txn_detail (txn_hash, txn_type, txn_time, height, amount, asset_name, from_address, to_address, block_index, txn_index, confirm_flag, event_type, contract_hash)
VALUES ('9eff6287e55ea56b2abcf8d84a1a151e8a00e0f482ea0ee0448fef9f5d3ebad4', 1, '2020-03-27 15:08:58.000000', 1, '$1.11', 'ETH', 'ed9d02e382b34818e88b88a309c7fe71e65f419d', 'ca843569e3427144cead5e4d5999a3d0ccf92b8e', 1, 1, 1, 1, '9eff6287e55ea56b2abcf8d84a1a151e8a00e0f482ea0ee0448fef9f5d3ebad4');