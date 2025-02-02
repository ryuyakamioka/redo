package com.kamiokaweb.redo.model.task;

public record Task(
        TaskId taskId,
        TaskTitle taskTitle,
        TaskStatus taskStatus
) {}
