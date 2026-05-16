package com.example.scheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class SchedulingApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SchedulingApplication.class, args);
	}

}
