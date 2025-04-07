package ru.uh635c.personservice.rest;

import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
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
public class IndividualRestControllerV1 {

    private final IndividualService individualService;

    @GetMapping("/{id}")
    public Mono<IndividualResponseDTO> getIndividual(@PathVariable String id) {
        if(StringUtil.isNullOrEmpty(id)){
            return Mono.error(new UserNotFoundException("Provided id is null or empty", "USER_NOT_FOUND"));
        }
        return individualService.getIndividual(id);
    }

    @GetMapping("/all")
    public Flux<IndividualResponseDTO> getAllIndividuals() {
        return individualService.getAllIndividuals();
    }

    @PostMapping()
    public Mono<IndividualResponseDTO> saveIndividual(@RequestBody @Validated IndividualRequestDTO individualRequestDTO) {
        if(individualRequestDTO == null){
            return Mono.error(new UserSavingException("Provided individualDto to save is null", "USER_SAVING_FAILED"));
        }
        return individualService.saveIndividual(individualRequestDTO);
    }

    @PutMapping()
    public Mono<IndividualResponseDTO> updateIndividual(@RequestBody IndividualRequestDTO individualRequestDTO) {
        if(individualRequestDTO == null){
            return Mono.error(new UserSavingException("Provided individualDto to update is null", "USER_SAVING_FAILED"));
        }
        return individualService.updateIndividual(individualRequestDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> rollbackDelete(@PathVariable String id){
        if(StringUtil.isNullOrEmpty(id)){
            return Mono.error(new UserNotFoundException("Provided id is null or empty", "USER_NOT_FOUND"));
        }
        return individualService.deleteIndividual(id);
    }

}
