
package com.example.scheduling;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class DynamicSchedulingConfig implements SchedulingConfigurer {

	@Autowired
	private TickService tickService;

	@Bean
	public ScheduledExecutorService taskExecutor() {
		return Executors.newSingleThreadScheduledExecutor();
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
		taskRegistrar.addTriggerTask(
				new Runnable() {

					@Override
					public void run() {
						tickService.tick();
					}

				},
				new Trigger() {

					@Override
					public Instant nextExecution(TriggerContext triggerContext) {

						Optional<Instant> lastCompletionTime = Optional.ofNullable(triggerContext.lastCompletion());
						System.out.println("lastCompletionTime: " + lastCompletionTime);

						return lastCompletionTime
								.orElseGet(Instant::now)
								.plusMillis(tickService.getDelay());
					}
				});
	}
}
