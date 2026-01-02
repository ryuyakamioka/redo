package com.kamiokaweb.redo.model.taskstatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "依頼ステータス")
public record TaskStatusMaster(
        TaskStatusId taskStatusId,
        TaskStatusCode taskStatusCode,
        TaskStatusName taskStatusName,
        @Schema(description = "表示色") DisplayColor displayColor,
        @Schema(description = "表示順") DisplayOrder displayOrder,
        @Schema(description = "完了フラグ") Boolean isCompleted
) {
}
