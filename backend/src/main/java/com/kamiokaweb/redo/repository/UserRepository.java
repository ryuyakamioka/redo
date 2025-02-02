package com.kamiokaweb.redo.repository;

import com.kamiokaweb.redo.infrastructure.UserDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserDto, Long> {
}
