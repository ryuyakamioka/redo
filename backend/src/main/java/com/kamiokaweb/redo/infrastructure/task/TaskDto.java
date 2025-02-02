package com.kamiokaweb.redo.infrastructure.task;

import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;
import com.kamiokaweb.redo.model.task.TaskStatus;
import com.kamiokaweb.redo.model.task.TaskTitle;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("tasks")
public record TaskDto(
        @Id
        Long id,
        String title,
        String status,
        LocalDateTime createdAt
) {
        public TaskDto(Task task) {
                this(null, task.taskTitle().value(), task.taskStatus().name(), LocalDateTime.now());
        }

        public Task from() {
                return new Task(new TaskId(id), new TaskTitle(title), TaskStatus.valueOf(status));
        }
}
