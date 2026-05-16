
package com.example.cachingdemo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cachingdemo.entity.Weather;
import com.example.cachingdemo.service.CacheInspectionService;
import com.example.cachingdemo.service.WeatherService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class WeatherController {

	@Autowired
	private WeatherService weatherService;

	@Autowired
	private CacheInspectionService cacheInspectionService;

	@GetMapping("/allWeather")
	public ResponseEntity<List<Weather>> getAllWeahter() {
		List<Weather> weather = weatherService.getAllWeather();
		if (weather != null && !weather.isEmpty()) {
			return ResponseEntity.ok(weather);
		}
		return new ResponseEntity<>(weather, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/weather")
	public ResponseEntity<?> getWeather(@RequestParam String city) {
		Optional<Weather> weather = weatherService.getWeather(city);
		if (weather.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
		}
		return ResponseEntity.ok(weather.get());
	}

	@PostMapping("/weather")
	public ResponseEntity<?> addWeather(@RequestBody Weather weather) {
		Weather weather2 = weatherService.addWeather(weather);
		if (weather2 == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not add Resource");
		}
		return new ResponseEntity<>(weather2, HttpStatus.CREATED);
	}

	@PutMapping("/weather/{city}")
	public ResponseEntity<?> updateWeather(@PathVariable String city, @RequestParam String updatedWeather) {
		log.info("PathVariable: {}, RequestParam: {}", city, updatedWeather);
		Optional<Weather> weather3 = weatherService.updateWeather(city, updatedWeather);
		if (weather3 == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not modify Resource");
		}
		return new ResponseEntity<>(weather3.get(), HttpStatus.CREATED);

	}

	@DeleteMapping("/weather")
	public ResponseEntity<?> deleteWeather(@RequestParam String city) {
		boolean deleted = weatherService.deleteWeather(city);
		if (!deleted) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not delete resouce");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Resouce delete successfully");
	}

	@GetMapping("/cacheData")
	public String getData() {
		return cacheInspectionService.printCacheContent("weather");
	}

}
