package com.kamiokaweb.redo.controller.taskstatus.io;

import com.kamiokaweb.redo.model.taskstatus.TaskStatusMaster;

public record TaskStatusResponse(
        TaskStatusMaster taskStatus
) {
    public static TaskStatusResponse of(TaskStatusMaster taskStatus) {
        return new TaskStatusResponse(taskStatus);
    }
}
