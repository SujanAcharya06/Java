
package com.example.scheduling;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class TickService {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	public void tick() {
		String time = LocalTime.now().format(formatter);
		System.out.println("Tick executed at: " + time);
	}

	public long getDelay() {
		return 5000;
	}
}
