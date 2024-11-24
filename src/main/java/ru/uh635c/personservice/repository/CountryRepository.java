package ru.uh635c.personservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.uh635c.personservice.entity.CountryEntity;

public interface CountryRepository extends R2dbcRepository<CountryEntity, Integer> {

    Mono<CountryEntity> findById(int id);
    Mono<CountryEntity> findByName(String name);
}
