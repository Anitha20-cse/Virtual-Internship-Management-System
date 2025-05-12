package com.internship.virtual.internship.controller;

import java.io.File;
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
import com.internship.virtual.internship.model.Feedback;
import com.internship.virtual.internship.model.Internship;
import com.internship.virtual.internship.model.Student;
import com.internship.virtual.internship.model.StudentInternship;
import com.internship.virtual.internship.service.DomainService;
import com.internship.virtual.internship.service.FeedbackService;
import com.internship.virtual.internship.service.InternshipService;
import com.internship.virtual.internship.service.StudentInternshipService;
import com.internship.virtual.internship.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private DomainService domainService;
    
    @Autowired
    private InternshipService internshipService;
    
    @Autowired
    private StudentInternshipService studentInternshipService;
    
    @Autowired
    private FeedbackService feedbackService;
    
    @GetMapping("/soptions")
    public String studentOptions() {
        return "soption";
    }
    
    @GetMapping("/type")
    public String studentType() {
        return "studentType";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "slogin";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, 
            HttpSession session, RedirectAttributes redirectAttributes) {
        
        Student student = studentService.validateStudent(username, password);
        
        if (student != null) {
            session.setAttribute("studentId", student.getId());
            session.setAttribute("studentUsername", student.getUsername());
            return "redirect:/student/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/student/slogin";
        }
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "sregister";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email, 
            @RequestParam String username, @RequestParam String password, 
            @RequestParam String college, @RequestParam String course,
            RedirectAttributes redirectAttributes) {
        
        Student existingStudent = studentService.findByUsername(username);
        
        if (existingStudent != null) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/student/register";
        }
        
        existingStudent = studentService.findByEmail(email);
        
        if (existingStudent != null) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:sregister.html";
        }
        
        Student student = new Student(name, email, username, password, college, course);
        studentService.save(student);
        
        redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
        return "redirect:/student/login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("studentId") == null) {
            return "redirect:/student/login";
        }
        
        Long studentId = (Long) session.getAttribute("studentId");
        Student student = studentService.findById(studentId);
        
        List<StudentInternship> studentInternships = studentInternshipService.findByStudent(student);
        
        model.addAttribute("student", student);
        model.addAttribute("registeredInternships", studentInternships);
        
        return "sdashboard";
    }
    
    @GetMapping("/view-internships")
    public String viewInternships(HttpSession session, Model model) {
        if (session.getAttribute("studentId") == null) {
            return "redirect:/student/login";
        }
        
        List<Domain> domains = domainService.findAll();
        model.addAttribute("domains", domains);
        
        return "sview";
    }
    
    @GetMapping("/public-internships")
    public String publicInternships(Model model) {
        List<Internship> internships = internshipService.findAll();
        model.addAttribute("internships", internships);
        return "newStudentInternship";
    }
    
    @PostMapping("/register-internship")
    public String registerInternship(@RequestParam Long internshipId, 
            HttpSession session, RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("studentId") == null) {
            return "redirect:/student/login";
        }
        
        Long studentId = (Long) session.getAttribute("studentId");
        Student student = studentService.findById(studentId);
        Internship internship = internshipService.findById(internshipId);
        
        if (internship == null) {
            redirectAttributes.addFlashAttribute("error", "Internship not found");
            return "redirect:/student/view-internships";
        }
        
        StudentInternship existingRegistration = studentInternshipService.findByStudentAndInternship(student, internship);
        
        if (existingRegistration != null) {
            redirectAttributes.addFlashAttribute("error", "You have already registered for this internship");
            return "redirect:/student/view-internships";
        }
        
        StudentInternship studentInternship = new StudentInternship(student, internship);
        studentInternshipService.save(studentInternship);
        
        redirectAttributes.addFlashAttribute("success", "Internship registration successful");
        return "redirect:/student/dashboard";
    }
    
    @PostMapping("/submit-task")
    public String submitTask(@RequestParam Long studentInternshipId, 
            @RequestParam String fileName, HttpSession session, 
            RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("studentId") == null) {
            return "redirect:/student/login";
        }
        
        StudentInternship studentInternship = studentInternshipService.findById(studentInternshipId);
        
        if (studentInternship == null) {
            redirectAttributes.addFlashAttribute("error", "Registration not found");
            return "redirect:/student/dashboard";
        }
        
        // Check if file exists
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            redirectAttributes.addFlashAttribute("error", "File does not exist or is empty");
            return "redirect:/student/dashboard";
        }
        
        studentInternshipService.submitTask(studentInternship, fileName);
        
        redirectAttributes.addFlashAttribute("success", "Task submitted successfully");
        return "redirect:/student/dashboard";
    }
    
    @PostMapping("/submit-feedback")
    public String submitFeedback(@RequestParam Long internshipId, 
            @RequestParam String content, HttpSession session, 
            RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("studentId") == null) {
            return "redirect:/student/login";
        }
        
        Long studentId = (Long) session.getAttribute("studentId");
        Student student = studentService.findById(studentId);
        Internship internship = internshipService.findById(internshipId);
        
        if (internship == null) {
            redirectAttributes.addFlashAttribute("error", "Internship not found");
            return "redirect:/student/dashboard";
        }
        
        StudentInternship studentInternship = studentInternshipService.findByStudentAndInternship(student, internship);
        
        if (studentInternship == null) {
            redirectAttributes.addFlashAttribute("error", "You are not registered for this internship");
            return "redirect:/student/dashboard";
        }
        
        Feedback feedback = new Feedback(student, internship, content);
        feedbackService.save(feedback);
        
        redirectAttributes.addFlashAttribute("success", "Feedback submitted successfully");
        return "redirect:/student/dashboard";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/task-submission")
    public String taskSubmissionPage(HttpSession session) {
        if (session.getAttribute("studentId") == null) {
            return "redirect:/student/login";
        }
        return "stasksubmission";
    }

    @GetMapping("/feedback")
    public String feedbackPage(HttpSession session) {
        if (session.getAttribute("studentId") == null) {
            return "redirect:/student/login";
        }
        return "sfeedback";
    }
}