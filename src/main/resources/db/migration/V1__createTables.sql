CREATE EXTENSION IF NOT EXISTS 'uuid-ossp';

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
    country_id int REFERENCES countries (id)
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

CREATE TABLE if not exists individuals
(
    id             UUID PRIMARY KEY default uuid_generate_v4(),
    passportNumber varchar(32),
    phoneNumber    varchar(32),
    user_id        UUID REFERENCES users (id),
    status         varchar(32)
);

CREATE TABLE if not exists user_histories
(
    id             UUID PRIMARY KEY default uuid_generate_v4(),
    created        timestamp,
    user_id        UUID REFERENCES users (id),
    user_type      varchar(32),
    reason         varchar(255),
    comment        varchar(255),
    changed_values JSONB
);

INSERT INTO countries(id, name, alpha2, alpha3) VALUES (1, 'Russia', 'RU', 'RUS');
INSERT INTO countries(id, name, alpha2, alpha3) VALUES (2, 'Unated States', 'US', 'USA');

INSERT INTO addresses(id, address, city, state, zip_code, country_id) VALUES ('7e46b49e-f181-448d-a1ae-d3586383737d', 'First str, bld. apr.1', 'Moscow', 'Moscow Region', '117525', 1);
INSERT INTO addresses(id, address, city, state, zip_code, country_id) VALUES ('d08beba0-f42e-4e08-81c2-79746ae3e01e', 'Main str, bld. apr.1', 'NY', 'NY', '999111', 2);

INSERT INTO users(id, secret_key, first_name, last_name, created_at, updated_at, verified_at, archived_at, filled, address_id)
VALUES ('a696e5c8-2c39-4ccb-baa1-770ded40dea3', 'secretKey1', 'FirstNameUser1', 'LastNameUser2', '2024-01-01','2024-03-01', '2024-03-01', null, true, '7e46b49e-f181-448d-a1ae-d3586383737d');
INSERT INTO users(id, secret_key, first_name, last_name, created_at, updated_at, verified_at, archived_at, filled, address_id)
VALUES ('2cd4077c-a522-43df-bc4f-5566d50669fa', 'secretKey1', 'FirstNameUser1', 'LastNameUser2', '2024-01-01','2024-03-01', '2024-03-01', null, true, 'd08beba0-f42e-4e08-81c2-79746ae3e01e');

INSERT INTO individuals(id, passportNumber, phoneNumber, user_id, status)
VALUES ('da431b33-e9d8-4ff8-8ae3-aaa64a1bbca4', '11 22 333333', '+79991112233', 'a696e5c8-2c39-4ccb-baa1-770ded40dea3', 'ACTIVE');
INSERT INTO individuals(id, passportNumber, phoneNumber, user_id, status)
VALUES ('942eb844-bff5-4d61-a904-7b5092c80d5d', '99 88 777777', '+12223334455', '2cd4077c-a522-43df-bc4f-5566d50669fa', 'ACTIVE');
