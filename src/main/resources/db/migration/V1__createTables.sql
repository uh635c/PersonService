CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE if not exists countries
(
    id     serial PRIMARY KEY,
    name   varchar(32),
    alpha2 varchar(2),
    alpha3 varchar(3)
);

CREATE TABLE if not exists addresses
(
    id         UUID PRIMARY KEY default uuid_generate_v4(),
    address    varchar(128),
    city       varchar(32),
    state      varchar(32),
    zip_code   varchar(32),
    country_id UUID REFERENCES countries (id)
);

CREATE TABLE if not exists users
(
    id          UUID PRIMARY KEY default uuid_generate_v4(),
    secret_key  varchar(32) NOT NULL,
    first_name  varchar(32) NOT NULL,
    last_name   varchar(32) NOT NULL,
    created_at  timestamp,
    updated_at  timestamp,
    verified_at timestamp,
    archived_at timestamp,
    filled      boolean,
    address_id  UUID REFERENCES addresses (id)
);

CREATE TABLE if not exists individual
(
    id             UUID PRIMARY KEY default uuid_generate_v4(),
    passportNumber varchar(32),
    phoneNumber    varchar(32),
    user_id        UUID REFERENCES users (id),
    status         varchar(32)
);