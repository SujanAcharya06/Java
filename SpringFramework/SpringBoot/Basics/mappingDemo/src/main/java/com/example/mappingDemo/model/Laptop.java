package com.example.mappingDemo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Laptop{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lid;
    private String brand;
    private String model;

    @ManyToMany(mappedBy = "laptops")
    private List<Student> students;

    public Laptop() {
    }

    public Laptop(Integer lid, String brand, String model) {
        this.lid = lid;
        this.brand = brand;
        this.model = model;
    }

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
