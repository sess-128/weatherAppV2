CREATE TABLE IF NOT EXISTS locations (
    id SERIAL PRIMARY KEY,
    name varchar(20) not null ,
    user_id bigint references users(id),
    latitude DECIMAL NOT NULL,
    longitude DECIMAL NOT NULL
);