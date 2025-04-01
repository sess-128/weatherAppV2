CREATE TABLE IF NOT EXISTS users
(
    id       serial PRIMARY KEY,
    name     varchar(20) unique not null,
    password varchar            not null
);