package com.internship.virtual.internship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.internship.virtual.internship.model.Manager;
import com.internship.virtual.internship.repository.ManagerRepository;

@Service
public class ManagerService {
    
    @Autowired
    private ManagerRepository managerRepository;
    
    public Manager findByUsername(String username) {
        return managerRepository.findByUsername(username);
    }
    
    public Manager validateManager(String username, String password) {
        return managerRepository.findByUsernameAndPassword(username, password);
    }
    
    public Manager save(Manager manager) {
        return managerRepository.save(manager);
    }
}
