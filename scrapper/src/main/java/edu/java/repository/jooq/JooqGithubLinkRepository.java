package edu.java.repository.jooq;

import edu.java.entity.GithubLink;
import edu.java.entity.Link;
import edu.java.repository.SpecificLinkRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.Tables.GITHUB_LINK;

@RequiredArgsConstructor
public class JooqGithubLinkRepository implements SpecificLinkRepository<GithubLink> {
    private final DSLContext dslContext;
    @Override
    public GithubLink getLink(Link link) {
        return dslContext.select(GITHUB_LINK.fields())
            .from(GITHUB_LINK)
            .where(GITHUB_LINK.ID.eq(link.getId()))
            .fetchOneInto(GithubLink.class);
    }
    @Override
    public GithubLink addLink(GithubLink link) {
        return dslContext.insertInto(GITHUB_LINK, GITHUB_LINK.ID, GITHUB_LINK.DEFAULT_BRANCH, GITHUB_LINK.FORKS_COUNT)
            .values(link.getId(), link.getDefaultBranch(), link.getForksCount())
            .returning(GITHUB_LINK.fields())
            .fetchOneInto(GithubLink.class);
    }
    @Override
    public void updateLink(GithubLink link) {
        dslContext.update(GITHUB_LINK)
            .set(GITHUB_LINK.DEFAULT_BRANCH, link.getDefaultBranch())
            .set(GITHUB_LINK.FORKS_COUNT, link.getForksCount())
            .where(GITHUB_LINK.ID.eq(link.getId()))
            .execute();
    }
}
