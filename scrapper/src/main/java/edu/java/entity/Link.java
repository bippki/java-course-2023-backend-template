package edu.java.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString(exclude = "telegramChats")
@EqualsAndHashCode(exclude = "telegramChats")
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "link")
@Inheritance(strategy = InheritanceType.JOINED)
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url")
    private URI url;
    @Column(name = "last_updated_at")
    private OffsetDateTime lastUpdatedAt;

    @ManyToMany
    @JoinTable(
        name = "assignment",
        joinColumns = @JoinColumn(name = "link_id"),
        inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private Set<TelegramChat> telegramChats = new HashSet<>();

    public void addTelegramChat(TelegramChat chat) {
        telegramChats.add(chat);
        chat.getLinks().add(this);
    }

    public void removeTelegramChat(TelegramChat chat) {
        telegramChats.remove(chat);
        chat.getLinks().remove(this);
    }
}
