


## @Scheduled


- The simple rules that we need to follow to annotate a method with @Scheduled are:
	- the method should typically have a void return type (if not, the returned value will be ignored)
	- the method should not expect any parameters

- If we want to use `@Scheduled`
1. Enable it use `@EnableScheduling` on main class
```java
@SpringBootApplication
@EnableScheduling
public class SchedulingApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SchedulingApplication.class, args);
	}

}
```

2. Create a Configuration class
```java
@Configuration
@EnableScheduling
public Class ScheduleConfig{

}
```
- The class which has `@Scheduled` annotation must have `@Component`, `@Service` or `@Repository`
- As they tell spring to manage the beans internally

---

### Schedule a Task at Fixed Delay

- In this case, the duration between the end of the last execution and the start of the next execution is fixed. The task always waits until the previous one is finished.

- This option should be used when it’s mandatory that the previous execution is completed before running again.

```java
@Component
public class Test {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Scheduled(fixedDelay = 1000)
	public void scheduleFixedDelay() {

		String time = LocalTime.now().format(formatter);
		System.out.println("Fixed Delay task " + time);
	}
}
```
```log
2026-05-10T13:04:41.037+05:30  INFO 11335 --- [scheduling] [  restartedMain] c.e.scheduling.SchedulingApplication     : Started SchedulingApplication in 0.756 seconds (process running for 0.934)
Fixed Delay task 13:04:41
Fixed Delay task 13:04:42
Fixed Delay task 13:04:43
Fixed Delay task 13:04:44
Fixed Delay task 13:04:45
Fixed Delay task 13:04:46
Fixed Delay task 13:04:47
Fixed Delay task 13:04:48
Fixed Delay task 13:04:49
Fixed Delay task 13:04:50
Fixed Delay task 13:04:51
Fixed Delay task 13:04:52
Fixed Delay task 13:04:53
Fixed Delay task 13:04:54
^C2026-05-10T13:04:54.634+05:30  INFO 11335 
```

---

### Schedule a Task at a Fixed Rate

- This option should be used when each execution of the task is independent.

> [!NOTE]
> scheduled tasks don’t run in parallel by default. So even if we used fixedRate, the next task won’t be invoked until the previous one is done.

```java
@Scheduled(fixedRate = 1000)
public void scheduleFixedRate() {
	String time = LocalTime.now().format(formatter);
	System.out.println("Fixed rate task " + time);
}
```

- **If we want to support parallel behavior in scheduled tasks, we need to add the @Async annotation:**

```java
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
```

---

### Schedule a Task With Initial Delay

```java
@Scheduled(fixedRate = 1000, initialDelay = 100)
public void scheduleFixedRate() {
	String time = LocalTime.now().format(formatter);
	System.out.println("Fixed rate task " + time);
}
```

---

### Fixed Rate vs Fixed Delay

- We can run a scheduled task using Spring’s @Scheduled annotation, but based on the properties fixedDelay and fixedRate, the nature of execution changes.

- The fixedDelay property makes sure that there is a delay of n millisecond between the finish time of an execution of a task and the start time of the next execution of the task.

- This property is specifically useful when we need to make sure that only one instance of the task runs all the time. For dependent jobs, it is quite helpful.

- The fixedRate property runs the scheduled task at every n millisecond. It doesn’t check for any previous executions of the task.

- This is useful when all executions of the task are independent. If we don’t expect to exceed the size of the memory and the thread pool, fixedRate should be quite handy.

- Although, if the incoming tasks do not finish quickly, it’s possible they end up with “Out of Memory exception”.

---

### Schedule a Task Using Cron Expressions

- Sometimes delays and rates are not enough, and we need the flexibility of a cron expression to control the schedule of our tasks:

```java
@Scheduled(cron = "0 15 10 15 * ?")
public void scheduleTaskUsingCronExpression() {

	long now = System.currentTimeMillis() / 1000;
	System.out.println(
		"schedule tasks using cron jobs - " + now);
}
```

> [!NOTE]
> In this example, we’re scheduling a task to be executed at 10:15 AM on the 15th day of every month

- By default, Spring will use the server’s local time zone for the cron expression. However, we can use the zone attribute to change this timezone:
`@Scheduled(cron = "0 15 10 15 * ?", zone = "Europe/Paris")`

> [!NOTE]
> With this configuration, Spring will schedule the annotated method to run at 10:15 AM on the 15th day of every month in Paris time

```java
* * * * * *
| | | | | |
| | | | | └── day of week
| | | | └──── month
| | | └────── day of month
| | └──────── hour
| └────────── minute
└──────────── second
```
---

### Parameterizing the Schedule

- Hardcoding these schedules is simple, but we usually need to be able to control the schedule without re-compiling and re-deploying the entire app.

- We’ll make use of Spring Expressions to externalize the configuration of the tasks, and we’ll store these in properties files.

- A fixedDelay task:
`@Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")`

- A fixedRate task:
`@Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")`

- A cron expression based task:
`@Scheduled(cron = "${cron.expression}")`

---

### Setting Delay or Rate Dynamically at Runtime

- Normally, all the properties of the @Scheduled annotation are resolved and initialized only once at Spring context startup.

- Therefore, changing the fixedDelay or fixedRate values at runtime isn’t possible when we use @Scheduled annotation in Spring.

- However, there is a workaround. Using Spring’s SchedulingConfigurer provides a more customizable way to give us the opportunity of setting the delay or rate dynamically.

- Let’s create a Spring configuration, DynamicSchedulingConfig, and implement the SchedulingConfigurer interface:

```java
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
```

- As we notice, with the help of the ScheduledTaskRegistrar#addTriggerTask method, we can add a Runnable task and a Trigger implementation to recalculate the nextExecutionTime after the end of each execution.

- Additionally, we annotate our DynamicSchedulingConfig with @EnableScheduling to make the scheduling work.

- As a result, we scheduled the TickService#tick method to run it after each amount of delay, which is determined dynamically at runtime by the getDelay method.

---

### Running Tasks in Parallel

- By default, Spring uses a local single-threaded scheduler to run the tasks. As a result, even if we have multiple @Scheduled methods, they each need to wait for the thread to complete executing a previous task.

- If our tasks are truly independent, it’s more convenient to run them in parallel. For that, we need to provide a TaskScheduler that better suits our needs:

```java
@Bean
public TaskScheduler  taskScheduler() {
	ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
	threadPoolTaskScheduler.setPoolSize(5);
	threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
	return threadPoolTaskScheduler;
}
```

- In the above example, we configured the TaskScheduler with a pool size of five, but keep in mind that the actual configuration should be fine-tuned to one’s specific needs.

- Using Spring Boot
- If we use Spring Boot, we can make use of an even more convenient approach to increase the scheduler’s pool size.

- It’s simply enough to set the spring.task.scheduling.pool.size property:
spring.task.scheduling.pool.size=5

---
