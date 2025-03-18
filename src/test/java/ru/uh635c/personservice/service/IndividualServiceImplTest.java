package ru.uh635c.personservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.uh635c.dto.IndividualRequestDTO;
import ru.uh635c.dto.IndividualResponseDTO;
import ru.uh635c.personservice.entity.*;
import ru.uh635c.personservice.mappers.*;
import ru.uh635c.personservice.repository.*;
import ru.uh635c.personservice.service.impl.IndividualServiceImpl;
import ru.uh635c.personservice.utils.DataUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
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
    @Mock
    private UserHistoryRepository userHistoryRepository;
    @Spy
    private IndividualMapper individualMapper = new IndividualMapperImpl();
    @Spy
    private UserMapper userMapper = new UserMapperImpl();
    @Spy
    private AddressMapper addressMapper = new AddressMapperImpl();
    @Spy
    private Javers javers = JaversBuilder.javers().build();
    @Spy
    ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private TransactionalOperator operator;

    @InjectMocks
    private IndividualServiceImpl individualServiceUnderTest;

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
        Mono<IndividualResponseDTO> obtainedIndividualResponceDto = individualServiceUnderTest
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
        AddressEntity addressEntity1 = DataUtils.addressEntityPersisted();
        CountryEntity countryEntity1 = DataUtils.countryEntityPersisted();


        BDDMockito.given(individualRepository.findAll())
                .willReturn(Flux.just(individualEntity1, individualEntity2));
        BDDMockito.given(userRepository.findById("userId"))
                .willReturn(Mono.just(userEntity1));
        BDDMockito.given(addressRepository.findById("addressId"))
                .willReturn(Mono.just(addressEntity1));
        BDDMockito.given(countryRepository.findById(1))
                .willReturn(Mono.just(countryEntity1));

        //when
        Flux<IndividualResponseDTO> obtainedIndividualsDto = individualServiceUnderTest
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

    @Test
    @DisplayName("Test save individual functionality")
    public void givenIndividualRequestDto_whenSaveIndividual_thenReturnSuccess() {
        //given
        IndividualRequestDTO individualRequestDTO = DataUtils.individualRequestDTO();
        CountryEntity countryEntity = DataUtils.countryEntityPersisted();
        AddressEntity addressEntity = DataUtils.addressEntityPersisted();
        UserEntity userEntity = DataUtils.userEntityPersisted();
        IndividualEntity individualEntity = DataUtils.individualEntityPersisted();

        BDDMockito.given(countryRepository.findByName(anyString())).willReturn(Mono.just(countryEntity));
        BDDMockito.given(addressRepository.save(any(AddressEntity.class))).willReturn(Mono.just(addressEntity));
        BDDMockito.given(userRepository.save(any(UserEntity.class))).willReturn(Mono.just(userEntity));
        BDDMockito.given(individualRepository.save(any(IndividualEntity.class))).willReturn(Mono.just(individualEntity));
        BDDMockito.given(operator.transactional(any(Mono.class)))
                .willAnswer(invocation -> {
                    Object[] args = invocation.getArguments();
                    return args[0];
                });

        //when
        Mono<IndividualResponseDTO> obtainedResponseDto = individualServiceUnderTest.saveIndividual(individualRequestDTO);

        //then
        StepVerifier.create(obtainedResponseDto)
                .assertNext(dto -> {
                    verify(countryRepository, times(1)).findByName("Russia");
                    verify(addressRepository, times(1)).save(any(AddressEntity.class));
                    verify(userRepository, times(1)).save(any(UserEntity.class));
                    verify(individualRepository, times(1)).save(any(IndividualEntity.class));
                    assertThat(dto).isEqualTo(individualMapper.map(individualEntity));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test update individual functionality")
    public void givenIndividualRequestDtoWithId_whenUpdateIndividual_thenReturnSuccess() throws JsonProcessingException {
        //given
            //for getIndividual
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

            //for updateIndividual
        IndividualRequestDTO individualRequestDTO = DataUtils.individualRequestDTO().toBuilder()
                .id("individualId")
                .build();
        IndividualResponseDTO individualResponseDTO = individualMapper.map(DataUtils.individualEntityCompleted()).toBuilder()
                .firstName("updatedFirstName")
                .phoneNumber("updatePassportNumber")
                .build();

        UserHistoryEntity userHistoryEntity = DataUtils.userHistoryEntity().toBuilder()
                .userId(individualEntity.getUserId())
                .changedValues(objectMapper.readTree("{\"firstName\":\"updatedFirstName\", \"passportNumber\":\"updatePassportNumber\"}"))
                .build();

        BDDMockito.given(userHistoryRepository.save(any(UserHistoryEntity.class))).willReturn(Mono.just(userHistoryEntity));
        BDDMockito.given(operator.transactional(any(Mono.class)))
                .willAnswer(invocation -> {
                    Object[] args = invocation.getArguments();
                    return args[0];
                });

            //for saveIndividual
        BDDMockito.given(countryRepository.findByName(anyString())).willReturn(Mono.just(countryEntity));
        BDDMockito.given(addressRepository.save(any(AddressEntity.class))).willReturn(Mono.just(addressEntity));
        BDDMockito.given(userRepository.save(any(UserEntity.class))).willReturn(Mono.just(userEntity));
        BDDMockito.given(individualRepository.save(any(IndividualEntity.class))).willReturn(Mono.just(individualEntity));

        //when
        Mono<IndividualResponseDTO> obtainedResponseDto = individualServiceUnderTest.updateIndividual(individualRequestDTO);

        //then
        StepVerifier.create(obtainedResponseDto)
                .assertNext(dto -> {
                    verify(individualRepository, times(2)).findById(anyString());
                    verify(userHistoryRepository, times(1)).save(any(UserHistoryEntity.class));
                    verify(countryRepository, times(1)).findByName(anyString());
                    verify(addressRepository, times(1)).save(any(AddressEntity.class));
                    verify(userRepository, times(1)).save(any(UserEntity.class));
                    verify(individualRepository, times(1)).save(any(IndividualEntity.class));
                    assertThat(dto).isExactlyInstanceOf(IndividualResponseDTO.class);
                })
                .verifyComplete();

    }

}
