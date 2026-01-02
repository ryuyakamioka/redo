package com.kamiokaweb.redo.repository.taskstatus;

import com.kamiokaweb.redo.infrastructure.taskstatus.TaskStatusAccessor;
import com.kamiokaweb.redo.infrastructure.taskstatus.TaskStatusDto;
import com.kamiokaweb.redo.model.taskstatus.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskStatusRepositoryImpl implements TaskStatusRepository {
    private final TaskStatusAccessor taskStatusAccessor;

    public TaskStatusRepositoryImpl(TaskStatusAccessor taskStatusAccessor) {
        this.taskStatusAccessor = taskStatusAccessor;
    }

    @Override
    public Optional<TaskStatusMaster> get(TaskStatusId taskStatusId) {
        return taskStatusAccessor.findById(taskStatusId.value())
                .map(TaskStatusDto::from);
    }

    @Override
    public Optional<TaskStatusMaster> getByCode(TaskStatusCode taskStatusCode) {
        return taskStatusAccessor.findByCode(taskStatusCode.value())
                .map(TaskStatusDto::from);
    }

    @Override
    public List<TaskStatusMaster> getList() {
        return taskStatusAccessor.findAllByOrderByDisplayOrderAsc().stream()
                .map(TaskStatusDto::from)
                .toList();
    }

    @Override
    public TaskStatusMaster save(TaskStatusMaster taskStatusMaster) {
        TaskStatusDto dto = new TaskStatusDto(
                taskStatusMaster.taskStatusId() != null ? taskStatusMaster.taskStatusId().value() : null,
                taskStatusMaster.taskStatusCode().value(),
                taskStatusMaster.taskStatusName().value(),
                taskStatusMaster.displayColor().value(),
                taskStatusMaster.displayOrder().value(),
                taskStatusMaster.isCompleted(),
                null
        );
        TaskStatusDto saved = taskStatusAccessor.save(dto);
        return saved.from();
    }

    @Override
    public void delete(TaskStatusId taskStatusId) {
        taskStatusAccessor.deleteById(taskStatusId.value());
    }
}
