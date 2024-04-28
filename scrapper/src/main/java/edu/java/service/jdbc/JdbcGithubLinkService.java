package edu.java.service.jdbc;

import edu.java.repository.jdbc.JdbcGithubLinkRepository;
import edu.java.entity.GithubLink;
import edu.java.entity.Link;
import edu.java.service.GithubLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.Optional;
@RequiredArgsConstructor
public class JdbcGithubLinkService implements GithubLinkService {
    private final JdbcGithubLinkRepository jdbcGithubLinkRepository;
    @Override
    public Optional<GithubLink> getLink(Link link) {
        try {
            return Optional.of(jdbcGithubLinkRepository.getLink(link));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
    @Override
    public GithubLink addLink(GithubLink link) {
        return jdbcGithubLinkRepository.addLink(link);
    }
    @Override
    public void updateLink(GithubLink link) {
        jdbcGithubLinkRepository.updateLink(link);
    }
}
