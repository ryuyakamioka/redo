package com.kamiokaweb.redo.repository.taskstatus;

import com.kamiokaweb.redo.model.taskstatus.TaskStatusCode;
import com.kamiokaweb.redo.model.taskstatus.TaskStatusId;
import com.kamiokaweb.redo.model.taskstatus.TaskStatusMaster;

import java.util.List;
import java.util.Optional;

public interface TaskStatusRepository {
    Optional<TaskStatusMaster> get(TaskStatusId taskStatusId);
    Optional<TaskStatusMaster> getByCode(TaskStatusCode taskStatusCode);
    List<TaskStatusMaster> getList();
    TaskStatusMaster save(TaskStatusMaster taskStatusMaster);
    void delete(TaskStatusId taskStatusId);
}
