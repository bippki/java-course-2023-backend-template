package edu.java.service;

import edu.java.entity.GithubLink;
import edu.java.entity.Link;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface GithubLinkService {
    @Transactional(readOnly = true)
    Optional<GithubLink> getLink(Link link);

    @Transactional
    GithubLink addLink(GithubLink link);

    @Transactional
    void updateLink(GithubLink link);
}
