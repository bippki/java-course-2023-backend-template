package edu.java.bot.controller;

import edu.java.bot.service.LinkUpdateService;
import lombok.RequiredArgsConstructor;
import entity.dto.LinkUpdateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class UpdateController {
    private final LinkUpdateService service;

    @PostMapping
    public void postUpdates(@RequestBody LinkUpdateRequest request) {
        service.sendUpdateNotification(request);
    }
}
