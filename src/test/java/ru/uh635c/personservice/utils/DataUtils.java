package ru.uh635c.personservice.utils;

import ru.uh635c.entity.Status;
import ru.uh635c.personservice.entity.AddressEntity;
import ru.uh635c.personservice.entity.CountryEntity;
import ru.uh635c.personservice.entity.IndividualEntity;
import ru.uh635c.personservice.entity.UserEntity;

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
                .name("name")
                .alpha2("alpha2")
                .alpha3("alpha3")
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
}
