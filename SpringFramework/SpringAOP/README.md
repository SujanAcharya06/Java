


## Spring Aspect Oriented Programming(AOP)


### `Before Advice`

- `@Before("execution(* *.*(..))")`
	- * -> any return type
	- *.* -> fullyQualifiedClassName
	- (..) -> represents wildcard for any argument

```java
@Component
@Aspect
public class LoggingAspect {

	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

	// return type, fullyQualifiedClassName.methodName(args)

	@Before("execution(* com.example.jobportal.service.JobService.*(..))")
	public void logMethodCalled() {
		log.info("Method called");
	}

}

```
```log
2026-04-14T08:12:16.642+05:30  INFO 19345 --- [demo] [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : Completed initialization in 2 ms
2026-04-14T08:12:16.670+05:30  INFO 19345 --- [demo] [nio-8080-exec-2] com.example.jobportal.LoggingAspect      : Method called
```

- `@Before("execution(* com.example.jobportal.service.JobService.*(..))")`
	- `@Before` is the `Advice`
	- `"execution(* com.example.jobportal.service.JobService.*(..))"`
		- is the PointCut
			- PointCut is an expression which specifies when we want the `Advice` to be called

### `JoinPoint`

- We can use `JoinPoint`'s object method to get the name of the method
	- `getSignature()`
	- `getName()`

#### `@Before` -> executes before a method is called

```java
@Component
@Aspect
public class LoggingAspect {

	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

	// return type, fullyQualifiedClassName.methodName(args)

	@Before("execution(* com.example.jobportal.service.JobService.getAllJobs(..))")
	public void logMethodCalled(JoinPoint jp) {
		log.info("Method called {}", jp.getSignature().getName());
	}

}
```
```log
2026-04-14T08:39:46.978+05:30  INFO 23554 --- [demo] [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2026-04-14T08:39:46.996+05:30  INFO 23554 --- [demo] [nio-8080-exec-2] c.example.jobportal.aop.LoggingAspect    : Method called getAllJobs
```

- We can also club methodnames by using `||` operator 

```java
@Component
@Aspect
public class LoggingAspect {

	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

	// return type, fullyQualifiedClassName.methodName(args)

	@Before("execution(* com.example.jobportal.service.JobService.getAllJobs(..)) || execution(* com.example.jobportal.service.JobService.getJob(..))")
	public void logMethodCalled(JoinPoint jp) {
		log.info("Method called {}", jp.getSignature().getName());
	}

}
```


```log
2026-04-14T08:43:42.402+05:30  INFO 24248 --- [demo] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2026-04-14T08:44:00.084+05:30  INFO 24248 --- [demo] [nio-8080-exec-3] c.example.jobportal.aop.LoggingAspect    : Method called getJob
```

#### `@After` -> executes after a method call

- Will be triggered even if the method did not run successfully, like if it threw an exception or something

```java
@Component
@Aspect
public class LoggingAspect {

	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

	@After("execution(* com.example.jobportal.service.JobService.getAllJobs(..)) || execution(* com.example.jobportal.service.JobService.getJob(..))")
	public void logMethodExecuted(JoinPoint jp) {
		log.info("Method Executed {}", jp.getSignature().getName());
	}

}
```

```log
2026-04-14T08:47:26.416+05:30  INFO 25286 --- [demo] [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2026-04-14T08:47:31.046+05:30  INFO 25286 --- [demo] [nio-8080-exec-3] c.example.jobportal.aop.LoggingAspect    : Method called getAllJobs
2026-04-14T08:47:31.046+05:30  INFO 25286 --- [demo] [nio-8080-exec-3] c.example.jobportal.aop.LoggingAspect    : Method Executed getAllJobs
```

#### `@AfterThrowing`, `@AfterReturning`

- `@AfterThrowing` -> executes if the method specified in the `PointCut` throws an `exception` 
- `@AfterReturning` -> executes if the method ran successfully

```java
@Component
@Aspect
public class LoggingAspect {

	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

	// return type, fullyQualifiedClassName.methodName(args)

	@AfterThrowing("execution(* com.example.jobportal.service.JobService.getAllJobs(..)) || execution(* com.example.jobportal.service.JobService.getJob(..))")
	public void logMethodCrash(JoinPoint jp) {
		log.info("@AfterThrowing - Method has some issues {}", jp.getSignature().getName());
	}

	@AfterReturning("execution(* com.example.jobportal.service.JobService.getAllJobs(..)) || execution(* com.example.jobportal.service.JobService.getJob(..))")
	public void logMethodExecutedSuccess(JoinPoint jp) {
		log.info("@AfterReturning - Method Executed Successfully{}", jp.getSignature().getName());
	}
}
```

```log
026-04-14T09:00:01.046+05:30  INFO 29018 --- [demo] [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2026-04-14T09:00:01.068+05:30  INFO 29018 --- [demo] [nio-8080-exec-2] c.example.jobportal.aop.LoggingAspect    : @Before - Method called getJob
2026-04-14T09:00:01.069+05:30  INFO 29018 --- [demo] [nio-8080-exec-2] c.example.jobportal.aop.LoggingAspect    : @AfterThrowing - Method has some issues getJob
2026-04-14T09:00:01.069+05:30  INFO 29018 --- [demo] [nio-8080-exec-2] c.example.jobportal.aop.LoggingAspect    : @After - Method Executed getJob
2026-04-14T09:00:01.071+05:30 ERROR 29018 --- [demo] [nio-8080-exec-2] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: java.lang.ArithmeticException: / by zero] with root cause
```
```log
2026-04-14T09:01:20.307+05:30  INFO 29331 --- [demo] [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2026-04-14T09:01:20.329+05:30  INFO 29331 --- [demo] [nio-8080-exec-2] c.example.jobportal.aop.LoggingAspect    : @Before - Method called getJob
2026-04-14T09:01:20.329+05:30  INFO 29331 --- [demo] [nio-8080-exec-2] c.example.jobportal.aop.LoggingAspect    : @AfterReturning - Method Executed SuccessfullygetJob
2026-04-14T09:01:20.329+05:30  INFO 29331 --- [demo] [nio-8080-exec-2] c.example.jobportal.aop.LoggingAspect    : @After - Method Executed getJob
```

### Performance Monitoring using `Around` Advice

- When we specify `@Around`, we are telling it to execute before and after the method
	- but here we should also mention that, we have to execute the method itself.
- In the `Advice` we have to mention the object of `ProceedingJointPoint`
	- `ProceedingJointPoint` 
		- basically means we are trying to call the actual method
	- While using `@Aurond`, we have to specify a return type
		- as we use `proceed` to call the actual object
			- this returns `Object` type
```java
@Component
@Aspect
public class PerformanceMonitoringAspect {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(PerformanceMonitoringAspect.class);

	@Around("execution(* com.example.jobportal.service.JobService.getJob(..))")
	public Object monitorTime(ProceedingJoinPoint jp) throws Throwable {
		long start = System.currentTimeMillis();

		Object obj = jp.proceed();

		long end = System.currentTimeMillis();

		long timeTakenNs = (end - start);
		log.info("Time taken {} ns ({} ms)", timeTakenNs, timeTakenNs / 1_000_000.0);

		return obj;
	}

}
```

### Validating the input using `Around` Advice

- We can validate the input by consuming the argument using `args(argument)`
```java
@Component
@Aspect
public class ValidationAspect {
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ValidationAspect.class);

	@Around("execution (* com.example.jobportal.service.JobService.getJob(..)) && args(postId)")
	public Object validateAndUpdate(ProceedingJoinPoint jp, int postId) throws Throwable {
		if (postId < 0) {
			log.info("postId is negative updating it");
			postId = -postId;
			log.info("New value, {}", postId);
		}

		Object obj = jp.proceed(new Object[] { postId });

		return obj;
	}

}
```

- while returning we need to send array of type Object
- if there are multiple arguments we can use comma separated value
`Object obj = jp.proceed(new Object[] { postId });
return obj;`

```bash
curl --location 'http://localhost:8080/jobPost/-2'
```

```log
026-04-14T14:11:14.128+05:30  INFO 51065 --- [demo] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2026-04-14T14:11:14.129+05:30  INFO 51065 --- [demo] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2026-04-14T14:11:14.163+05:30  INFO 51065 --- [demo] [nio-8080-exec-1] c.example.jobportal.aop.LoggingAspect    : @Before - Method called getJob
2026-04-14T14:11:14.166+05:30  INFO 51065 --- [demo] [nio-8080-exec-1] c.e.jobportal.aop.ValidationAspect       : postId is negative updating it
2026-04-14T14:11:14.166+05:30  INFO 51065 --- [demo] [nio-8080-exec-1] c.e.jobportal.aop.ValidationAspect       : New value, 2
2026-04-14T14:11:14.167+05:30  INFO 51065 --- [demo] [nio-8080-exec-1] c.e.j.aop.PerformanceMonitoringAspect    : Time taken by getJob, 3 ms
2026-04-14T14:11:14.167+05:30  INFO 51065 --- [demo] [nio-8080-exec-1] c.example.jobportal.aop.LoggingAspect    : @AfterReturning - Method Executed SuccessfullygetJob
2026-04-14T14:11:14.167+05:30  INFO 51065 --- [demo] [nio-8080-exec-1] c.example.jobportal.aop.LoggingAspect    : @After - Method Executed getJob
```
