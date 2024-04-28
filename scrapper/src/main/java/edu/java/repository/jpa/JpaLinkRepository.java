package edu.java.repository.jpa;

import edu.java.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUrl(URI url);

    List<Link> findAllByLastUpdatedAtBefore(OffsetDateTime time);

    @Query("SELECT l FROM Link l JOIN l.telegramChats c WHERE c.id = :chatId")
    List<Link> findAllForChat(@Param("chatId") Long chatId);

    @Query("SELECT c.id FROM Link l JOIN l.telegramChats c WHERE l.id = :linkId")
    List<Long> findAllChatIdsByLinkId(@Param("linkId") Long linkId);
}
