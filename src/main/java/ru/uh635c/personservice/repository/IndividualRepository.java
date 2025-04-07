package ru.uh635c.personservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.uh635c.personservice.entity.IndividualEntity;

public interface IndividualRepository extends R2dbcRepository<IndividualEntity, String> {
}
