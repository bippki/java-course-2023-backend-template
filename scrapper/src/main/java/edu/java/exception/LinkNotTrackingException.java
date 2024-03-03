package edu.java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;

public class LinkNotTrackingException extends ResponseStatusException {
    public LinkNotTrackingException(Long tgChatId, URI url) {
        super(HttpStatus.BAD_REQUEST, "Link %s is not tracking by id %d".formatted(url.toString(), tgChatId));
    }
}
