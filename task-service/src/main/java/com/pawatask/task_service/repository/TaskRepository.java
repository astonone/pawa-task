package com.pawatask.task_service.repository;

import com.pawatask.task_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByOrderByIsDoneAscTodoDateAsc();
}
