package com.kmgtechnologies.taskmanagement.controller;

import com.kmgtechnologies.taskmanagement.model.Task;
import com.kmgtechnologies.taskmanagement.model.TaskStatus;
import com.kmgtechnologies.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
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
    public String createTask(
            @Valid @ModelAttribute("task") Task task,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", TaskStatus.values());
            return "tasks/form";
        }

        taskService.save(task);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Tâche créée avec succès !"
        );

        return "redirect:/tasks";
    }

    // Afficher le formulaire de modification
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Task task = getTaskOrThrow(id);

        model.addAttribute("task", task);
        model.addAttribute("statuses", TaskStatus.values());

        return "tasks/form";
    }

    // Enregistrer les modifications
    @PostMapping("/{id}")
    public String updateTask(
            @PathVariable Long id,
            @Valid @ModelAttribute("task") Task formTask,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        Task existingTask = getTaskOrThrow(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", TaskStatus.values());
            return "tasks/form";
        }

        existingTask.setTitle(formTask.getTitle());
        existingTask.setDescription(formTask.getDescription());
        existingTask.setDueDate(formTask.getDueDate());
        existingTask.setStatus(formTask.getStatus());

        taskService.save(existingTask);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Tâche modifiée avec succès !"
        );

        return "redirect:/tasks";
    }

    // Supprimer une tâche
    @PostMapping("/{id}/delete")
    public String deleteTask(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        getTaskOrThrow(id);
        taskService.deleteById(id);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Tâche supprimée avec succès !"
        );

        return "redirect:/tasks";
    }

    // Gérer les identifiants inexistants
    private Task getTaskOrThrow(Long id) {
        return taskService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tâche introuvable : " + id
                ));
    }
}