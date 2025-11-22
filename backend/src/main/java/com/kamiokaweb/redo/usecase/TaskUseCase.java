package com.kamiokaweb.redo.usecase;

import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;
import com.kamiokaweb.redo.repository.task.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskUseCase {
    private final TaskRepository taskRepository;

    public TaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task get(TaskId taskId) {
        return taskRepository.get(taskId).orElseThrow();
    }

    public List<Task> getList() {
        return taskRepository.getList();
    }

    public void register(Task task) {
        taskRepository.register(task);
    }

    public void update(Task task) {
        // 既存のタスク明細を削除してから新しいものを登録
        taskRepository.register(task);
    }

    public void delete(TaskId taskId) {
        taskRepository.delete(taskId);
    }

    public void complete(TaskId taskId) {
        taskRepository.complete(taskId);
    }
}
