
package com.thriftybug.circuit_breaker_test.service;

import org.springframework.stereotype.Service;

@Service
public class WeatherService {

	public String getWeather(String city) {
		// Simulates a call to weather api
		if ("error".equalsIgnoreCase(city)) {
			throw new RuntimeException("Simulated API failure");
		} else if ("timeout".equalsIgnoreCase(city)) {
			try {
				Thread.sleep(3000); // simulate a long running operation
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			throw new RuntimeException("Simulated timeout");
		}
		return String.format("Sunny in %s", city);
	}

}
