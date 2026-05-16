
package com.example.cachingdemo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cachingdemo.entity.Weather;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

	Optional<Weather> findByCity(String city);

	void deleteByCity(String city);
}
