package com.internship.virtual.internship.model;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_internships")
public class StudentInternship {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;
    
    private LocalDateTime registrationDate;
    private String status; // "Registered", "In Progress", "Completed"
    private String submissionFile;
    
    public StudentInternship() {
        this.registrationDate = LocalDateTime.now();
        this.status = "Registered";
    }
    
    public StudentInternship(Student student, Internship internship) {
        this.student = student;
        this.internship = internship;
        this.registrationDate = LocalDateTime.now();
        this.status = "Registered";
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public Internship getInternship() {
        return internship;
    }
    
    public void setInternship(Internship internship) {
        this.internship = internship;
    }
    
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getSubmissionFile() {
        return submissionFile;
    }
    
    public void setSubmissionFile(String submissionFile) {
        this.submissionFile = submissionFile;
    }
    
    @Override
    public String toString() {
        return "StudentInternship [id=" + id + ", registrationDate=" + registrationDate + ", status=" + status + "]";
    }
}