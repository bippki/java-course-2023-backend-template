package edu.java.repository.jooq;

import edu.java.entity.TelegramChat;
import edu.java.repository.EntityRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.Tables.TELEGRAM_CHAT;

@RequiredArgsConstructor
public class JooqTelegramChatRepository implements EntityRepository<TelegramChat> {
    private final DSLContext dslContext;
    @Override
    public TelegramChat add(TelegramChat entity) {
        return dslContext.insertInto(TELEGRAM_CHAT, TELEGRAM_CHAT.ID, TELEGRAM_CHAT.REGISTERED_AT)
            .values(entity.getId(), entity.getRegisteredAt())
            .returning(TELEGRAM_CHAT.fields())
            .fetchOneInto(TelegramChat.class);
    }
    @Override
    public TelegramChat remove(TelegramChat entity) {
        return dslContext.deleteFrom(TELEGRAM_CHAT)
            .where(TELEGRAM_CHAT.ID.eq(entity.getId()))
            .returning(TELEGRAM_CHAT.fields())
            .fetchOneInto(TelegramChat.class);
    }
    @Override
    public Collection<TelegramChat> findAll() {
        return dslContext.select(TELEGRAM_CHAT.fields())
            .from(TELEGRAM_CHAT)
            .fetchInto(TelegramChat.class);
    }
}
