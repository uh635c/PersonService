package ru.uh635c.personservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.uh635c.personservice.entity.UserEntity;

public interface UserRepository extends R2dbcRepository<UserEntity, String> {
}
