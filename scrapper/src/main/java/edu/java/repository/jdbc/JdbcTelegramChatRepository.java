package edu.java.repository.jdbc;

import edu.java.entity.TelegramChat;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcTelegramChatRepository implements JdbcRepository<TelegramChat> {
    private enum Query {
        ADD_TELEGRAM_CHAT("INSERT INTO telegram_chat (id, registered_at) VALUES (?, ?)"),
        DELETE_TELEGRAM_CHAT("DELETE FROM telegram_chat WHERE id=? RETURNING *"),
        SELECT_ALL_TELEGRAM_CHAT("SELECT * FROM telegram_chat");

        private final String sql;

        Query(String sql) {
            this.sql = sql;
        }

        public String getSql() {
            return sql;
        }
    }

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public TelegramChat add(TelegramChat entity) {
        jdbcTemplate.update(Query.ADD_TELEGRAM_CHAT.getSql(), entity.getId(), entity.getRegisteredAt());
        return entity;
    }

    @Override
    @Transactional
    public TelegramChat remove(TelegramChat entity) {
        return jdbcTemplate.queryForObject(Query.DELETE_TELEGRAM_CHAT.getSql(), new BeanPropertyRowMapper<>(TelegramChat.class), entity.getId());
    }


    @Override
    @Transactional(readOnly = true)
    public Collection<TelegramChat> findAll() {
        return jdbcTemplate.query(Query.SELECT_ALL_TELEGRAM_CHAT.getSql(), new BeanPropertyRowMapper<>(TelegramChat.class));
    }
}
