package com.kamiokaweb.redo.usecase;

import com.kamiokaweb.redo.model.taskstatus.TaskStatusCode;
import com.kamiokaweb.redo.model.taskstatus.TaskStatusId;
import com.kamiokaweb.redo.model.taskstatus.TaskStatusMaster;
import com.kamiokaweb.redo.repository.taskstatus.TaskStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskStatusUseCase {
    private final TaskStatusRepository taskStatusRepository;

    public TaskStatusUseCase(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    public List<TaskStatusMaster> getList() {
        return taskStatusRepository.getList();
    }

    public Optional<TaskStatusMaster> getByCode(String code) {
        return taskStatusRepository.getByCode(new TaskStatusCode(code));
    }

    public TaskStatusMaster create(TaskStatusMaster taskStatusMaster) {
        return taskStatusRepository.save(taskStatusMaster);
    }

    public TaskStatusMaster update(TaskStatusMaster taskStatusMaster) {
        return taskStatusRepository.save(taskStatusMaster);
    }

    public void delete(Long taskStatusId) {
        taskStatusRepository.delete(new TaskStatusId(taskStatusId));
    }
}
