package edu.java.service;

import edu.java.entity.Link;
import edu.java.entity.dto.bot.LinkResponse;
import edu.java.entity.dto.bot.ListLinksResponse;
import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

public interface LinkService {
    LinkResponse add(Long tgChatId, URI url);

    LinkResponse remove(Long tgChatId, URI url);

    Collection<Link> listAllWithInterval(Duration interval);

    ListLinksResponse listAllForChat(Long tgChatId);

    List<Long> getAllChatsForLink(Long linkId);

    void updateLink(Link link);
}
