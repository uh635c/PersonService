package ru.uh635c.personservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.uh635c.dto.IndividualRequestDTO;
import ru.uh635c.dto.IndividualResponseDTO;

public interface IndividualService {
    Mono<IndividualResponseDTO> saveIndividual(IndividualRequestDTO individualDTO);

    Mono<IndividualResponseDTO> getIndividual(String id);

    Mono<IndividualResponseDTO> updateIndividual(IndividualRequestDTO individualDTO);

    Flux<IndividualResponseDTO> getAllIndividuals();
}
