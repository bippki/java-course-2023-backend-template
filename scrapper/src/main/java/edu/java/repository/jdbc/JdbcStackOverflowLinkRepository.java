package edu.java.repository.jdbc;

import edu.java.entity.Link;
import edu.java.entity.StackOverflowLink;
import edu.java.repository.SpecificLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class JdbcStackOverflowLinkRepository implements SpecificLinkRepository<StackOverflowLink> {
    private enum Query {
        SELECT_LINK_BY_ID("SELECT * FROM stackoverflow_link WHERE id=?"),
        UPDATE_LINK("UPDATE stackoverflow_link SET answer_count=?, score=? WHERE id=?"),
        ADD_LINK("INSERT INTO stackoverflow_link (id, answer_count, score) VALUES (?,?,?) RETURNING *");

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
    public StackOverflowLink getLink(Link link) {
        return jdbcTemplate.queryForObject(
            Query.SELECT_LINK_BY_ID.getSql(),
            new BeanPropertyRowMapper<>(StackOverflowLink.class),
            link.getId()
        );
    }
    @Override
    public StackOverflowLink addLink(StackOverflowLink link) {
        return jdbcTemplate.queryForObject(
            Query.ADD_LINK.getSql(),
            new BeanPropertyRowMapper<>(StackOverflowLink.class),
            link.getId(),
            link.getAnswerCount(),
            link.getScore()
        );
    }
    @Override
    public void updateLink(StackOverflowLink link) {
        jdbcTemplate.update(Query.UPDATE_LINK.getSql(), link.getAnswerCount(), link.getScore(), link.getId());
    }
}

