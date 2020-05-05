DROP TABLE IF EXISTS tbl_identities;
-- auto-generated definition
create table tbl_identities
(
    hash          varchar(255) default '''' not null,
    tx_type       varchar(255),
    create_time   timestamp    default CURRENT_TIMESTAMP,
    height        bigint                    not null,
    initiator     varchar(255) default '''' not null,
    owner_address varchar(255) default '''' not null,
    did           varchar(255) default '''' not null,
    public_key    varchar(255) default '''' not null,
    name          varchar(255) default '''' not null,
    tx_index      bigint,
    contract_hash varchar(255) default '''',
    constraint tbl_identities_pkey
        primary key (height, tx_index)
);
create index tbl_identities_hash_index
    on tbl_identities (hash);

