create table if not exists transaction (
    id                      bigserial
        primary key,
    payment_method          varchar(32),
    amount                  numeric(10, 2)          not null,
    currency                varchar(32)             not null,
    external_transaction_id bigint                  not null,
    created_at              timestamp default now() not null,
    updated_at              timestamp default now() not null,
    cart_number             varchar(16)             not null,
    cvv                     varchar(4)              not null,
    exp_date                date                    not null,
    language                varchar(8)              not null,
    notification_url        varchar(256)            not null,
    customer_first_name     varchar(128)            not null,
    customer_last_name      varchar(128)            not null,
    customer_country        varchar(16)             not null,
    status                  varchar(16)             not null,
    merchant_id             bigint                  not null
        constraint transaction_merchant_id_fk
        references merchant
);

alter table transaction
    owner to postgres;

