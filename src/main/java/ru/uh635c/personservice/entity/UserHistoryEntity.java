package ru.uh635c.personservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("user_history")
public class UserHistoryEntity {
    private String id;
    private LocalDateTime created;
    private String user_type;
    private String reason;
    private String comment;
    private String changedValue;
    private String userId;
}
