package edu.java.service.jdbc;


import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.entity.Link;
import edu.java.entity.dto.bot.LinkResponse;
import edu.java.entity.dto.bot.ListLinksResponse;
import edu.java.exception.LinkAlreadyTrackingException;
import edu.java.exception.LinkNotSupportedException;
import edu.java.exception.LinkNotTrackingException;
import edu.java.exception.TelegramChatNotExistsException;
import edu.java.service.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import edu.java.util.LinkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;
    private final LinkUtil linkUtil;

    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        Link link;

        if (!linkUtil.isUrlSupported(url)) {
            throw new LinkNotSupportedException(url);
        }

        try {
            link = linkRepository.add(new Link()
                .setUrl(url)
                .setLastUpdatedAt(OffsetDateTime.now()));
        } catch (DuplicateKeyException ignored) {
            link = linkRepository.findByUrl(url);
        }

        try {
            linkRepository.connectChatToLink(tgChatId, link.getId());
        } catch (DuplicateKeyException ignored) {
            throw new LinkAlreadyTrackingException(tgChatId, url);
        } catch (DataIntegrityViolationException ignored) {
            throw new TelegramChatNotExistsException(tgChatId);
        }

        return new LinkResponse(link.getId(), link.getUrl());
    }

    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        try {
            Link link = linkRepository.findByUrl(url);

            linkRepository.removeChatToLink(tgChatId, link.getId());

            return new LinkResponse(link.getId(), link.getUrl());
        } catch (EmptyResultDataAccessException ignored) {
            throw new LinkNotTrackingException(tgChatId, url);
        }
    }

    @Override
    public Collection<Link> listAllWithInterval(Duration interval) {
        return linkRepository.findAllWithInterval(interval);
    }

    @Override
    public ListLinksResponse listAllForChat(Long tgChatId) {
        Collection<Link> links = linkRepository.findAllForChat(tgChatId);

        return new ListLinksResponse(links.stream()
            .map(link -> new LinkResponse(link.getId(), link.getUrl()))
            .toList(), links.size());
    }

    @Override
    public List<Long> getAllChatsForLink(Long linkId) {
        return linkRepository.findAllChatsForLink(linkId);
    }

    @Override
    public void updateLink(Link link) {
        linkRepository.updateLink(link);
    }
}
