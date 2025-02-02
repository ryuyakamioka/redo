package com.kamiokaweb.redo.controller.task.io;

import com.kamiokaweb.redo.model.task.Task;

public record TaskRequest(
        Task task
) {}
