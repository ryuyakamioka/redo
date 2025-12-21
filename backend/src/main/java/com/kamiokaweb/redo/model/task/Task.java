package com.kamiokaweb.redo.model.task;

import com.kamiokaweb.redo.model.client.Client;
import com.kamiokaweb.redo.model.taskitem.TaskItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record Task(
        TaskId taskId,
        TaskTitle taskTitle,
        TaskStatus taskStatus,
        @Schema(description = "依頼日") LocalDate requestDate,
        @Schema(description = "依頼人") Client client,
        @Schema(description = "希望記入欄（メモ）") String note,
        @Schema(description = "依頼明細") List<TaskItem> taskItems,
        @Schema(description = "着手予定日") LocalDate expectedDeliveryDate,
        @Schema(description = "納品日") LocalDate deliveryDate,
        @Schema(description = "請求日") LocalDate billingDate
) {}
