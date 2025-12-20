package com.kamiokaweb.redo.infrastructure.freee;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("freee_tokens")
public record FreeeTokenDto(
        @Id Long id,
        String accessToken,
        String refreshToken,
        String tokenType,
        LocalDateTime expiresAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public FreeeTokenDto(
            String accessToken,
            String refreshToken,
            String tokenType,
            LocalDateTime expiresAt
    ) {
        this(null, accessToken, refreshToken, tokenType, expiresAt, LocalDateTime.now(), LocalDateTime.now());
    }
}
