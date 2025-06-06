package com.internship.virtual.internship.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.internship.virtual.internship.model.Internship;
import  com.internship.virtual.internship.model.Task;
import  com.internship.virtual.internship.repository.TaskRepository;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    public List<Task> findAll() {
        return taskRepository.findAll();
    }
    
    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }
    
    public List<Task> findByInternship(Internship internship) {
        return taskRepository.findByInternship(internship);
    }
    
    public Task save(Task task) {
        return taskRepository.save(task);
    }
    
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }
}