create table if not exists balance (
    id          bigserial
        constraint balance_id_pk
        primary key,
    balance     numeric(10, 2) not null,
    currency    varchar(32)    not null,
    merchant_id bigint         not null
        constraint balance_merchant_id_fk
        references merchant,
    constraint balance_merchant_id_currency_pk
        unique (merchant_id, currency)
);

create index if not exists balance_merchant_id_index
    on balance(merchant_id);
