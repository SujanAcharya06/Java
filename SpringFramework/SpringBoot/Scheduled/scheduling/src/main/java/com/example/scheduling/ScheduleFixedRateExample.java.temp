
package com.example.scheduling;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
public class ScheduleFixedRateExample {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Async
	@Scheduled(fixedRate = 1000)
	public void scheduleWithFixedRateAsync() throws InterruptedException {
		String time = LocalTime.now().format(formatter);
		System.out.println(time);
		Thread.sleep(2000);
	}

}
