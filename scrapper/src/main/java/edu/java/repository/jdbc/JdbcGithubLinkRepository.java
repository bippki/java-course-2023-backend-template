package edu.java.repository.jdbc;

import edu.java.repository.SpecificLinkRepository;
import edu.java.entity.GithubLink;
import edu.java.entity.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class JdbcGithubLinkRepository implements SpecificLinkRepository<GithubLink> {

    private final JdbcTemplate jdbcTemplate;

    private enum Query {
        SELECT_LINK_BY_ID("SELECT * FROM github_link WHERE id=?"),
        UPDATE_LINK("UPDATE github_link SET default_branch=?, forks_count=? WHERE id=?"),
        ADD_LINK("INSERT INTO github_link (id, default_branch, forks_count) VALUES (?,?,?) RETURNING *");

        private final String sql;

        Query(String sql) {
            this.sql = sql;
        }

        public String getSql() {
            return sql;
        }
    }

    @Override
    public GithubLink getLink(Link link) {
        return jdbcTemplate.queryForObject(
            Query.SELECT_LINK_BY_ID.getSql(),
            new BeanPropertyRowMapper<>(GithubLink.class),
            link.getId()
        );
    }

    @Override
    public GithubLink addLink(GithubLink link) {
        return jdbcTemplate.queryForObject(
            Query.ADD_LINK.getSql(),
            new BeanPropertyRowMapper<>(GithubLink.class),
            link.getId(),
            link.getDefaultBranch(),
            link.getForksCount()
        );
    }

    @Override
    public void updateLink(GithubLink link) {
        jdbcTemplate.update(
            Query.UPDATE_LINK.getSql(),
            link.getDefaultBranch(),
            link.getForksCount(),
            link.getId()
        );
    }
}
