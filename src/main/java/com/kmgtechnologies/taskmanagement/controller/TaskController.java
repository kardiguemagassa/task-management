
package com.kmgtechnologies.taskmanagement.controller;

import com.kmgtechnologies.taskmanagement.model.Task;
import com.kmgtechnologies.taskmanagement.model.TaskStatus;
import com.kmgtechnologies.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final MessageSource messageSource;

    public TaskController(TaskService taskService, MessageSource messageSource) {
        this.taskService = taskService;
        this.messageSource = messageSource;
    }

    // Afficher toutes les tâches
    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    // Afficher le formulaire de création
    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("task", new Task());
        model.addAttribute("statuses", TaskStatus.values());

        return "tasks/form";
    }

    // Enregistrer une nouvelle tâche
    @PostMapping
    public String createTask(@Valid @ModelAttribute("task") Task task, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, Locale locale) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", TaskStatus.values());
            return "tasks/form";
        }

        taskService.save(task);

        String successMessage = messageSource.getMessage("task.success.created", null, locale);

        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return "redirect:/tasks";
    }

    // Afficher le formulaire de modification
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {

        Task task = getTaskOrThrow(id);

        model.addAttribute("taskId", id);
        model.addAttribute("task", task);
        model.addAttribute("statuses", TaskStatus.values());

        return "tasks/form";
    }

    // Enregistrer les modifications
    @PostMapping("/{id}")
    public String updateTask(@PathVariable Long id, @Valid @ModelAttribute("task") Task formTask, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, Locale locale) {

        Task existingTask = getTaskOrThrow(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("taskId", id);
            model.addAttribute("statuses", TaskStatus.values());
            return "tasks/form";
        }

        existingTask.setTitle(formTask.getTitle());
        existingTask.setDescription(formTask.getDescription());
        existingTask.setDueDate(formTask.getDueDate());
        existingTask.setStatus(formTask.getStatus());

        taskService.save(existingTask);

        String successMessage = messageSource.getMessage("task.success.updated", null, locale);

        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return "redirect:/tasks";
    }

    // Supprimer une tâche
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes, Locale locale) {

        getTaskOrThrow(id);
        taskService.deleteById(id);

        String successMessage = messageSource.getMessage("task.success.deleted", null, locale);

        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return "redirect:/tasks";
    }

    // Gérer les identifiants inexistants
    private Task getTaskOrThrow(Long id) {

        return taskService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tâche introuvable : " + id));
    }
}
