package ru.uh635c.personservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.uh635c.dto.IndividualRequestDTO;
import ru.uh635c.dto.IndividualResponseDTO;
import ru.uh635c.personservice.exceptions.UserNotFoundException;
import ru.uh635c.personservice.exceptions.UserSavingException;
import ru.uh635c.personservice.service.IndividualService;

@RestController
@RequestMapping("/api/v1/individuals")
@RequiredArgsConstructor
public class IndividualController {

    private final IndividualService individualService;

    @GetMapping("/{id}")
    public Mono<IndividualResponseDTO> getIndividual(@PathVariable String id) {
        if(id == null || id.isEmpty()){
            return Mono.error(new UserNotFoundException("Provided id is null or empty", "USER_NOT_FOUND")); //TODO или сделать через валидацию лучше?
        }
        return individualService.getIndividual(id);
    }

    @GetMapping("/all")
    public Flux<IndividualResponseDTO> getAllIndividuals() {
        return individualService.getAllIndividuals();
    }

    @PostMapping()
    public Mono<IndividualResponseDTO> saveIndividual(@RequestBody @Validated IndividualRequestDTO individualRequestDTO) { //TODO нужно ли как то валидировать заполненость входящего обьекта?
        if(individualRequestDTO == null){
            return Mono.error(new UserSavingException("Provided individualDto to save is null", "USER_SAVING_FAILED"));
        }
        return individualService.saveIndividual(individualRequestDTO);
    }

    @PutMapping()
    public Mono<IndividualResponseDTO> updateIndividual(@RequestBody IndividualRequestDTO individualRequestDTO) { //TODO нужно ли как то валидировать заполненость входящего обьекта?
        if(individualRequestDTO == null){
            return Mono.error(new UserSavingException("Provided individualDto to update is null", "USER_SAVING_FAILED"));
        }
        return individualService.updateIndividual(individualRequestDTO);
    }

}
