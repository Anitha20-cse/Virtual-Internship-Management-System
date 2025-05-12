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

import com.internship.virtual.internship.model.Domain;
import com.internship.virtual.internship.model.Internship;
import com.internship.virtual.internship.model.Task;
import com.internship.virtual.internship.service.DomainService;
import com.internship.virtual.internship.service.InternshipService;
import com.internship.virtual.internship.service.TaskService;

@RestController
@RequestMapping("/api/internships")
public class InternshipController {
    
    @Autowired
    private InternshipService internshipService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private DomainService domainService;
    
    @GetMapping
    public ResponseEntity<List<Internship>> getAllInternships() {
        List<Internship> internships = internshipService.findAll();
        return ResponseEntity.ok(internships);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Internship> getInternshipById(@PathVariable Long id) {
        Internship internship = internshipService.findById(id);
        if (internship != null) {
            return ResponseEntity.ok(internship);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getTasksByInternship(@PathVariable Long id) {
        Internship internship = internshipService.findById(id);
        if (internship != null) {
            List<Task> tasks = taskService.findByInternship(internship);
            return ResponseEntity.ok(tasks);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Internship> addInternship(@RequestParam String name,
                                                    @RequestParam String description,
                                                    @RequestParam String duration,
                                                    @RequestParam Long domainId) {
        Domain domain = domainService.findById(domainId);
        if (domain == null) {
            return ResponseEntity.badRequest().build();
        }
        Internship internship = new Internship();
        internship.setName(name);
        internship.setDescription(description);
        internship.setDuration(duration);
        internship.setDomain(domain);
        Internship savedInternship = internshipService.save(internship);
        return ResponseEntity.ok(savedInternship);
    }
}
