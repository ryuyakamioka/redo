package com.kamiokaweb.redo.usecase;

import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;
import com.kamiokaweb.redo.repository.task.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskUseCase {
    private final TaskRepository taskRepository;

    public TaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task get(TaskId taskId) {
        return taskRepository.get(taskId).orElseThrow();
    }

    public void register(Task task) {
        taskRepository.register(task);
    }
}
