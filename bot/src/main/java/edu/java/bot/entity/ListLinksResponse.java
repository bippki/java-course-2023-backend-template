package edu.java.bot.entity;


import java.util.List;

public record ListLinksResponse(
    List<LinkResponse> links,
    Integer size
) {
}
