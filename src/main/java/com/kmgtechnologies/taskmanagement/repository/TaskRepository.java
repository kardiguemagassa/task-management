package com.kmgtechnologies.taskmanagement.repository;

import com.kmgtechnologies.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}