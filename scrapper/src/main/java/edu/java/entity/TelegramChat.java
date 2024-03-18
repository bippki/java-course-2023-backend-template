package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelegramChat {
    private Long id;
    private List<Link> links;
    private OffsetDateTime registeredAt;
}
