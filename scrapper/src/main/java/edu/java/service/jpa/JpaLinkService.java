package edu.java.service.jpa;

import edu.java.entity.Link;
import edu.java.entity.TelegramChat;
import edu.java.entity.dto.bot.LinkResponse;
import edu.java.entity.dto.bot.ListLinksResponse;
import edu.java.exception.LinkAlreadyTrackingException;
import edu.java.exception.LinkNotSupportedException;
import edu.java.exception.LinkNotTrackingException;
import edu.java.exception.TelegramChatNotExistsException;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.JpaTelegramChatRepository;
import edu.java.service.LinkService;
import edu.java.util.LinkUtil;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaTelegramChatRepository jpaTelegramChatRepository;
    private final LinkUtil linkUtil;

    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        if (!linkUtil.isUrlSupported(url)) {
            throw new LinkNotSupportedException(url);
        }

        Optional<Link> byUrl = jpaLinkRepository.findByUrl(url);
        Link link = byUrl.orElseGet(() -> jpaLinkRepository.save(new Link()
            .setUrl(url)
            .setLastUpdatedAt(OffsetDateTime.now())));
        TelegramChat currentChat = jpaTelegramChatRepository.findById(tgChatId)
            .orElseThrow(() -> new TelegramChatNotExistsException(tgChatId));

        if (link.getTelegramChats().contains(currentChat)) {
            throw new LinkAlreadyTrackingException(tgChatId, url);
        }

        link.addTelegramChat(currentChat);
        Link savedEntity = jpaLinkRepository.save(link);

        return new LinkResponse(savedEntity.getId(), savedEntity.getUrl());
    }

    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        Link link = jpaLinkRepository.findByUrl(url)
            .orElseThrow(() -> new LinkNotTrackingException(tgChatId, url));
        TelegramChat currentChat = jpaTelegramChatRepository.findById(tgChatId)
            .orElseThrow(() -> new TelegramChatNotExistsException(tgChatId));

        if (!link.getTelegramChats().contains(currentChat)) {
            throw new LinkNotTrackingException(tgChatId, url);
        }

        link.removeTelegramChat(currentChat);
        Link savedEntity = jpaLinkRepository.save(link);

        return new LinkResponse(savedEntity.getId(), savedEntity.getUrl());
    }

    @Override
    public Collection<Link> listAllWithInterval(Duration interval) {
        return jpaLinkRepository.findAllByLastUpdatedAtBefore(OffsetDateTime.now().minus(interval));
    }

    @Override
    public ListLinksResponse listAllForChat(Long tgChatId) {
        List<Link> links = jpaLinkRepository.findAllForChat(tgChatId);

        return new ListLinksResponse(links.stream()
            .map(link -> new LinkResponse(link.getId(), link.getUrl()))
            .toList(), links.size());
    }

    @Override
    public List<Long> getAllChatsForLink(Long linkId) {
        return jpaLinkRepository.findAllChatIdsByLinkId(linkId);
    }

    @Override
    public void updateLink(Link link) {
        jpaLinkRepository.save(link);
    }
}
