package com.kamiokaweb.redo.infrastructure.task;

import com.kamiokaweb.redo.model.client.Client;
import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;
import com.kamiokaweb.redo.model.task.TaskStatus;
import com.kamiokaweb.redo.model.task.TaskTitle;
import com.kamiokaweb.redo.model.taskitem.TaskItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Table("tasks")
public record TaskDto(
        @Id
        Long id,
        String title,
        String status,
        LocalDate requestDate,
        Long clientId,
        String note,
        LocalDate expectedDeliveryDate,
        LocalDate deliveryDate,
        LocalDate billingDate,
        LocalDateTime createdAt
) {
        public TaskDto(Task task) {
                this(
                        task.taskId() != null && task.taskId().value() != 0 ? task.taskId().value() : null,
                        task.taskTitle().value(),
                        task.taskStatus().name(),
                        task.requestDate(),
                        task.client() != null ? task.client().clientId().value() : null,
                        task.note(),
                        task.expectedDeliveryDate(),
                        task.deliveryDate(),
                        task.billingDate(),
                        LocalDateTime.now()
                );
        }

        public Task from(Client client, List<TaskItem> taskItems) {
                return new Task(
                        new TaskId(id),
                        new TaskTitle(title),
                        TaskStatus.valueOf(status),
                        requestDate,
                        client,
                        note,
                        taskItems,
                        expectedDeliveryDate,
                        deliveryDate,
                        billingDate
                );
        }
}
