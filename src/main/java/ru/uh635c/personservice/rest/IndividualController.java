package ru.uh635c.personservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.uh635c.dto.IndividualRequestDTO;
import ru.uh635c.dto.IndividualResponseDTO;
import ru.uh635c.personservice.service.IndividualService;

@RestController
@RequestMapping("/api/v1/individuals")
@RequiredArgsConstructor
public class IndividualController {

    private final IndividualService individualService;

    @GetMapping("/{id}")
    public Mono<IndividualResponseDTO> getIndividual(@PathVariable String id) {
        return individualService.getIndividual(id);
    }

    @GetMapping("/all")
    public Flux<IndividualResponseDTO> getAllIndividuals() {
        return individualService.getAllIndividuals();
    }

    @PostMapping("/")
    public Mono<IndividualResponseDTO> saveIndividual(@RequestBody IndividualRequestDTO individualRequestDTO) {
        return individualService.saveIndividual(individualRequestDTO);
    }

    @PutMapping("/")
    public Mono<IndividualResponseDTO> updateIndividual(@RequestBody IndividualRequestDTO individualRequestDTO) {
        return individualService.updateIndividual(individualRequestDTO);
    }




}
