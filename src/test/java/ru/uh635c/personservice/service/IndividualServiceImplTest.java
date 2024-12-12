package ru.uh635c.personservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.uh635c.dto.IndividualResponseDTO;
import ru.uh635c.personservice.entity.AddressEntity;
import ru.uh635c.personservice.entity.CountryEntity;
import ru.uh635c.personservice.entity.IndividualEntity;
import ru.uh635c.personservice.entity.UserEntity;
import ru.uh635c.personservice.mappers.*;
import ru.uh635c.personservice.repository.AddressRepository;
import ru.uh635c.personservice.repository.CountryRepository;
import ru.uh635c.personservice.repository.IndividualRepository;
import ru.uh635c.personservice.repository.UserRepository;
import ru.uh635c.personservice.service.impl.IndividualServiceImpl;
import ru.uh635c.personservice.utils.DataUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IndividualServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CountryRepository countryRepository;
    @Spy
    private IndividualMapper individualMapper = new IndividualMapperImpl();
    @Spy
    private UserMapper userMapper = new UserMapperImpl();
    @Spy
    private AddressMapper addressMapper = new AddressMapperImpl();

    @InjectMocks
    private IndividualServiceImpl individualService;

    @Test
    @DisplayName("Test get individual by id functionality")
    public void givenIndividualId_whenGetIndividualById_thenReturnSuccess() throws InterruptedException {
        //given
        String individualId = "individualId";
        IndividualEntity individualEntity = DataUtils.individualEntityPersisted();
        UserEntity userEntity = DataUtils.userEntityPersisted();
        AddressEntity addressEntity = DataUtils.addressEntityPersisted();
        CountryEntity countryEntity = DataUtils.countryEntityPersisted();


        BDDMockito.given(individualRepository.findById(anyString()))
                .willReturn(Mono.just(individualEntity));
        BDDMockito.given(userRepository.findById(anyString()))
                .willReturn(Mono.just(userEntity));
        BDDMockito.given(addressRepository.findById(anyString()))
                .willReturn(Mono.just(addressEntity));
        BDDMockito.given(countryRepository.findById(anyInt()))
                .willReturn(Mono.just(countryEntity));

        //when
        Mono<IndividualResponseDTO> obtainedIndividualResponceDto = individualService
                .getIndividual(individualId);

        //given
        StepVerifier.create(obtainedIndividualResponceDto)
                .assertNext(dto -> {
                    verify(individualRepository, times(1)).findById(anyString());
                    verify(userRepository, times(1)).findById(anyString());
                    verify(addressRepository, times(1)).findById(anyString());
                    verify(countryRepository, times(1)).findById(anyInt());
                    assertThat(dto).isEqualTo(individualMapper.map(individualEntity));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test get all individuals functionality")
    public void given_whenGetAllIndividual_thenReturnSuccess() {
        //given
        IndividualEntity individualEntity1 = DataUtils.individualEntityPersisted();
        IndividualEntity individualEntity2 = DataUtils.individualEntityPersisted().toBuilder()
                .id("individualId2")
                .build();
        UserEntity userEntity1 = DataUtils.userEntityPersisted();
        UserEntity userEntity2 = DataUtils.userEntityPersisted().toBuilder()
                .id("userId2")
                .build();
        AddressEntity addressEntity1 = DataUtils.addressEntityPersisted();
        AddressEntity addressEntity2 = DataUtils.addressEntityPersisted().toBuilder()
                .id("addressId2")
                .build();
        CountryEntity countryEntity1 = DataUtils.countryEntityPersisted();
        CountryEntity countryEntity2 = DataUtils.countryEntityPersisted().toBuilder()
                .id(2)
                .build();

        BDDMockito.given(individualRepository.findAll())
                .willReturn(Flux.just(individualEntity1, individualEntity2));
        BDDMockito.given(userRepository.findById("userId"))
                .willReturn(Mono.just(userEntity1));
        BDDMockito.given(userRepository.findById("userId2"))
                .willReturn(Mono.just(userEntity2));
        BDDMockito.given(addressRepository.findById("addressId"))
                .willReturn(Mono.just(addressEntity1));
        BDDMockito.given(addressRepository.findById("addressId2"))
                .willReturn(Mono.just(addressEntity2));
        BDDMockito.given(countryRepository.findById(1))
                .willReturn(Mono.just(countryEntity1));
        BDDMockito.given(countryRepository.findById(2))
                .willReturn(Mono.just(countryEntity2));

        //when
        Flux<IndividualResponseDTO> obtainedIndividualsDto = individualService
                .getAllIndividuals();

        //given
        StepVerifier.create(obtainedIndividualsDto)
                .assertNext(dto -> {
                    verify(individualRepository, times(1)).findAll();
                    verify(userRepository, times(1)).findById(anyString());
                    verify(addressRepository, times(1)).findById(anyString());
                    verify(countryRepository, times(1)).findById(anyInt());
                    assertThat(dto).isEqualTo(individualMapper.map(individualEntity1));
                })
                .assertNext(dto -> {
                    verify(userRepository, times(2)).findById(anyString());
                    verify(addressRepository, times(2)).findById(anyString());
                    verify(countryRepository, times(2)).findById(anyInt());
                    assertThat(dto).isEqualTo(individualMapper.map(individualEntity2));
                })
                .verifyComplete();
    }


}
