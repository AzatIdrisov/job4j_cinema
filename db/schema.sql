CREATE TABLE account
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    email    VARCHAR NOT NULL UNIQUE,
    phone    VARCHAR NOT NULL UNIQUE
);

CREATE TABLE ticket
(
    id         SERIAL PRIMARY KEY,
    session_id INT NOT NULL,
    row        INT NOT NULL,
    col       INT NOT NULL,
    account_id INT NOT NULL REFERENCES account (id),
    unique (session_id, row, col)
);

create table hall
(
    id     serial primary key,
    row    int,
    col    int,
    isAvailable boolean
);