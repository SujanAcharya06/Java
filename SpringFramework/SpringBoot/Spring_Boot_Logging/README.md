

## Spring Book Logging Slf4j + Logback + ELK Stack + S3 + Tracing


![](./assets/2025-10-28-11-14-45.png)

![](assets/2025-10-29-10-10-09.png)


![](assets/2025-10-29-10-19-37.png)

![](assets/2025-10-29-10-23-55.png)

![](assets/2025-10-29-10-27-30.png)

![](assets/2025-10-29-10-29-29.png)

![](assets/2025-10-29-10-31-00.png)

![](assets/2025-10-29-11-18-10.png)

- Slf4j uses `logback` by default

- class?
- To use Logger
	- 1st approach
		- create an object of it
			- `import org.slf4j.Logger`
			- `import org.slf4j.LoggerFactory`
			- `Logger logger = LoggerFactory.getLogger(HelloWorld.class);`
	- 2nd approach
		- use `lombok` -> `@Slf4j` -> log variable

- what method to call?
- There are multiple methods
	1. `trace()` -> more granular than debug
		- by default it will not be printed even in non-prod
		- you need to put additional configured to see trace statement.
	2. `debug()` -> will not be printed in production
		- use debug for testing only till non-prod
	3. `info()` -> info statements will be printed in production 
	4. `warn()` -> something which is warning something is concerning... look into it.. otherwise it can become a problem in future
	5. `error()` -> whenever there is exception and we want to capture the information

- these methods are nothing but `log levels`
- TRACE
- DEBUG
- INFO
- WARN
- ERROR

- There is some `external configuration` which define which log level to print
- DEFAULT `external configuration` is `INFO`

- If `external configuration` is `TRACE`, then `trace()` and above methods will be printed
- external `TRACE`
	- `trace()`
	- `debug()`
	- `info()`
	- `warn()`
	- `error()`

- If `external configuration` is `DEBUG`, then `debug()` and above will be printed
	- for all non-prod system default configuration is `DEBUG` 
- external `DEBUG`
	- `debug()`
	- `info()`
	- `warn()`
	- `error()`

- If `external configuration` is `INFO`, then `info()` and above methods will be printed
	- for production system, default external configuration is `INFO`
- external `INFO`
	- `info()`
	- `warn()`
	- `error()`

	
- If `external configuration` is `WARN`, then `warn()` and above will be printed
- external `WARN`
	- `warn()`
	- `error()`
	
- If `external configuration` is `ERROR`, then `error()` and above will be printed
- external `ERROR`
	- `error()`

---

### Useful links

- [https://sematext.com/blog/slf4j-tutorial](https://sematext.com/blog/slf4j-tutorial/)
- [slf4j.org](https://slf4j.org/manual.html)
