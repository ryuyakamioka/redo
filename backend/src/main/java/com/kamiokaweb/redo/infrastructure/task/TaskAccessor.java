package com.kamiokaweb.redo.infrastructure.task;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskAccessor extends CrudRepository<TaskDto, Long> {
    List<TaskDto> findAllByOrderByCreatedAtAsc();
}
