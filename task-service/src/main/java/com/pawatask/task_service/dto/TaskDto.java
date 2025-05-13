package com.pawatask.task_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.pawatask.task_service.model.Priority;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private String createdBy;
    private String lastEditedBy;
    @NotNull(message = "Date is required")
    private LocalDateTime todoDate;
    @NotNull(message = "Priority is required")
    private Priority priority;
    private List<CommentDto> comments;
}
