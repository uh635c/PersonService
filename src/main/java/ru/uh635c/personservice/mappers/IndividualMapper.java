package ru.uh635c.personservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.uh635c.dto.IndividualResponseDTO;
import ru.uh635c.personservice.entity.IndividualEntity;
import ru.uh635c.entity.Status;

//@Mapper(componentModel = "spring", imports = Status.class)
public interface IndividualMapper {

//
//    IndividualResponseDTO map(IndividualEntity entity);
//
//    @Mapping(target = "passportNumber", source = "passportNumber")
//    @Mapping(target = "phoneNumber", source = "phoneNumber")
//    @Mapping(target = "email", source = "email")
//    @Mapping(target = "status", expression = "java(Status.ACTIVE)")
//    @Mapping(target = "userId", ignore = true)
//    @Mapping(target = "userEntity", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    IndividualEntity mapIndividualEntity(IndividualResponseDTO responseDTO);
}