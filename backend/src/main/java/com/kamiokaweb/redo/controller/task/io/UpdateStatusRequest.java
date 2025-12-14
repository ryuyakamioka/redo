package com.kamiokaweb.redo.controller.task.io;

import com.kamiokaweb.redo.model.task.TaskStatus;

public record UpdateStatusRequest(
        TaskStatus status
) {}
