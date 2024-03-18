package edu.java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;

public class LinkNotSupportedException extends ResponseStatusException {
    public LinkNotSupportedException(URI url) {
        super(HttpStatus.NOT_ACCEPTABLE, "Link %s is not supported".formatted(url.toString()));
    }
}
