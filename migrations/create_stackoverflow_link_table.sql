CREATE TABLE IF NOT EXISTS stackoverflow_link
(
    id           BIGINT REFERENCES link (id) ON DELETE CASCADE,
    answer_count BIGINT,
    score        BIGINT,
    PRIMARY KEY (id)
);
