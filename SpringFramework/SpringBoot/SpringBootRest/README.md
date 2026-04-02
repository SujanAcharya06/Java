



## Springboot REST


- When we use `@Controller` annotation by default spring will search for a view name
- for example 
```java
@GetMapping("jobPosts")
public List<JobPost> jobPosts() {
	System.out.println("method called");
	return jobService.getAllJobs();
}
```
- here spring tries to find the view resolver for `jobService.getAllJobs();`
	- instead here we would like to return `json` data
		- we can do this by adding `@ResponseBody` annotation


### Content Negotiation

- By default Spring uses `Jackson` library to convert `Objects` into `Json` format
- If we want `xml` response body, we have to explicitly add `jackson-dataformat-xml` one more dependency to pom.xml file
```xml
<dependency>
	<groupId>com.fasterxml.jackson.dataformat</groupId>
	<artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```
- While calling the `GET` request, in `Headers` 
	- Key -> `Accept`
	- Value -> `application/xml` or any other type

- In `Controller` class, in any `Mapping` method we can mention `produces={"application/json"}`
	- this ensures the method always returns `json` data.
```java
@GetMapping(path = "jobPosts", produces = { "application/json" })
@ResponseBody
public List<JobPost> jobPosts() {
	return jobService.getAllJobs();
}
```

- Similarly in `@PostMapping` we can mention `consumes={"application/xml"}`
	- this tells that the method always accepts `application/xml`
- while calling this endpoint, the `Header`
	- key -> `Content-Type`
	- value -> `"application/xml"` or any other type
	- can be specified
