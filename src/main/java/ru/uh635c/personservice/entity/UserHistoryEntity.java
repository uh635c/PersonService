package ru.uh635c.personservice.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("user_histories")
public class UserHistoryEntity {
    @Id
    private String id;
    @Column("created")
    private LocalDateTime created;
    @Column("user_type")
    private UserType user_type;
    @Column("reason")
    private String reason;//by system
    @Column("comment")
    private String comment;//by system
    @Column("changed_values")
    private String changedValues;
    @Column("user_id")
    private String userId;
}
