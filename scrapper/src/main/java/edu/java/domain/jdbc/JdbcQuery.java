package edu.java.domain.jdbc;

public enum JdbcQuery {
    // LINK
    ADD_LINK("INSERT INTO link (url, last_updated_at) VALUES (?, ?) RETURNING *"),
    DELETE_LINK("DELETE FROM link WHERE id=? RETURNING *"),
    SELECT_ALL_LINK("SELECT * FROM link"),
    SELECT_ALL_WITH_INTERVAL("SELECT * FROM link WHERE last_updated_at < ?"),
    SELECT_BY_URL("SELECT * FROM link WHERE url=?"),
    ADD_CHAT_TO_LINK("INSERT INTO assignment (chat_id, link_id) VALUES (?, ?)"),
    DELETE_CHAT_TO_LINK("DELETE FROM assignment WHERE chat_id=? AND link_id=?"),
    SELECT_ALL_FOR_CHAT("SELECT * FROM link INNER JOIN assignment a on link.id = a.link_id WHERE a.chat_id=?"),
    SELECT_ALL_CHATS_FOR_LINK("SELECT chat_id FROM assignment WHERE link_id=?"),
    UPDATE_LINK("UPDATE link SET last_updated_at=? WHERE id=?"),
    // TELEGRAM CHAT
    ADD_TELEGRAM_CHAT("INSERT INTO telegram_chat (id, registered_at) VALUES (?, ?)"),
    DELETE_TELEGRAM_CHAT("DELETE FROM telegram_chat WHERE id=? RETURNING *"),
    SELECT_ALL_TELEGRAM_CHAT("SELECT * FROM telegram_chat");


    private final String query;

    JdbcQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
