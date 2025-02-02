package com.kamiokaweb.redo.infrastructure.task;

import org.springframework.data.repository.CrudRepository;

public interface TaskAccessor extends CrudRepository<TaskDto, Long> {
}
