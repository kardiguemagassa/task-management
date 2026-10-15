package com.kmgtechnologies.taskmanagement.service;

import com.kmgtechnologies.taskmanagement.model.Task;
import com.kmgtechnologies.taskmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Récupérer toutes les tâches
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    // Récupérer une tâche par son identifiant
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    // Créer ou modifier une tâche
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    // Supprimer une tâche par son identifiant
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    // Vérifier si une tâche existe
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }
}