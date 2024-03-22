package edu.java.controller;

import edu.java.entity.dto.bot.AddLinkRequest;
import edu.java.entity.dto.bot.LinkResponse;
import edu.java.entity.dto.bot.ListLinksResponse;
import edu.java.entity.dto.bot.RemoveLinkRequest;
import edu.java.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;

    @GetMapping
    public ListLinksResponse getAllLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        return linkService.listAllForChat(tgChatId);
    }

    @PostMapping
    public LinkResponse addLink(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody AddLinkRequest request) {
        return linkService.add(tgChatId, request.link());
    }

    @DeleteMapping
    public LinkResponse removeLink(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody RemoveLinkRequest request) {
        return linkService.remove(tgChatId, request.link());
    }
}
