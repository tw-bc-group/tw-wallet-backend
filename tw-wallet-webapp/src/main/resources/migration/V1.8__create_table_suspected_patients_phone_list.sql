DROP TABLE IF EXISTS tbl_suspected_patients_phone_list;

create table tbl_suspected_patients_phone_list
(
    phone       varchar(20)                                         not null,
    create_time timestamp with time zone default CURRENT_TIMESTAMP  not null,

    constraint tbl_suspected_patients_phone_list_pkey
        primary key(phone)
);