package ru.uh635c.personservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;


import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserEntity implements Persistable<String> {
    @Id
    private String id;
    @Column("secret_key")
    private String secretKey;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column("created_at")
    private LocalDateTime createAt;
    @Column("updated_at")
    private LocalDateTime updateAt;
    @Column("verified_at")
    private LocalDateTime verifiedAt;
    @Column("archived_at")
    private LocalDateTime archivedAt;
    @Column("filled")
    private boolean filled;
    @Column("address_id")
    private String addressId;

    @Transient
    private AddressEntity addressEntity;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
