package com.kamiokaweb.redo.infrastructure.taskstatus;

import com.kamiokaweb.redo.model.taskstatus.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("task_statuses")
public record TaskStatusDto(
        @Id
        Long id,
        String code,
        String name,
        String displayColor,
        Integer displayOrder,
        Boolean isCompleted,
        LocalDateTime createdAt
) {
    public TaskStatusMaster from() {
        return new TaskStatusMaster(
                new TaskStatusId(id),
                new TaskStatusCode(code),
                new TaskStatusName(name),
                new DisplayColor(displayColor),
                new DisplayOrder(displayOrder),
                isCompleted
        );
    }
}
