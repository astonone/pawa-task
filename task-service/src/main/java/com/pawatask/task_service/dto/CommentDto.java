package com.pawatask.task_service.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private String author;
}
