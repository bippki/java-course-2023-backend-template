package edu.java.bot.service;

import entity.dto.AddLinkRequest;
import entity.dto.LinkResponse;
import entity.dto.ListLinksResponse;
import entity.dto.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface Scrapper {
    Mono<ResponseEntity<Void>> registerChat(Long tgChatId);
    Mono<ResponseEntity<Void>> deleteChat(Long tgChatId);
    Mono<ResponseEntity<ListLinksResponse>> getAllLinksForChat(Long tgChatId);
    Mono<ResponseEntity<LinkResponse>> addLink(Long tgChatId, AddLinkRequest request);
    Mono<ResponseEntity<LinkResponse>> removeLink(Long tgChatId, RemoveLinkRequest request);
}
