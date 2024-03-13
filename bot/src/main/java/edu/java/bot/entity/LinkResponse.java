package edu.java.bot.entity;


import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
