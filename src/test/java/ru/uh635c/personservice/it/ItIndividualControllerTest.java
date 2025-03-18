package ru.uh635c.personservice.it;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.uh635c.dto.IndividualRequestDTO;
import ru.uh635c.personservice.config.PostgreTestcontainerConfig;
import ru.uh635c.personservice.entity.*;
import ru.uh635c.personservice.repository.*;
import ru.uh635c.personservice.utils.DataUtils;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(PostgreTestcontainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ItIndividualControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @BeforeEach
    public void beforeAll() {
        individualRepository.deleteAll().block();
        userRepository.deleteAll().block();
        addressRepository.deleteAll().block();
        userHistoryRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Test get individual by id functionality")
    public void givenIndividualId_whenGetIndividual_thenReturnIndividualResponseDto() {

        //given
        IndividualEntity individualEntity = DataUtils.individualEntityTransient();
        UserEntity userEntity = DataUtils.userEntityTransient();
        AddressEntity addressEntity = DataUtils.addressEntityTransient();
        CountryEntity countryEntity = DataUtils.countryEntityTransient();

        CountryEntity countryPersisted = countryRepository.findByName(countryEntity.getName()).block();
        addressRepository.save(addressEntity.toBuilder().countryId(countryPersisted.getId()).build()).block();
        AddressEntity addressPersisted = addressRepository.findAll().blockFirst();
        userRepository.save(userEntity.toBuilder().addressId(addressPersisted.getId()).build()).block();
        UserEntity userPersisted = userRepository.findAll().blockFirst();
        individualRepository.save(individualEntity.toBuilder().userId(userPersisted.getId()).build()).block();
        IndividualEntity individualPersisted = individualRepository.findAll().blockFirst();

        //when
        WebTestClient.ResponseSpec exchange = webClient.get()
                .uri("/api/v1/individuals/{id}", individualPersisted.getId())
                .exchange();

        //then
        exchange.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.first_name").isEqualTo("firstName")
                .jsonPath("$.address").isEqualTo("address")
                .jsonPath("$.country").isEqualTo("Russia");
    }


    @Test
    @DisplayName("Test get all individuals functionality")
    public void given_whenGetAllIndividuals_thenReturnIndividualResponseDtos() {
        //given
        IndividualEntity individualEntity = DataUtils.individualEntityTransient();
        UserEntity userEntity = DataUtils.userEntityTransient();
        AddressEntity addressEntity = DataUtils.addressEntityTransient();
        CountryEntity countryEntity = DataUtils.countryEntityTransient();

        IndividualEntity individualEntity2 = DataUtils.individualEntityTransient2();
        UserEntity userEntity2 = DataUtils.userEntityTransient2();
        AddressEntity addressEntity2 = DataUtils.addressEntityTransient2();
        CountryEntity countryEntity2 = DataUtils.countryEntityTransient2();

        CountryEntity countryPersisted = countryRepository.findByName(countryEntity.getName()).block();
        addressRepository.save(addressEntity.toBuilder().countryId(countryPersisted.getId()).build()).block();
        AddressEntity addressPersisted = addressRepository.findAll().blockFirst();
        userRepository.save(userEntity.toBuilder().addressId(addressPersisted.getId()).build()).block();
        UserEntity userPersisted = userRepository.findAll().blockFirst();
        individualRepository.save(individualEntity.toBuilder().userId(userPersisted.getId()).build()).block();

        CountryEntity countryPersisted2 = countryRepository.findByName(countryEntity2.getName()).block();
        addressRepository.save(addressEntity2.toBuilder().countryId(countryPersisted2.getId()).build()).block();
        AddressEntity addressPersisted2 = addressRepository.findAll().blockLast();
        userRepository.save(userEntity2.toBuilder().addressId(addressPersisted2.getId()).build()).block();
        UserEntity userPersisted2 = userRepository.findAll().blockLast();
        individualRepository.save(individualEntity2.toBuilder().userId(userPersisted2.getId()).build()).block();

        //when
        WebTestClient.ResponseSpec exchange = webClient.get()
                .uri("/api/v1/individuals/all")
                .exchange();

        //then
        exchange.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.size()").isEqualTo(2)
                .jsonPath("$[0].first_name").isEqualTo("firstName")
                .jsonPath("$[1].first_name").isEqualTo("2firstName2");
    }

    @Test
    @DisplayName("Test save individual functionality")
    public void givenIndividualRequestDto_whenSaveIndividual_thenReturnIndividualResponseDto() {
        //given
        IndividualRequestDTO dto = DataUtils.individualRequestDTO();

        //when
        WebTestClient.ResponseSpec exchange = webClient.post()
                .uri("/api/v1/individuals")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(dto), IndividualRequestDTO.class)
                .exchange();

        //then
        exchange.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.first_name").isEqualTo("firstName")
                .jsonPath("$.address").isEqualTo("address")
                .jsonPath("$.country").isEqualTo("Russia");
    }

    @Test
    @DisplayName("Test update individual functionality")
    public void givenIndividualRequestDto_whenUpdateIndividual_thenReturnUpdatedIndividualResponseDto() {
        //given
        IndividualEntity individualEntity = DataUtils.individualEntityTransient();
        UserEntity userEntity = DataUtils.userEntityTransient();
        AddressEntity addressEntity = DataUtils.addressEntityTransient();
        CountryEntity countryEntity = DataUtils.countryEntityTransient();

        CountryEntity countryPersisted = countryRepository.findByName(countryEntity.getName()).block();
        addressRepository.save(addressEntity.toBuilder().countryId(countryPersisted.getId()).build()).block();
        AddressEntity addressPersisted = addressRepository.findAll().blockFirst();
        userRepository.save(userEntity.toBuilder().addressId(addressPersisted.getId()).build()).block();
        UserEntity userPersisted = userRepository.findAll().blockFirst();
        individualRepository.save(individualEntity.toBuilder().userId(userPersisted.getId()).build()).block();
        IndividualEntity individualPersisted = individualRepository.findAll().blockFirst();

        IndividualRequestDTO dto = DataUtils.individualRequestDTO().toBuilder()
                .id(individualPersisted.getId())
                .firstName("updateFirstName")
                .address("updateAddress")
                .build();

        //when
        WebTestClient.ResponseSpec exchange = webClient.put()
                .uri("/api/v1/individuals")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(dto), IndividualRequestDTO.class)
                .exchange();

        //then
        UserHistoryEntity userHistoryEntity = userHistoryRepository.findAll().blockFirst();
        assertThat(userHistoryEntity.getChangedValues().toString()).isEqualTo("{\"address\":\"updateAddress\",\"firstName\":\"updateFirstName\"}");
        exchange.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(individualPersisted.getId())
                .jsonPath("$.first_name").isEqualTo("updateFirstName")
                .jsonPath("$.address").isEqualTo("updateAddress");
    }


}
