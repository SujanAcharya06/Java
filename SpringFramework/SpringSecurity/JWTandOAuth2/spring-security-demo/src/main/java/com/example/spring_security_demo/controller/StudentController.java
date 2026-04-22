package com.example.spring_security_demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_security_demo.model.Student;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class StudentController {

	List<Student> students = new ArrayList<>(List.of(
			new Student(1, "Yuji", "Blockchain"),
			new Student(2, "Megumi", "Java")));

	@GetMapping("students")
	public List<Student> getStudents() {
		return students;
	}

	@PostMapping("student")
	public void addStudent(@RequestBody Student student) {
		students.add(student);
	}

	@GetMapping("/csrf-token")
	public CsrfToken getCsrfToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}

}
