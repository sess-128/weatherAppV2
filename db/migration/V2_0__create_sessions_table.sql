CREATE TABLE IF NOT EXISTS sessions
(
    id         UUID      NOT NULL PRIMARY KEY,
    user_id    BIGINT REFERENCES users (id),
    expires_at TIMESTAMP NOT NULL
);