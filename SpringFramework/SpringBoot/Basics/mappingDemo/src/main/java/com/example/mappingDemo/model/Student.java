package com.example.mappingDemo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Student{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer sid;

    private String sname;

    private String tech;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "student_laptop",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "laptop_id")
    )
    private List<Laptop> laptops;

    public Student() {

    }

    public Student(Integer sid, String sname, String tech, List<Laptop> laptops) {
        this.sid = sid;
        this.sname = sname;
        this.tech = tech;
        this.laptops = laptops;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getTech() {
        return tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }

    public List<Laptop> getLaptops() {
        return laptops;
    }

    public void setLaptops(List<Laptop> laptops) {
        this.laptops = laptops;
    }
}
