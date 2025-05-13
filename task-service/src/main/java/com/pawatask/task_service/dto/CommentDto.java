package com.pawatask.task_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private String author;
    private LocalDateTime createdAt;
}
