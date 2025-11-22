package com.kamiokaweb.redo.repository.task;

import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;

import java.util.Optional;
import java.util.List;

public interface TaskRepository {

    void register(Task task);

    Optional<Task> get(TaskId taskId);

    List<Task> getList();

    void delete(TaskId taskId);

    void complete(TaskId taskId);

    void revert(TaskId taskId);
}
