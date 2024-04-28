package edu.java.repository.jooq;

import edu.java.entity.Link;
import edu.java.repository.EntityRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import static edu.java.domain.jooq.Tables.LINK;
import static edu.java.domain.jooq.Tables.ASSIGNMENT;

@RequiredArgsConstructor
public class JooqLinkRepository implements EntityRepository<Link> {
    private final DSLContext dslContext;

    @Override
    public Link add(Link entity) {
        return dslContext.insertInto(LINK, LINK.URL, LINK.LAST_UPDATED_AT)
            .values(entity.getUrl().toString(), entity.getLastUpdatedAt())
            .returning(LINK.fields())
            .fetchOneInto(Link.class);
    }
    @Override
    public Link remove(Link entity) {
        return dslContext.deleteFrom(LINK)
            .where(LINK.ID.eq(entity.getId()))
            .returning(LINK.fields())
            .fetchOneInto(Link.class);
    }
    @Override
    public Collection<Link> findAll() {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .fetchInto(Link.class);
    }
    public Collection<Link> findAllWithInterval(Duration interval) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .where(LINK.LAST_UPDATED_AT.lt(OffsetDateTime.now().minus(interval)))
            .fetchInto(Link.class);
    }
    public Link findByUrl(URI url) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .where(LINK.URL.eq(url.toString()))
            .fetchOneInto(Link.class);
    }
    public void connectChatToLink(Long chatId, Long linkId) {
        dslContext.insertInto(ASSIGNMENT, ASSIGNMENT.CHAT_ID, ASSIGNMENT.LINK_ID)
            .values(chatId, linkId)
            .execute();
    }
    public void removeChatToLink(Long chatId, Long linkId) {
        dslContext.deleteFrom(ASSIGNMENT)
            .where(ASSIGNMENT.CHAT_ID.eq(chatId).and(ASSIGNMENT.LINK_ID.eq(linkId)))
            .execute();
    }
    public Collection<Link> findAllForChat(Long chatId) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .innerJoin(ASSIGNMENT).on(LINK.ID.eq(ASSIGNMENT.LINK_ID))
            .where(ASSIGNMENT.CHAT_ID.eq(chatId))
            .fetchInto(Link.class);
    }
    public List<Long> findAllChatsForLink(Long linkId) {
        return dslContext.select(ASSIGNMENT.CHAT_ID)
            .from(ASSIGNMENT)
            .where(ASSIGNMENT.LINK_ID.eq(linkId))
            .fetchInto(Long.class);
    }
    public void updateLink(Link link) {
        dslContext.update(LINK)
            .set(LINK.LAST_UPDATED_AT, link.getLastUpdatedAt())
            .where(LINK.ID.eq(link.getId()))
            .execute();
    }
}
