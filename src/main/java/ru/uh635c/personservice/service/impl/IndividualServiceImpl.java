package ru.uh635c.personservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.uh635c.dto.IndividualRequestDTO;
import ru.uh635c.dto.IndividualResponseDTO;
import ru.uh635c.personservice.entity.IndividualEntity;
import ru.uh635c.personservice.entity.UserHistoryEntity;
import ru.uh635c.personservice.entity.UserType;
import ru.uh635c.personservice.exceptions.UserNotFoundException;
import ru.uh635c.personservice.exceptions.UserSavingException;
import ru.uh635c.personservice.mappers.AddressMapper;
import ru.uh635c.personservice.mappers.IndividualMapper;
import ru.uh635c.personservice.mappers.UserMapper;
import ru.uh635c.personservice.repository.*;
import ru.uh635c.personservice.service.IndividualService;

import java.time.LocalDateTime;

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
    private final Javers javers;
    private final TransactionalOperator operator;
    private final ObjectMapper objectMapper;


    @Override
    public Mono<IndividualResponseDTO> getIndividual(String id) {
        return individualRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("There is no user with provided id = " + id, "USER_NOT_FOUND")))
                .flatMap(this::getIndividualEntity)
                .map(individualMapper::map);
    }

    @Override
    public Flux<IndividualResponseDTO> getAllIndividuals() {
        return individualRepository.findAll()
                .switchIfEmpty(Mono.error(new UserNotFoundException("There are no users", "USER_NOT_FOUND")))
                .flatMap(this::getIndividualEntity)
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
                            addressEntity.setCountryEntity(countryEntity);
                            return addressEntity;
                        }))
                .flatMap(addressEntity -> userRepository.save(userMapper.mapUser(individualDTO)
                                .toBuilder()
                                .addressId(addressEntity.getId())
                                .build())
                        .map(userEntity -> {
                            userEntity.setAddressEntity(addressEntity);
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
                .as(operator::transactional)
                .map(individualMapper::map);
    }

    @Override
    public Mono<IndividualResponseDTO> updateIndividual(IndividualRequestDTO newIndividualDTO) {
        if (newIndividualDTO.getId() == null || newIndividualDTO.getId().isEmpty()) {
            return Mono.error(new UserSavingException("Provided individualDto`s id to update is null or empty", "USER_SAVING_FAILED"));
        }


        return individualRepository.findById(newIndividualDTO.getId())
                .switchIfEmpty(Mono.error(new UserNotFoundException("There is no user to update with provided id = " + newIndividualDTO.getId(), "USER_NOT_FOUND")))
                .flatMap(this::getIndividualEntity)
                .zipWhen(individualEntity -> Mono.just(individualMapper.map(individualEntity)))
                .flatMap(tuple2 -> {
                    Diff diff = javers.compare(individualMapper.mapIndividualRequestDTO(tuple2.getT2()), newIndividualDTO);
                    JsonObject changes = new JsonObject();
                    diff.getChangesByType(ValueChange.class).forEach(change ->
                            changes.addProperty(change.getPropertyName(), (String) change.getRight()));

                    IndividualEntity individual = tuple2.getT1();

                    try {
                        return userHistoryRepository.save(UserHistoryEntity.builder()
                                        .created(LocalDateTime.now())
                                        .comment("by system")
                                        .reason("by system")
                                        .userType(UserType.INDIVIDUAL)
                                        .userId(individual.getUserId())
                                        .changedValues(objectMapper.readTree(changes.toString()))
                                        .build())
                                .then(countryRepository.findByName(newIndividualDTO.getCountry())
                                        .flatMap(countryEntity -> addressRepository.save(addressMapper.mapAddress(newIndividualDTO).toBuilder()
                                                        .id(individual.getUserEntity().getAddressId())
                                                        .countryId(countryEntity.getId())
                                                        .build())
                                                .map(addressEntity -> {
                                                    addressEntity.setCountryEntity(countryEntity);
                                                    return addressEntity;
                                                }))
                                        .flatMap(addressEntity -> userRepository.save(userMapper.mapUser(newIndividualDTO).toBuilder()
                                                        .id(individual.getUserId())
                                                        .addressId(addressEntity.getId())
                                                        .build())
                                                .map(userEntity -> {
                                                    userEntity.setAddressEntity(addressEntity);
                                                    return userEntity;
                                                }))
                                        .flatMap(userEntity -> individualRepository.save(individualMapper.mapIndividualEntity(newIndividualDTO)
                                                        .toBuilder()
                                                        .id(individual.getId())
                                                        .userId(userEntity.getId())
                                                        .build())
                                                .map(individualEntity -> {
                                                    individualEntity.setUserEntity(userEntity);
                                                    return individualEntity;
                                                })
                                        )
                                );
                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                })
                .as(operator::transactional)
                .map(individualMapper::map);
    }

////////////////////////////////////private methods////////////////////////////////////

    private Mono<IndividualEntity> getIndividualEntity(IndividualEntity individualEntity) {
        return userRepository.findById(individualEntity.getUserId())
                .flatMap(userEntity -> {
                    individualEntity.setUserEntity(userEntity);
                    return addressRepository.findById(userEntity.getAddressId())
                            .flatMap(addressEntity -> {
                                userEntity.setAddressEntity(addressEntity);
                                return countryRepository.findById(addressEntity.getCountryId())
                                        .map(countryEntity -> {
                                            addressEntity.setCountryEntity(countryEntity);
                                            return individualEntity;
                                        });
                            });
                });
    }
}
