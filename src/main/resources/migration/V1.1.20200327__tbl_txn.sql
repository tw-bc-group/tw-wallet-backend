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

