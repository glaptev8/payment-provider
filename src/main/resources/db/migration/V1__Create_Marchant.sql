create table if not exists merchant (
    id            bigserial
        constraint merchant_id_pk
        primary key,
    merchant_name varchar(128)  not null
        constraint merchant_merchant_name_pk
        unique,
    secret        varchar(2064) not null
);

create index if not exists merchant_merchant_name_index
    on merchant(merchant_name);