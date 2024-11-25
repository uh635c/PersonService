package ru.uh635c.personservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.uh635c.dto.IndividualRequestDTO;
import ru.uh635c.dto.IndividualResponseDTO;
import ru.uh635c.personservice.entity.IndividualEntity;
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
        return individualRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("not found")))
                .flatMap(individualEntity -> userRepository.findById(individualEntity.getUserId())
                        .flatMap(userEntity -> {
                            individualEntity.setUserEntity(userEntity);
                            return addressRepository.findById(userEntity.getAddressId())
                                    .flatMap(addressEntity -> {
                                        userEntity.setAddress(addressEntity);
                                        return countryRepository.findById(addressEntity.getCountryId())
                                                .map(countryEntity -> {
                                                    addressEntity.setCountry(countryEntity);
                                                    return individualEntity;
                                                });
                                    });
                        }))
                .map(individualMapper::map);
    }

    @Override
    public Flux<IndividualResponseDTO> getAllIndividuals() {
        return individualRepository.findAll()
                .switchIfEmpty(Mono.error(new RuntimeException("not found")))
                .flatMap(individualEntity -> userRepository.findById(individualEntity.getUserId())
                        .flatMap(userEntity -> {
                            individualEntity.setUserEntity(userEntity);
                            return addressRepository.findById(userEntity.getAddressId())
                                    .flatMap(addressEntity -> {
                                        userEntity.setAddress(addressEntity);
                                        return countryRepository.findById(addressEntity.getCountryId())
                                                .map(countryEntity -> {
                                                    addressEntity.setCountry(countryEntity);
                                                    return individualEntity;
                                                });
                                    });
                        }))
                .map(individualMapper::map);
    }


    @Override
    public Mono<IndividualResponseDTO> saveIndividual(IndividualRequestDTO individualDTO) {
        return countryRepository.findByName(individualDTO.getCountry())
                .flatMap(countryEntity -> addressRepository.save(addressMapper.mapAddress(individualDTO)
                                .toBuilder()
                                .countryId(countryEntity.getId())
                                .build())
                        .map(addressEntity -> {
                            addressEntity.setCountry(countryEntity);
                            return addressEntity;
                        }))
                .flatMap(addressEntity -> userRepository.save(userMapper.mapUser(individualDTO)
                                .toBuilder()
                                .addressId(addressEntity.getId())
                                .build())
                        .map(userEntity -> {
                            userEntity.setAddress(addressEntity);
                            return userEntity;
                        }))
                .flatMap(userEntity -> individualRepository.save(individualMapper.mapIndividualEntity(individualDTO)
                                .toBuilder()
                                .userId(userEntity.getId())
                                .build())
                        .map(individualEntity -> {
                            individualEntity.setUserEntity(userEntity);
                            return individualEntity;
                        }))
                .map(individualMapper::map);
    }

    @Override
    public Mono<IndividualResponseDTO> updateIndividual(IndividualRequestDTO individualDTO) {
        return null;
    }

    ////////////////////////////////////private methods////////////////////////////////////

    private Mono<IndividualEntity> getIndividualEntity(IndividualEntity individualEntity) {
        return userRepository.findById(individualEntity.getUserId())
                .flatMap(userEntity -> {
                    individualEntity.setUserEntity(userEntity);
                    return addressRepository.findById(userEntity.getAddressId())
                            .flatMap(addressEntity -> {
                                userEntity.setAddress(addressEntity);
                                return countryRepository.findById(addressEntity.getCountryId())
                                        .map(countryEntity -> {
                                            addressEntity.setCountry(countryEntity);
                                            return individualEntity;
                                        });
                            });
                })
    }
}
