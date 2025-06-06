package com.internship.virtual.internship.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.internship.virtual.internship.model.Student;
import com.internship.virtual.internship.repository.StudentRepository;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
    
    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
    
    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username);
    }
    
    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    
    public Student validateStudent(String username, String password) {
        return studentRepository.findByUsernameAndPassword(username, password);
    }
    
    public Student save(Student student) {
        return studentRepository.save(student);
    }
    
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}