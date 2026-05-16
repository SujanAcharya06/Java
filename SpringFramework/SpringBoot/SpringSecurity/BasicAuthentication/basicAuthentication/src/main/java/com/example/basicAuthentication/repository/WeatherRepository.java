package com.example.basicAuthentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.basicAuthentication.entity.Weather;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long>{

}
