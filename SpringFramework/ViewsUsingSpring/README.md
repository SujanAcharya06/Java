## Views using Spring

- Create base spring boot application using 
```bash
spring init \
  --build=maven \
  --java-version=21 \
  --dependencies=web,lombok \
  jobapp
```

- add maven dependencies
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<optional>true</optional>
</dependency>

<dependency>
	<groupId>org.apache.tomcat.embed</groupId>
	<artifactId>tomcat-embed-jasper</artifactId>
</dependency>

<dependency>
	<groupId>jakarta.servlet.jsp.jstl</groupId>
	<artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
</dependency>

<dependency>
	<groupId>org.glassfish.web</groupId>
	<artifactId>jakarta.servlet.jsp.jstl</artifactId>
</dependency>
```

- add views inside `webapp` folder
```
/main/
	/java
	/resources
		/static
		/templates
		/webapp
			/views
				*.jsp
			*.css
```

---

- To create a `Controller`, we need to make use of `@Controller` annotation

```java
@Controller
public class JobController {

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);

	@RequestMapping({ "/", "home" })
	public String home() {
		logger.info("Home method called");
		return "home";
	}
}
```
- `/addjob` 
```java
@RequestMapping("addjob")
public String addjob() {
	return "addjob";
}
```

- When we click on add job, the request would be `POST`, to create `POST` requests we need to have a form
`<form action="handleForm" method="post">`

- When we click on `Submit` the request goes to `http://localhost:8080/handleForm`

```java
@Data // for getter setters
@AllArgsConstructor // all fields constructor
@NoArgsConstructor // empty constructor
@Component // to be able to view in other classes
public class JobPost {
	private int postId;
	private String postProfile;
	private String postDesc;
	private String reqExperience;
	private List<String> postTechStack;
}
```

```java
@PostMapping("handleForm")
public String handleForm(JobPost jobPost) {
	return "success";
}
```
- Make sure to import correct package
	- `<%@ page import="com.example.jobapp.model.JobPost" %>`

```java
@Service
public class JobService {

	@Autowired
	private JobRepo jobRepo;

	public void addJob(JobPost job) {
		jobRepo.addJob(job);

	}

	public List<JobPost> getAllJobs() {
		return jobRepo.getAllJobs();
	}

}
```

```java
@Controller
public class JobController {

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);

	@Autowired
	private JobService jobService;

	@GetMapping({ "/", "home" })
	public String home() {
		logger.info("Home method called");
		return "home";
	}

	@GetMapping("addjob")
	public String addjob() {
		return "addjob";
	}

	@PostMapping("handleForm")
	public String handleForm(JobPost jobPost) {
		jobService.addJob(jobPost);
		return "success";
	}

}
```
---

