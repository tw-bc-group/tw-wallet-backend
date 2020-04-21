DROP TABLE IF EXISTS tbl_blocks;
-- auto-generated definition
create table tbl_blocks
(
    height      bigint                                                  not null,
    hash        varchar(64)              default ''''::character varying not null,
    create_time timestamp with time zone default CURRENT_TIMESTAMP       not null,
    constraint tbl_blocks_pkey
        primary key (height)
);
--
-- INSERT INTO public.tbl_blocks (height, hash)
-- VALUES (0, '0');