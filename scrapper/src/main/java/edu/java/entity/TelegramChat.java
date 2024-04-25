package edu.java.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "telegram_chat")
public class TelegramChat {
    @Id
    private Long id;
    @Column(name = "registered_at")
    private OffsetDateTime registeredAt;

    @ManyToMany(mappedBy = "telegramChats")
    private Set<Link> links = new HashSet<>();
}
