package com.example.Spring_data_jpa_demo;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.Spring_data_jpa_demo.model.Student;
import com.example.Spring_data_jpa_demo.repo.StudentRepo;

@SpringBootApplication
public class SpringDataJpaDemoApplication {

	public static void main(String[] args) {
		ApplicationContext appContext = SpringApplication.run(SpringDataJpaDemoApplication.class, args);

		StudentRepo studentRepo = appContext.getBean(StudentRepo.class);

		// Student s1 = appContext.getBean(Student.class);
		// Student s2 = appContext.getBean(Student.class);
		// Student s3 = appContext.getBean(Student.class);
		//
		// s1.setRollNo(101);
		// s1.setName("Gojo");
		// s1.setMarks(99);

		List<Student> s = studentRepo.findByName("Gojo");
		System.out.println(s);

		// List<Student> s4 = studentRepo.findByMarksGreaterThan(96);
		// System.out.println(s4);

		// studentRepo.delete(s1);
	}

}
