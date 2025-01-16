package ru.uh635c.personservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.uh635c.entity.Status;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("individuals")
public class IndividualEntity {
    @Id
    private String id;
    @Column("passport_number")
    private String passportNumber;
    @Column("phone_number")
    private String phoneNumber;
    @Column("email")
    private String email;
    @Column("status")
    private Status status;
    @Column("user_id")
    private String userId;

    @Transient
    private UserEntity userEntity;
}
