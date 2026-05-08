
package com.example.scheduling;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Test {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	// @Scheduled(fixedDelay = 1000)
	// public void scheduleFixedDelay() {
	// String time = LocalTime.now().format(formatter);
	// System.out.println("Fixed Delay task " + time);
	// }

	// @Scheduled(fixedRate = 1000)
	// public void scheduleFixedRate() {
	// String time = LocalTime.now().format(formatter);
	// System.out.println("Fixed rate task " + time);
	// }
	//

	// @Scheduled(cron = "0 */1 * * * *")
	// public void scheduleWithCron() {
	// String time = LocalTime.now().format(formatter);
	// System.out.println("Scheduled with cron: " + time);
	// }
}
