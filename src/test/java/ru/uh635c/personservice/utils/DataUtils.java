package ru.uh635c.personservice.utils;

import ru.uh635c.dto.IndividualRequestDTO;
import ru.uh635c.dto.IndividualResponseDTO;
import ru.uh635c.entity.Status;
import ru.uh635c.personservice.entity.*;

import java.time.LocalDateTime;

public class DataUtils {

    public static IndividualEntity individualEntityPersisted() {
        return IndividualEntity.builder()
                .id("individualId")
                .passportNumber("passportNumber")
                .phoneNumber("phoneNumber")
                .email("email")
                .status(Status.ACTIVE)
                .userId("userId")
                .build();
    }

    public static UserEntity userEntityPersisted(){
        return UserEntity.builder()
                .id("userId")
                .secretKey("secretKey")
                .firstName("firstName")
                .lastName("lastName")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .verifiedAt(LocalDateTime.now())
                .archivedAt(LocalDateTime.now())
                .filled(true)
                .addressId("addressId")
                .build();
    }
    public static AddressEntity addressEntityPersisted(){
        return AddressEntity.builder()
                .id("addressId")
                .address("address")
                .zipCode("zipCode")
                .city("city")
                .state("state")
                .countryId(1)
                .build();
    }

    public static CountryEntity countryEntityPersisted(){
        return CountryEntity.builder()
                .id(1)
                .name("Russia")
                .alpha2("RU")
                .alpha3("RUS")
                .build();
    }

    public static IndividualEntity individualEntityCompleted(){
        CountryEntity countryEntity = countryEntityPersisted();
        AddressEntity addressEntity = addressEntityPersisted();
        addressEntity.setCountryEntity(countryEntity);
        UserEntity userEntity = userEntityPersisted();
        userEntity.setAddressEntity(addressEntity);
        IndividualEntity individualEntity = individualEntityPersisted();
        individualEntity.setUserEntity(userEntity);
        return individualEntity;
    }

    public static IndividualRequestDTO individualRequestDTO(){
        return IndividualRequestDTO.builder()
                .secretKey("secretKey")
                .firstName("firstName")
                .lastName("lastName")
                .address("address")
                .zipCode("zipCode")
                .city("city")
                .state("state")
                .country("Russia")
                .passportNumber("passportNumber")
                .phoneNumber("phoneNumber")
                .email("email")
                .build();
    }

    public static UserHistoryEntity userHistoryEntity(){
        return UserHistoryEntity.builder()
                .id("userId")
                .created(LocalDateTime.now())
                .userType(UserType.INDIVIDUAL)
                .reason("by system")
                .comment("by system")
                .build();
    }

    public static IndividualEntity individualEntityTransient() {
        return IndividualEntity.builder()
                .passportNumber("passportNumber")
                .phoneNumber("phoneNumber")
                .email("email")
                .status(Status.ACTIVE)
                .build();
    }

    public static UserEntity userEntityTransient(){
        return UserEntity.builder()
                .secretKey("secretKey")
                .firstName("firstName")
                .lastName("lastName")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .verifiedAt(LocalDateTime.now())
                .archivedAt(LocalDateTime.now())
                .filled(true)
                .build();
    }
    public static AddressEntity addressEntityTransient(){
        return AddressEntity.builder()
                .address("address")
                .zipCode("zipCode")
                .city("city")
                .state("state")
                .build();
    }

    public static CountryEntity countryEntityTransient(){
        return CountryEntity.builder()
                .name("Russia")
                .alpha2("RU")
                .alpha3("RUS")
                .build();
    }

    public static IndividualEntity individualEntityTransient2() {
        return IndividualEntity.builder()
                .passportNumber("2passportNumber2")
                .phoneNumber("2phoneNumber2")
                .email("2email2")
                .status(Status.ACTIVE)
                .build();
    }

    public static UserEntity userEntityTransient2(){
        return UserEntity.builder()
                .secretKey("2secretKey2")
                .firstName("2firstName2")
                .lastName("2lastName2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .verifiedAt(LocalDateTime.now())
                .archivedAt(LocalDateTime.now())
                .filled(true)
                .build();
    }
    public static AddressEntity addressEntityTransient2(){
        return AddressEntity.builder()
                .address("2address2")
                .zipCode("2zipCode2")
                .city("2city2")
                .state("2state2")
                .build();
    }

    public static CountryEntity countryEntityTransient2(){
        return CountryEntity.builder()
                .name("Unated States")
                .alpha2("US")
                .alpha3("USA")
                .build();
    }
}


