package com.example.mappingDemo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mappingDemo.model.Laptop;

@Repository
public interface LaptopRepo extends JpaRepository<Laptop, Integer>{

}
