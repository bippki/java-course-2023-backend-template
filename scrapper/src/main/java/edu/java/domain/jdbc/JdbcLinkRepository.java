package edu.java.domain.jdbc;

import edu.java.entity.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import static edu.java.domain.jdbc.JdbcQuery.ADD_CHAT_TO_LINK;
import static edu.java.domain.jdbc.JdbcQuery.ADD_LINK;
import static edu.java.domain.jdbc.JdbcQuery.DELETE_CHAT_TO_LINK;
import static edu.java.domain.jdbc.JdbcQuery.DELETE_LINK;
import static edu.java.domain.jdbc.JdbcQuery.SELECT_ALL_CHATS_FOR_LINK;
import static edu.java.domain.jdbc.JdbcQuery.SELECT_ALL_FOR_CHAT;
import static edu.java.domain.jdbc.JdbcQuery.SELECT_ALL_LINK;
import static edu.java.domain.jdbc.JdbcQuery.SELECT_ALL_WITH_INTERVAL;
import static edu.java.domain.jdbc.JdbcQuery.SELECT_BY_URL;
import static edu.java.domain.jdbc.JdbcQuery.UPDATE_LINK;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements JdbcRepository<Link> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public Link add(Link entity) {
        return jdbcTemplate.queryForObject(
            ADD_LINK.getQuery(),
            new BeanPropertyRowMapper<>(Link.class),
            entity.getUrl().toString(),
            entity.getLastUpdatedAt()
        );
    }

    @Override
    @Transactional
    public Link remove(Link entity) {
        return jdbcTemplate.queryForObject(DELETE_LINK.getQuery(), new BeanPropertyRowMapper<>(Link.class), entity.getId());
    }

    @Override
    @Transactional
    public Collection<Link> findAll() {
        return jdbcTemplate.query(SELECT_ALL_LINK.getQuery(), new BeanPropertyRowMapper<>(Link.class));
    }

    @Transactional
    public Collection<Link> findAllWithInterval(Duration interval) {
        return jdbcTemplate.query(
            SELECT_ALL_WITH_INTERVAL.getQuery(),
            new BeanPropertyRowMapper<>(Link.class),
            Timestamp.from(OffsetDateTime.now().minusSeconds(interval.getSeconds()).toInstant())
        );
    }

    @Transactional
    public Link findByUrl(URI url) {
        return jdbcTemplate.queryForObject(
            SELECT_BY_URL.getQuery(),
            new BeanPropertyRowMapper<>(Link.class),
            url.toString()
        );
    }

    @Transactional
    public void connectChatToLink(Long chatId, Long linkId) {
        jdbcTemplate.update(ADD_CHAT_TO_LINK.getQuery(), chatId, linkId);
    }

    @Transactional
    public void removeChatToLink(Long chatId, Long linkId) {
        jdbcTemplate.update(DELETE_CHAT_TO_LINK.getQuery(), chatId, linkId);
    }

    @Transactional
    public Collection<Link> findAllForChat(Long chatId) {
        return jdbcTemplate.query(SELECT_ALL_FOR_CHAT.getQuery(), new BeanPropertyRowMapper<>(Link.class), chatId);
    }

    @Transactional
    public List<Long> findAllChatsForLink(Long linkId) {
        return jdbcTemplate.query(
            SELECT_ALL_CHATS_FOR_LINK.getQuery(),
            (rs, rowNum) -> rs.getLong("chat_id"),
            linkId
        );
    }

    @Transactional
    public void updateLink(Link link) {
        jdbcTemplate.update(UPDATE_LINK.getQuery(), link.getLastUpdatedAt(), link.getId());
    }
}
