package com.internship.virtual.internship.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.internship.virtual.internship.model.Domain;
import com.internship.virtual.internship.model.Internship;
import com.internship.virtual.internship.model.Manager;
import com.internship.virtual.internship.model.Task;
import com.internship.virtual.internship.service.DomainService;
import com.internship.virtual.internship.service.InternshipService;
import com.internship.virtual.internship.service.ManagerService;
import com.internship.virtual.internship.service.StudentInternshipService;
import com.internship.virtual.internship.service.FeedbackService;
import com.internship.virtual.internship.service.TaskService;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    
    @Autowired
    private ManagerService managerService;
    
    @Autowired
    private DomainService domainService;
    
    @Autowired
    private InternshipService internshipService;
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private StudentInternshipService studentInternshipService;

    @Autowired
    private FeedbackService feedbackService;
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, 
            HttpSession session, RedirectAttributes redirectAttributes) {
        
        System.out.println("Attempting login for username: " + username);
        Manager manager = managerService.validateManager(username, password);
        System.out.println("Manager found: " + (manager != null));
        
        if (manager != null) {
            session.setAttribute("managerId", manager.getId());
            session.setAttribute("managerUsername", manager.getUsername());
            return "redirect:/manager/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/manager/login";
        }
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("managerId") == null) {
            return "redirect:/manager/login";
        }
        
        List<Domain> domains = domainService.findAll();
        model.addAttribute("domains", domains);
        model.addAttribute("studentInternships", studentInternshipService.findAll());
        model.addAttribute("feedbacks", feedbackService.findAll());

        return "dashboard";
    }
    
    @PostMapping("/add-domain")
    public String addDomain(@RequestParam String name, @RequestParam String description, 
            HttpSession session, RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("managerId") == null) {
            return "redirect:/manager/login";
        }
        
        Domain existingDomain = domainService.findByName(name);
        
        if (existingDomain != null) {
            redirectAttributes.addFlashAttribute("error", "Domain with this name already exists");
            return "redirect:/manager/dashboard";
        }
        
        Domain domain = new Domain(name, description);
        domainService.save(domain);
        
        redirectAttributes.addFlashAttribute("success", "Domain added successfully");
        return "redirect:/manager/dashboard";
    }
    
    @PostMapping("/add-internship")
    public String addInternship(@RequestParam String domainName, 
            @RequestParam String name, @RequestParam String description, 
            @RequestParam String duration, HttpSession session, 
            RedirectAttributes redirectAttributes) {
        try {
            if (session.getAttribute("managerId") == null) {
                return "redirect:/manager/login";
            }
            
            Domain domain = domainService.findByName(domainName);
            
            if (domain == null) {
                redirectAttributes.addFlashAttribute("error", "Domain not found");
                return "redirect:/manager/dashboard";
            }
            
            Internship existingInternship = internshipService.findByNameAndDomain(name, domain);
            
            if (existingInternship != null) {
                redirectAttributes.addFlashAttribute("error", "Internship with this name already exists in the selected domain");
                return "redirect:/manager/dashboard";
            }
            
            Internship internship = new Internship(name, description, duration);
            internship.setDomain(domain);
            internshipService.save(internship);
            
            redirectAttributes.addFlashAttribute("success", "Internship added successfully");
            return "redirect:/manager/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding internship: " + e.getMessage());
            return "redirect:/manager/dashboard";
        }
    }
    
    @PostMapping("/add-task")
    public String addTask(@RequestParam String domainName, @RequestParam String internshipName, 
            @RequestParam String name, @RequestParam String description, 
            HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            if (session.getAttribute("managerId") == null) {
                return "redirect:/manager/login";
            }
            
            Domain domain = domainService.findByName(domainName);
            
            if (domain == null) {
                redirectAttributes.addFlashAttribute("error", "Domain not found");
                return "redirect:/manager/dashboard";
            }
            
            Internship internship = internshipService.findByNameAndDomain(internshipName, domain);
            
            if (internship == null) {
                redirectAttributes.addFlashAttribute("error", "Internship not found");
                return "redirect:/manager/dashboard";
            }
            
            Task task = new Task(name, description);
            task.setInternship(internship);
            taskService.save(task);
            
            redirectAttributes.addFlashAttribute("success", "Task added successfully");
            return "redirect:/manager/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding task: " + e.getMessage());
            return "redirect:/manager/dashboard";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
