
package com.kmgtechnologies.taskmanagement.service;

import com.kmgtechnologies.taskmanagement.model.Task;
import com.kmgtechnologies.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Apprendre Spring Boot");
        task.setDescription("Écrire des tests unitaires");
    }

    @Test
    void findAll_shouldReturnAllTasks() {
        // Arrange
        when(taskRepository.findAll())
                .thenReturn(List.of(task));

        // Act
        List<Task> result = taskService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Apprendre Spring Boot", result.get(0).getTitle());

        verify(taskRepository).findAll();
    }

    @Test
    void findById_shouldReturnTaskWhenFound() {
        // Arrange
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // Act
        Optional<Task> result = taskService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Apprendre Spring Boot", result.get().getTitle());

        verify(taskRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        // Arrange
        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act
        Optional<Task> result = taskService.findById(99L);

        // Assert
        assertTrue(result.isEmpty());

        verify(taskRepository).findById(99L);
    }

    @Test
    void save_shouldReturnSavedTask() {
        // Arrange
        when(taskRepository.save(task))
                .thenReturn(task);

        // Act
        Task result = taskService.save(task);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Apprendre Spring Boot", result.getTitle());

        verify(taskRepository).save(task);
    }

    @Test
    void deleteById_shouldCallRepository() {
        // Act
        taskService.deleteById(1L);

        // Assert
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void existsById_shouldReturnTrueWhenTaskExists() {
        // Arrange
        when(taskRepository.existsById(1L))
                .thenReturn(true);

        // Act
        boolean result = taskService.existsById(1L);

        // Assert
        assertTrue(result);

        verify(taskRepository).existsById(1L);
    }

    @Test
    void existsById_shouldReturnFalseWhenTaskDoesNotExist() {
        // Arrange
        when(taskRepository.existsById(99L))
                .thenReturn(false);

        // Act
        boolean result = taskService.existsById(99L);

        // Assert
        assertFalse(result);

        verify(taskRepository).existsById(99L);
    }
}
