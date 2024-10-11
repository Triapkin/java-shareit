DROP TABLE IF EXISTS requests, comments,bookings,items, users;


CREATE TABLE IF NOT EXISTS users
(
    id    integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255)        NOT NULL,
    email VARCHAR(512) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS requests
(
    id           integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description  VARCHAR(255) NOT NULL,
    requestor_id integer       NOT NULL,
    created      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (requestor_id) REFERENCES users (id)
    );

CREATE TABLE IF NOT EXISTS items
(
    id          integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    is_available   BOOLEAN      NOT NULL,
    owner_id    integer       NOT NULL,
    request_id  integer,
    FOREIGN KEY (owner_id) REFERENCES users (id),
    FOREIGN KEY (request_id) REFERENCES requests (id)
    );

CREATE TABLE IF NOT EXISTS bookings
(
    id         integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    integer                      NOT NULL,
    booker_id  integer                      NOT NULL,
    status     varchar(15)                 NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (booker_id) REFERENCES users (id)
    );

CREATE TABLE IF NOT EXISTS comments
(
    id        integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text      VARCHAR(255) NOT NULL,
    item_id   integer       NOT NULL,
    author_id integer       NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
    );