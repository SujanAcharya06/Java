package com.example.mappingDemo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.mappingDemo.model.Laptop;
import com.example.mappingDemo.model.Student;
import com.example.mappingDemo.repo.LaptopRepo;
import com.example.mappingDemo.repo.StudentRepo;

@SpringBootApplication
public class MappingDemoApplication implements CommandLineRunner{

    @Autowired
    StudentRepo studentRepo;

    @Autowired
    LaptopRepo laptopRepo;

	public static void main(String[] args) {
		SpringApplication.run(MappingDemoApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {

        Student s1 = new Student();
        s1.setSname("Navin");
        s1.setTech("Java");

        Student s2 = new Student();
        s2.setSname("Sam");
        s2.setTech("AI");

        Student s3 = new Student();
        s3.setSname("Joy");
        s3.setTech("Python");

        Laptop l1 = new Laptop();
        l1.setBrand("Asus");
        l1.setModel("rog");

        Laptop l2 = new Laptop();
        l2.setBrand("Dell");
        l2.setModel("XPS");

        Laptop l3 = new Laptop();
        l3.setBrand("Apple");
        l3.setModel("M3");

        // Setting up relationships
        s1.setLaptops(List.of(l1, l2));
        s2.setLaptops(List.of(l2, l3));
        s3.setLaptops(List.of(l1));

        l1.setStudents(List.of(s1, s3));
        l2.setStudents(List.of(s1, s2));
        l3.setStudents(List.of(s2));

        // Save both sides
        // laptopRepo.saveAll(List.of(l1, l2, l3));
        studentRepo.saveAll(List.of(s1, s2, s3));
    }

}
