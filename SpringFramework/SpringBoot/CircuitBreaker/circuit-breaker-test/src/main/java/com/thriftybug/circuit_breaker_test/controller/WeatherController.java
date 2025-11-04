
package com.thriftybug.circuit_breaker_test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thriftybug.circuit_breaker_test.service.WeatherService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class WeatherController {

	private WeatherService weatherService;

	public WeatherController(final WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@GetMapping("/weather")
	@CircuitBreaker(name = "getWeatherCircuitBreaker", fallbackMethod = "fallbackGetWeather")
	public String getWeather(@RequestParam String city) {
		return weatherService.getWeather(city);
	}

	public String fallbackGetWeather(String city, Throwable throwable) {
		return String.format("Weather service is currently unavailable for city %s, Please try again later", city);
	}
}
