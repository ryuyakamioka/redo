package com.kamiokaweb.redo.repository.user;

import com.kamiokaweb.redo.infrastructure.user.UserDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserDto, Long> {
}
