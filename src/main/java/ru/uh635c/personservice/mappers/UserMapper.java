package ru.uh635c.personservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.uh635c.dto.IndividualRequestDTO;
import ru.uh635c.personservice.entity.UserEntity;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface UserMapper {


    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "archivedAt", ignore = true)
    @Mapping(target = "filled", ignore = true)
    @Mapping(target = "addressId", ignore = true)
    @Mapping(target = "addressEntity", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserEntity mapUser(IndividualRequestDTO requestDTO);
}
