package com.example.mappingDemo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mappingDemo.model.Student;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer>{

}
