package com.kamiokaweb.redo.infrastructure;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("users")
public record UserDto(
        @Id
        Long id,
        String name,
        String email,
        LocalDateTime createdAt
) {}
