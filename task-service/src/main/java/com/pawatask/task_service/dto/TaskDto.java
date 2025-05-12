package com.pawatask.task_service.dto;

import com.pawatask.task_service.model.Priority;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String createdBy;
    private String lastEditedBy;
    private LocalDateTime todoDate;
    private Priority priority;
    private List<CommentDto> comments;
}
