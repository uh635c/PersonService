package ru.uh635c.personservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ru.uh635c.personservice.entity.UserHistoryEntity;

import java.util.UUID;

public interface UserHistoryRepository extends R2dbcRepository<UserHistoryEntity, UUID> {
}
