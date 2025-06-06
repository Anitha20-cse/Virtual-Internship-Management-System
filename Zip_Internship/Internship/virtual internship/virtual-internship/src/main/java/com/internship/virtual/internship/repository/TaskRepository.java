package com.internship.virtual.internship.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.internship.virtual.internship.model.Internship;
import  com.internship.virtual.internship.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByInternship(Internship internship);
}