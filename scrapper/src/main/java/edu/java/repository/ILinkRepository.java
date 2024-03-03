package edu.java.repository;

import edu.java.entity.Link;
import java.net.URI;
import java.util.Optional;

public interface ILinkRepository {

    Optional<Link> findByUrl(URI url);

    Link save(Link link);
}
