DROP TABLE IF EXISTS tbl_healthy_verification;

create table tbl_healthy_verification
(
    phone    varchar(20)                     not null,
    status   varchar(64) default 'unhealthy' not null,
    verification_time timestamp with time zone default CURRENT_TIMESTAMP   not null,

    constraint tbl_healthy_verification_pkey
        primary key(phone)
);