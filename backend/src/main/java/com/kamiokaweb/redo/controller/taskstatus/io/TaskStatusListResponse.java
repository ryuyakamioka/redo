package com.kamiokaweb.redo.controller.taskstatus.io;

import com.kamiokaweb.redo.model.taskstatus.TaskStatusMaster;

import java.util.List;

public record TaskStatusListResponse(
        List<TaskStatusMaster> taskStatusList
) {
    public static TaskStatusListResponse of(List<TaskStatusMaster> taskStatusList) {
        return new TaskStatusListResponse(taskStatusList);
    }
}
