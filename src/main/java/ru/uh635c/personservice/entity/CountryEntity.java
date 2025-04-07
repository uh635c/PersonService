package ru.uh635c.personservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("countries")
public class CountryEntity {
    @Id
    private int id;
    @Column("name")
    private String name;
    @Column("alpha2")
    private String alpha2;
    @Column("alpha3")
    private String alpha3;
}
