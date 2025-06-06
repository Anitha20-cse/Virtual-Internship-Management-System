package com.internship.virtual.internship.model;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private String username;
    private String password;
    private String college;
    private String course;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentInternship> studentInternships = new ArrayList<>();
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks = new ArrayList<>();
    
    public Student() {
    }
    
    public Student(String name, String email, String username, String password, String college, String course) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.college = college;
        this.course = course;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getCollege() {
        return college;
    }
    
    public void setCollege(String college) {
        this.college = college;
    }
    
    public String getCourse() {
        return course;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    
    public List<StudentInternship> getStudentInternships() {
        return studentInternships;
    }
    
    public void setStudentInternships(List<StudentInternship> studentInternships) {
        this.studentInternships = studentInternships;
    }
    
    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }
    
    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
    
    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", email=" + email + ", username=" + username + ", college=" + college + ", course=" + course + "]";
    }
}