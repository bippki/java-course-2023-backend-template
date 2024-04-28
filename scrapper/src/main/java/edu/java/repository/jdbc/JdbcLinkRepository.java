package edu.java.repository.jdbc;

import edu.java.entity.Link;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcLinkRepository implements JdbcRepository<Link> {
    private enum Query {
        ADD_LINK("INSERT INTO link (url, last_updated_at) VALUES (?, ?) RETURNING *"),
        DELETE_LINK("DELETE FROM link WHERE id=? RETURNING *"),
        SELECT_ALL_LINK("SELECT * FROM link"),
        SELECT_ALL_WITH_INTERVAL("SELECT * FROM link WHERE last_updated_at < ?"),
        SELECT_BY_URL("SELECT * FROM link WHERE url=?"),
        ADD_CHAT_TO_LINK("INSERT INTO assignment (chat_id, link_id) VALUES (?, ?)"),
        DELETE_CHAT_TO_LINK("DELETE FROM assignment WHERE chat_id=? AND link_id=?"),
        SELECT_ALL_FOR_CHAT("SELECT * FROM link INNER JOIN assignment a on link.id = a.link_id WHERE a.chat_id=?"),
        SELECT_ALL_CHATS_FOR_LINK("SELECT chat_id FROM assignment WHERE link_id=?"),
        UPDATE_LINK("UPDATE link SET last_updated_at=? WHERE id=?");
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
    public Link add(Link entity) {
        return jdbcTemplate.queryForObject(
            Query.ADD_LINK.getSql(),
            new BeanPropertyRowMapper<>(Link.class),
            entity.getUrl().toString(),
            entity.getLastUpdatedAt()
        );
    }

    @Override
    @Transactional
    public Link remove(Link entity) {
        return jdbcTemplate.queryForObject(Query.DELETE_LINK.getSql(), new BeanPropertyRowMapper<>(Link.class), entity.getId());
    }

    @Override
    @Transactional
    public Collection<Link> findAll() {
        return jdbcTemplate.query(Query.SELECT_ALL_LINK.getSql(), new BeanPropertyRowMapper<>(Link.class));
    }

    @Transactional
    public Collection<Link> findAllWithInterval(Duration interval) {
        return jdbcTemplate.query(
            Query.SELECT_ALL_WITH_INTERVAL.getSql(),
            new BeanPropertyRowMapper<>(Link.class),
            Timestamp.from(OffsetDateTime.now().minusSeconds(interval.getSeconds()).toInstant())
        );
    }

    @Transactional
    public Link findByUrl(URI url) {
        return jdbcTemplate.queryForObject(
            Query.SELECT_BY_URL.getSql(),
            new BeanPropertyRowMapper<>(Link.class),
            url.toString()
        );
    }

    @Transactional
    public void connectChatToLink(Long chatId, Long linkId) {
        jdbcTemplate.update(Query.ADD_CHAT_TO_LINK.getSql(), chatId, linkId);
    }

    @Transactional
    public void removeChatToLink(Long chatId, Long linkId) {
        jdbcTemplate.update(Query.DELETE_CHAT_TO_LINK.getSql(), chatId, linkId);
    }

    @Transactional
    public Collection<Link> findAllForChat(Long chatId) {
        return jdbcTemplate.query(Query.SELECT_ALL_FOR_CHAT.getSql(), new BeanPropertyRowMapper<>(Link.class), chatId);
    }

    @Transactional
    public List<Long> findAllChatsForLink(Long linkId) {
        return jdbcTemplate.query(
            Query.SELECT_ALL_CHATS_FOR_LINK.getSql(),
            (rs, rowNum) -> rs.getLong("chat_id"),
            linkId
        );
    }

    @Transactional
    public void updateLink(Link link) {
        jdbcTemplate.update(Query.UPDATE_LINK.getSql(), link.getLastUpdatedAt(), link.getId());
    }
}
