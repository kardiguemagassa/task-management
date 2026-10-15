
package com.kmgtechnologies.taskmanagement.controller;

import com.kmgtechnologies.taskmanagement.model.Task;
import com.kmgtechnologies.taskmanagement.model.TaskStatus;
import com.kmgtechnologies.taskmanagement.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private MessageSource messageSource;

    private MockMvc mockMvc;

    private Task task;

    @BeforeEach
    void setUp() {
        TaskController controller =
                new TaskController(taskService, messageSource);

        LocalValidatorFactoryBean validator =
                new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setValidator(validator)
                .build();

        task = new Task();
        task.setId(1L);
        task.setTitle("Apprendre Spring Boot");
        task.setDescription("Tester le contrôleur");
        task.setStatus(TaskStatus.TO_DO);
    }

    @Test
    void listTasks_shouldReturnTaskList() throws Exception {
        when(taskService.findAll()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/list"))
                .andExpect(model().attributeExists("tasks"));

        verify(taskService).findAll();
    }

    @Test
    void showCreateForm_shouldReturnForm() throws Exception {
        mockMvc.perform(get("/tasks/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/form"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void createTask_shouldSaveAndRedirect() throws Exception {
        when(messageSource.getMessage(
                eq("task.success.created"),
                isNull(),
                any(Locale.class)
        )).thenReturn("Tâche créée avec succès !");

        mockMvc.perform(post("/tasks")
                        .param("title", "Nouvelle tâche")
                        .param("description", "Description")
                        .param("status", "TO_DO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"))
                .andExpect(flash().attribute(
                        "successMessage",
                        "Tâche créée avec succès !"
                ));

        verify(taskService).save(any(Task.class));
    }

    @Test
    void createTask_shouldReturnFormWhenTitleIsBlank() throws Exception {
        mockMvc.perform(post("/tasks")
                        .param("title", "")
                        .param("status", "TO_DO"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/form"))
                .andExpect(model().attributeHasFieldErrors(
                        "task", "title"
                ));

        verify(taskService, never()).save(any(Task.class));
    }

    @Test
    void showEditForm_shouldReturnExistingTask() throws Exception {
        when(taskService.findById(1L))
                .thenReturn(Optional.of(task));

        mockMvc.perform(get("/tasks/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/form"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("taskId", 1L))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void showEditForm_shouldReturn404WhenTaskDoesNotExist()
            throws Exception {

        when(taskService.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/99/edit"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTask_shouldSaveAndRedirect() throws Exception {
        when(taskService.findById(1L))
                .thenReturn(Optional.of(task));

        when(messageSource.getMessage(
                eq("task.success.updated"),
                isNull(),
                any(Locale.class)
        )).thenReturn("Tâche modifiée avec succès !");

        mockMvc.perform(post("/tasks/1")
                        .param("title", "Tâche modifiée")
                        .param("description", "Nouvelle description")
                        .param("status", "IN_PROGRESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"))
                .andExpect(flash().attribute(
                        "successMessage",
                        "Tâche modifiée avec succès !"
                ));

        verify(taskService).save(argThat(savedTask ->
                savedTask.getId().equals(1L)
                        && savedTask.getTitle().equals("Tâche modifiée")
                        && savedTask.getStatus() == TaskStatus.IN_PROGRESS
        ));
    }

    @Test
    void updateTask_shouldReturnFormWhenTitleIsBlank()
            throws Exception {

        when(taskService.findById(1L))
                .thenReturn(Optional.of(task));

        mockMvc.perform(post("/tasks/1")
                        .param("title", "")
                        .param("status", "TO_DO"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/form"))
                .andExpect(model().attributeHasFieldErrors(
                        "task", "title"
                ))
                .andExpect(model().attribute("taskId", 1L));

        verify(taskService, never()).save(any(Task.class));
    }

    @Test
    void updateTask_shouldReturn404WhenTaskDoesNotExist()
            throws Exception {

        when(taskService.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/tasks/99")
                        .param("title", "Nouvelle tâche")
                        .param("status", "TO_DO"))
                .andExpect(status().isNotFound());

        verify(taskService, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_shouldDeleteAndRedirect() throws Exception {
        when(taskService.findById(1L))
                .thenReturn(Optional.of(task));

        when(messageSource.getMessage(
                eq("task.success.deleted"),
                isNull(),
                any(Locale.class)
        )).thenReturn("Tâche supprimée avec succès !");

        mockMvc.perform(post("/tasks/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"))
                .andExpect(flash().attribute(
                        "successMessage",
                        "Tâche supprimée avec succès !"
                ));

        verify(taskService).deleteById(1L);
    }

    @Test
    void deleteTask_shouldReturn404WhenTaskDoesNotExist()
            throws Exception {

        when(taskService.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/tasks/99/delete"))
                .andExpect(status().isNotFound());

        verify(taskService, never()).deleteById(anyLong());
    }
}
