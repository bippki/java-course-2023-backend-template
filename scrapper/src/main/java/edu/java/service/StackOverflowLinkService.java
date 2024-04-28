package edu.java.service;

import edu.java.entity.Link;
import edu.java.entity.StackOverflowLink;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface StackOverflowLinkService {
    @Transactional(readOnly = true)
    Optional<StackOverflowLink> getLink(Link link);

    @Transactional
    StackOverflowLink addLink(StackOverflowLink link);

    @Transactional
    void updateLink(StackOverflowLink link);
}
