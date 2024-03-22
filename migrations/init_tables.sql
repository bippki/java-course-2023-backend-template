CREATE TABLE IF NOT EXISTS telegram_chat
(
    id            BIGINT,
    reg_date TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS link
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY,
    url             VARCHAR(200) UNIQUE NOT NULL,
    last_upd_date TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);

CREATE INDEX idx_upd_date ON link (last_upd_date);

CREATE TABLE IF NOT EXISTS assignment
(
    chat_id BIGINT REFERENCES telegram_chat (id) ON DELETE CASCADE,
    link_id BIGINT REFERENCES link (id),
    PRIMARY KEY (chat_id, link_id)
)
