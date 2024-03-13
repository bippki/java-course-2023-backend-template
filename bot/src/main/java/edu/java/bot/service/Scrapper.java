package edu.java.bot.service;

import edu.java.bot.entity.AddLinkRequest;
import edu.java.bot.entity.LinkResponse;
import edu.java.bot.entity.ListLinksResponse;
import edu.java.bot.entity.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface Scrapper {
    Mono<ResponseEntity<Void>> registerChat(Long tgChatId);
    Mono<ResponseEntity<Void>> deleteChat(Long tgChatId);
    Mono<ResponseEntity<ListLinksResponse>> getAllLinksForChat(Long tgChatId);
    Mono<ResponseEntity<LinkResponse>> addLink(Long tgChatId, AddLinkRequest request);
    Mono<ResponseEntity<LinkResponse>> removeLink(Long tgChatId, RemoveLinkRequest request);
}
