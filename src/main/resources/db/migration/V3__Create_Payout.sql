create table if not exists payout (
    id                      bigserial
        primary key,
    payment_method          varchar(32)    not null,
    amount                  numeric(10, 2) not null,
    currency                varchar(32)    not null,
    external_transaction_id bigint         not null,
    card_number             varchar(16)    not null,
    language                varchar(8)     not null,
    notification_url        varchar(256)   not null,
    customer_first_name     varchar(128)   not null,
    customer_last_name      varchar(128)   not null,
    customer_country        varchar(16)    not null,
    status                  varchar(16)    not null,
    merchant_id             bigint         not null
        constraint payout_merchant_id_fk
        references merchant
);

alter table payout
    owner to postgres;

