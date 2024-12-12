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

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("addresses")
public class AddressEntity implements Persistable<String> {
    @Id
    private String id;
    @Column("address")
    private String address;
    @Column("zip_code")
    private String zipCode;
    @Column("city")
    private String city;
    @Column("state")
    private String state;
    @Column("country_id")
    private int countryId;

    @Transient
    private CountryEntity countryEntity;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
