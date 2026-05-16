
package com.example.cachingdemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.cachingdemo.entity.Weather;
import com.example.cachingdemo.repo.WeatherRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherService {

	@Autowired
	private WeatherRepository weatherRepository;

	public List<Weather> getAllWeather() {
		return weatherRepository.findAll();
	}

	@Cacheable(value = "weather", key = "#city")
	public Optional<Weather> getWeather(String city) {
		log.info("Fetching from db for city: ,{}", city);
		return weatherRepository.findByCity(city);
	}

	public Weather addWeather(Weather weather) {
		return weatherRepository.save(weather);
	}

	@CachePut(value = "weather", key = "#city")
	public Optional<Weather> updateWeather(String city, String updatedWeather) {

		Optional<Weather> existingWeather = weatherRepository.findByCity(city);
		if (existingWeather.isEmpty()) {
			return Optional.empty();
		}
		Weather wetherToUpdate = existingWeather.get();
		wetherToUpdate.setForecast(updatedWeather);
		Weather savedWeather = weatherRepository.save(wetherToUpdate);
		return Optional.of(savedWeather);
	}

	@Transactional
	@CacheEvict(value = "weather", key = "#city")
	public boolean deleteWeather(String city) {
		if (weatherRepository.findByCity(city) == null) {
			return false;
		}

		weatherRepository.deleteByCity(city);
		return true;
	}

}
