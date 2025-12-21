package com.kamiokaweb.redo.repository.task;

import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    void register(Task task);

    Optional<Task> get(TaskId taskId);

    List<Task> getList();

    void delete(TaskId taskId);

    void complete(TaskId taskId);

    void revert(TaskId taskId);

    void updateStatus(TaskId taskId, com.kamiokaweb.redo.model.task.TaskStatus status);

    void updateBillingDate(TaskId taskId, LocalDate billingDate);
}
