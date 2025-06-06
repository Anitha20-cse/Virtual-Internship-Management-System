package com.internship.virtual.internship.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.internship.virtual.internship.model.Internship;
import com.internship.virtual.internship.model.Student;
import com.internship.virtual.internship.model.StudentInternship;

@Repository
public interface StudentInternshipRepository extends JpaRepository<StudentInternship, Long> {
    List<StudentInternship> findByStudent(Student student);
    List<StudentInternship> findByInternship(Internship internship);
    StudentInternship findByStudentAndInternship(Student student, Internship internship);
}
