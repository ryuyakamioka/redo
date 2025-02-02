package com.kamiokaweb.redo.repository.task;

import com.kamiokaweb.redo.infrastructure.task.TaskAccessor;
import com.kamiokaweb.redo.infrastructure.task.TaskDto;
import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private final TaskAccessor taskAccessor;

    public TaskRepositoryImpl(TaskAccessor taskAccessor) {
        this.taskAccessor = taskAccessor;
    }

    @Override
    public void register(Task task) {
        var dto = taskAccessor.findById(task.taskId().value());
        if (dto.isEmpty()) {
            taskAccessor.save(new TaskDto(task));
        }
    }

    @Override
    public Optional<Task> get(TaskId taskId) {
        var dto = taskAccessor.findById(taskId.value());
        return dto.map(TaskDto::from);
    }
}
