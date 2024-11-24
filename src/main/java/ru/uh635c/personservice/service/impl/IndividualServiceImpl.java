package ru.uh635c.personservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.uh635c.dto.IndividualRequestDTO;
import ru.uh635c.dto.IndividualResponseDTO;
import ru.uh635c.personservice.entity.AddressEntity;
import ru.uh635c.personservice.entity.CountryEntity;
import ru.uh635c.personservice.entity.IndividualEntity;
import ru.uh635c.personservice.entity.UserEntity;
import ru.uh635c.personservice.mappers.AddressMapper;
import ru.uh635c.personservice.mappers.IndividualMapper;
import ru.uh635c.personservice.mappers.UserMapper;
import ru.uh635c.personservice.repository.*;
import ru.uh635c.personservice.service.IndividualService;

@Service
@RequiredArgsConstructor
public class IndividualServiceImpl implements IndividualService {

    private final IndividualRepository individualRepository;
    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    private final IndividualMapper individualMapper;
    private final AddressMapper addressMapper;
    private final UserMapper userMapper;

    @Override
    public Mono<IndividualResponseDTO> getIndividual(String id) {
//        return individualRepository.findById(id)
//                .switchIfEmpty(Mono.error(new RuntimeException("not found")))
//                .map(individualEntity -> {
//                            userRepository.findById(individualEntity.getUserId())
//                                    .flatMap(userEntity -> {
//                                        individualEntity.setUserEntity(userEntity);
//                                        return addressRepository.findById(userEntity.getAddressId())
//                                                .flatMap(addressEntity -> {
//                                                    userEntity.setAddressEntity(addressEntity);
//                                                    return countryRepository.findById(addressEntity.getCountryId())
//                                                            .map(countryEntity -> {
//                                                                addressEntity.setCountry(countryEntity);
//                                                                return countryEntity;
//                                                            });
//                                                });
//                                    }).block();
//                            return individualEntity; //сомнительно
//                        }
//                )
//                .map(individualMapper::map);
        return null;
    }

    @Override
    public Flux<IndividualResponseDTO> getAllIndividuals() {
        individualRepository.findAll()
                .switchIfEmpty(Mono.error(new RuntimeException("not found")))
                .map(individualEntity -> {
                    userRepository.findById(individualEntity.getUserId())
                            .flatMap(userEntity -> {
                                individualEntity.setUserEntity(userEntity);
                                return addressRepository.findById(userEntity.getAddressId())
                                        .flatMap(addressEntity -> {
                                            userEntity.setAddressEntity(addressEntity);
                                            return countryRepository.findById(addressEntity.getCountryId())
                                                    .map(countryEntity -> {
                                                        addressEntity.setCountry(countryEntity);
                                                        return countryEntity;
                                                    });
                                        });
                            }).block();
                    return individualEntity; //сомнительно
                });
        return null;
    }

    @Override
    public Mono<IndividualResponseDTO> saveIndividual(IndividualRequestDTO individualDTO) {
//        countryRepository.findByName(individualDTO.getCountry())
//                .flatMap(countryEntity -> addressRepository.save(addressMapper.mapAddress(individualDTO)
//                        .toBuilder()
//                        .countryId(countryEntity.getId())
//                        .build()))
//                .flatMap(addressEntity -> userRepository.save(userMapper.mapUser(individualDTO)
//                        .toBuilder()
//                        .addressId(addressEntity.getId())
//                        .build()))
//                .flatMap(userEntity -> individualRepository.save(individualMapper.mapIndividualEntity(individualDTO)
//                        .toBuilder()
//                        .userId(userEntity.getId())
//                        .build()))
//                .map(individualMapper::map)



        return null;
    }

    @Override
    public Mono<IndividualResponseDTO> updateIndividual(IndividualRequestDTO individualDTO) {
        return null;
    }
}
