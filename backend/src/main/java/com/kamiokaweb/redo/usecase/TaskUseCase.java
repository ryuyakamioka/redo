package com.kamiokaweb.redo.usecase;

import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;
import com.kamiokaweb.redo.repository.task.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public void revert(TaskId taskId) {
        taskRepository.revert(taskId);
    }

    public void updateStatus(TaskId taskId, com.kamiokaweb.redo.model.task.TaskStatus status) {
        taskRepository.updateStatus(taskId, status);
    }

    public void updateBillingDate(TaskId taskId, LocalDate billingDate) {
        taskRepository.updateBillingDate(taskId, billingDate);
    }

    public void updateBillingDates(List<Long> taskIds, LocalDate billingDate) {
        taskIds.forEach(taskId -> updateBillingDate(new TaskId(taskId), billingDate));
    }
}
