package com.kamiokaweb.redo.infrastructure.taskitem;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskItemAccessor extends CrudRepository<TaskItemDto, Long> {
    @Query("SELECT * FROM task_items WHERE task_id = :taskId")
    List<TaskItemDto> findByTaskId(@Param("taskId") Long taskId);

    @Modifying
    @Query("DELETE FROM task_items WHERE task_id = :taskId")
    void deleteByTaskId(@Param("taskId") Long taskId);
}
