package edu.java.service.jdbc;

import edu.java.entity.Link;
import edu.java.entity.StackOverflowLink;
import edu.java.repository.jdbc.JdbcStackOverflowLinkRepository;
import edu.java.service.StackOverflowLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcStackOverflowLinkService implements StackOverflowLinkService {
    private final JdbcStackOverflowLinkRepository jdbcStackOverflowLinkRepository;
    @Override
    public Optional<StackOverflowLink> getLink(Link link) {
        try {
            return Optional.of(jdbcStackOverflowLinkRepository.getLink(link));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
    @Override
    public StackOverflowLink addLink(StackOverflowLink link) {
        return jdbcStackOverflowLinkRepository.addLink(link);
    }
    @Override
    public void updateLink(StackOverflowLink link) {
        jdbcStackOverflowLinkRepository.updateLink(link);
    }
}
