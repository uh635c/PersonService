package ru.uh635c.personservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.uh635c.dto.IndividualRequestDTO;
import ru.uh635c.personservice.entity.AddressEntity;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "countryId", ignore = true)
    @Mapping(target = "countryEntity", ignore = true)
    @Mapping(target = "id", ignore = true)
    AddressEntity mapAddress(IndividualRequestDTO requestDTO);
}
