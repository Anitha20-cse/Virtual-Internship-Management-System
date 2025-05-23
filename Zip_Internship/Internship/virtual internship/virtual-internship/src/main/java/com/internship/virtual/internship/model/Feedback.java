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
@Table(name = "feedbacks")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;
    
    private String content;
    private LocalDateTime submissionDate;
    
    public Feedback() {
        this.submissionDate = LocalDateTime.now();
    }
    
    public Feedback(Student student, Internship internship, String content) {
        this.student = student;
        this.internship = internship;
        this.content = content;
        this.submissionDate = LocalDateTime.now();
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
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }
    
    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    @Override
    public String toString() {
        return "Feedback [id=" + id + ", content=" + content + ", submissionDate=" + submissionDate + "]";
    }
}
