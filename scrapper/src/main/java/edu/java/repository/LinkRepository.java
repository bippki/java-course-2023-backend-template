package edu.java.repository;

import edu.java.entity.Link;
import org.springframework.stereotype.Repository;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class LinkRepository implements ILinkRepository {
    private final Map<Long, Link> repository = new HashMap<>();

    @Override
    public Optional<Link> findByUrl(URI url) {
        return repository.values().stream()
            .filter(linkUrl -> linkUrl.getUrl().equals(url))
            .findFirst();
    }

    @Override
    public Link save(Link link) {
        repository.put(link.getId(), link);

        return link;
    }
}
