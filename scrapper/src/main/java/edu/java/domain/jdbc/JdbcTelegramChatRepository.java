package edu.java.domain.jdbc;

import edu.java.entity.TelegramChat;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.domain.jdbc.JdbcQuery.ADD_TELEGRAM_CHAT;
import static edu.java.domain.jdbc.JdbcQuery.DELETE_TELEGRAM_CHAT;
import static edu.java.domain.jdbc.JdbcQuery.SELECT_ALL_TELEGRAM_CHAT;

@Repository
@RequiredArgsConstructor
public class JdbcTelegramChatRepository implements JdbcRepository<TelegramChat> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public TelegramChat add(TelegramChat entity) {
        jdbcTemplate.update(ADD_TELEGRAM_CHAT.getQuery(), entity.getId(), entity.getRegisteredAt());
        return entity;
    }

    @Override
    @Transactional
    public TelegramChat remove(TelegramChat entity) {
        return jdbcTemplate.queryForObject(DELETE_TELEGRAM_CHAT.getQuery(), new BeanPropertyRowMapper<>(TelegramChat.class), entity.getId());
    }


    @Override
    @Transactional(readOnly = true)
    public Collection<TelegramChat> findAll() {
        return jdbcTemplate.query(SELECT_ALL_TELEGRAM_CHAT.getQuery(), new BeanPropertyRowMapper<>(TelegramChat.class));
    }
}
