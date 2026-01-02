package com.kamiokaweb.redo.controller.taskstatus.io;

import com.kamiokaweb.redo.model.taskstatus.TaskStatusMaster;

public record TaskStatusRequest(
        TaskStatusMaster taskStatus
) {
}
