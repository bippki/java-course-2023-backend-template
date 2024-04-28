package edu.java.service.jooq;

import edu.java.entity.Link;
import edu.java.entity.dto.bot.LinkResponse;
import edu.java.entity.dto.bot.ListLinksResponse;
import edu.java.exception.LinkAlreadyTrackingException;
import edu.java.exception.LinkNotSupportedException;
import edu.java.exception.LinkNotTrackingException;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.service.LinkService;
import edu.java.util.LinkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    private final JooqLinkRepository jooqLinkRepository;
    private final LinkUtil linkUtil;
    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        Link link;
        if (!linkUtil.isUrlSupported(url)) {
            throw new LinkNotSupportedException(url);
        }
        try {
            link = jooqLinkRepository.add(new Link()
                .setUrl(url)
                .setLastUpdatedAt(OffsetDateTime.now()));
        } catch (DuplicateKeyException ignored) {
            link = jooqLinkRepository.findByUrl(url);
        }
        try {
            jooqLinkRepository.connectChatToLink(tgChatId, link.getId());
        } catch (DuplicateKeyException ignored) {
            throw new LinkAlreadyTrackingException(tgChatId, url);
        }
        return new LinkResponse(link.getId(), link.getUrl());
    }
    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        Link link = jooqLinkRepository.findByUrl(url);
        if (link == null) {
            throw new LinkNotTrackingException(tgChatId, url);
        }
        jooqLinkRepository.removeChatToLink(tgChatId, link.getId());
        return new LinkResponse(link.getId(), link.getUrl());
    }
    @Override
    public Collection<Link> listAllWithInterval(Duration interval) {
        return jooqLinkRepository.findAllWithInterval(interval);
    }
    @Override
    public ListLinksResponse listAllForChat(Long tgChatId) {
        Collection<Link> links = jooqLinkRepository.findAllForChat(tgChatId);
        return new ListLinksResponse(links.stream()
            .map(link -> new LinkResponse(link.getId(), link.getUrl()))
            .toList(), links.size());
    }
    @Override
    public List<Long> getAllChatsForLink(Long linkId) {
        return jooqLinkRepository.findAllChatsForLink(linkId);
    }
    @Override
    public void updateLink(Link link) {
        jooqLinkRepository.updateLink(link);
    }
}
