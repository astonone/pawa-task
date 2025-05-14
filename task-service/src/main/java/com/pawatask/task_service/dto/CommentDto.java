package com.pawatask.task_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    @NotBlank(message = "Text is required")
    private String text;
    @NotBlank(message = "Author is required")
    private String author;
    private LocalDateTime createdAt;
}
