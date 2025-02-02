package com.kamiokaweb.redo.repository.task;

import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;

import java.util.Optional;

public interface TaskRepository {

    void register(Task task);

    Optional<Task> get(TaskId taskId);
}
