package com.internship.virtual.internship.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
        @GetMapping("/")
        public String home() {
            return "index";  // Loads templates/index.html
        }
    
        @GetMapping("/role-selection")
        public String roleSelection() {
        return "role-selection";
        }
    }