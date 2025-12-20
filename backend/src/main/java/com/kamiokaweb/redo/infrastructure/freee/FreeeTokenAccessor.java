package com.kamiokaweb.redo.infrastructure.freee;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FreeeTokenAccessor extends CrudRepository<FreeeTokenDto, Long> {

    @Query("SELECT * FROM freee_tokens LIMIT 1")
    Optional<FreeeTokenDto> findLatest();

    @Modifying
    @Query("DELETE FROM freee_tokens")
    void deleteAll();
}
