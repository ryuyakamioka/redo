package com.kamiokaweb.redo.repository.task;

import com.kamiokaweb.redo.infrastructure.task.TaskAccessor;
import com.kamiokaweb.redo.infrastructure.task.TaskDto;
import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    @Override
    public List<Task> getList() {
        return StreamSupport.stream(taskAccessor.findAll().spliterator(), false)
                .map(TaskDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(TaskId taskId) {
        var task = taskAccessor.findById(taskId.value());
        taskAccessor.delete(task.get());
    }
}
