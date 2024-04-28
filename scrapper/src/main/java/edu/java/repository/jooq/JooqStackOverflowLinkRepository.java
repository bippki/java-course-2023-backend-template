package edu.java.repository.jooq;

import edu.java.entity.Link;
import edu.java.entity.StackOverflowLink;
import edu.java.repository.SpecificLinkRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.Tables.STACKOVERFLOW_LINK;

@RequiredArgsConstructor
public class JooqStackOverflowLinkRepository implements SpecificLinkRepository<StackOverflowLink> {
    private final DSLContext dslContext;
    @Override
    public StackOverflowLink getLink(Link link) {
        return dslContext.select(STACKOVERFLOW_LINK.fields())
            .from(STACKOVERFLOW_LINK)
            .where(STACKOVERFLOW_LINK.ID.eq(link.getId()))
            .fetchOneInto(StackOverflowLink.class);
    }
    @Override
    public StackOverflowLink addLink(StackOverflowLink link) {
        return dslContext.insertInto(
                STACKOVERFLOW_LINK,
                STACKOVERFLOW_LINK.ID,
                STACKOVERFLOW_LINK.ANSWER_COUNT,
                STACKOVERFLOW_LINK.SCORE
            )
            .values(link.getId(), link.getAnswerCount(), link.getScore())
            .returning(STACKOVERFLOW_LINK.fields())
            .fetchOneInto(StackOverflowLink.class);
    }
    @Override
    public void updateLink(StackOverflowLink link) {
        dslContext.update(STACKOVERFLOW_LINK)
            .set(STACKOVERFLOW_LINK.ANSWER_COUNT, link.getAnswerCount())
            .set(STACKOVERFLOW_LINK.SCORE, link.getScore())
            .execute();
    }
}
