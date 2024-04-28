package edu.java.service.jooq;

import edu.java.entity.Link;
import edu.java.entity.StackOverflowLink;
import edu.java.repository.jooq.JooqStackOverflowLinkRepository;
import edu.java.service.StackOverflowLinkService;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@RequiredArgsConstructor
public class JooqStackOverflowLinkService implements StackOverflowLinkService {
    private final JooqStackOverflowLinkRepository jooqStackOverflowLinkRepository;
    @Override
    public Optional<StackOverflowLink> getLink(Link link) {
        StackOverflowLink stackOverflowLink = jooqStackOverflowLinkRepository.getLink(link);
        return stackOverflowLink != null ? Optional.of(stackOverflowLink) : Optional.empty();
    }
    @Override
    public StackOverflowLink addLink(StackOverflowLink link) {
        return jooqStackOverflowLinkRepository.addLink(link);
    }
    @Override
    public void updateLink(StackOverflowLink link) {
        jooqStackOverflowLinkRepository.updateLink(link);
    }
}
