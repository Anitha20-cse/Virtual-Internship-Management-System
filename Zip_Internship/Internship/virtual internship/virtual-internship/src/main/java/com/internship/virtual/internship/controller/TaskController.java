package com.internship.virtual.internship.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.internship.virtual.internship.model.Internship;
import com.internship.virtual.internship.model.Task;
import com.internship.virtual.internship.service.InternshipService;
import com.internship.virtual.internship.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private InternshipService internshipService;
    
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/internship/{internshipId}")
    public ResponseEntity<List<Task>> getTasksByInternship(@PathVariable Long internshipId) {
        Internship internship = internshipService.findById(internshipId);
        if (internship != null) {
            List<Task> tasks = taskService.findByInternship(internship);
            return ResponseEntity.ok(tasks);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Task> addTask(@RequestParam String name,
                                        @RequestParam String description,
                                        @RequestParam Long internshipId) {
        Internship internship = internshipService.findById(internshipId);
        if (internship == null) {
            return ResponseEntity.badRequest().build();
        }
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setInternship(internship);
        Task savedTask = taskService.save(task);
        return ResponseEntity.ok(savedTask);
    }
}
