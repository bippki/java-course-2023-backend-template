package edu.java.repository;

import edu.java.entity.Link;

public interface SpecificLinkRepository<L extends Link> {
    L getLink(Link link);
    L addLink(L link);
    void updateLink(L link);
}
