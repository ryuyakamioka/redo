package com.kamiokaweb.redo.infrastructure.taskstatus;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskStatusAccessor extends CrudRepository<TaskStatusDto, Long> {
    List<TaskStatusDto> findAllByOrderByDisplayOrderAsc();
    Optional<TaskStatusDto> findByCode(String code);
}
