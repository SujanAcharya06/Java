# Spring MVC DispatcherServlet Mapping using `web.xml`

## Why do we need `web.xml`?

In traditional Spring MVC (before Spring Boot auto-configuration), the **`web.xml` file acts as the deployment descriptor** for a Java web application.

It helps the **Dispatcher Servlet**:

* Intercept incoming HTTP requests
* Map the request URI to the appropriate controller method
* Manage the lifecycle of the web application
* Configure servlets, filters, listeners, and URL mappings

In simple words:

-> **`web.xml` tells the server which servlet should handle which request.**

In Spring MVC, we configure the **DispatcherServlet**, which is the **front controller** that:

1. Receives every request
2. Decides which controller should handle it
3. Calls the correct method
4. Returns the view

---

## 📌 How DispatcherServlet works

When a request like:

```
http://localhost:8080/
```

comes to the server:

1. The server checks `web.xml`
2. It finds the URL mapping for DispatcherServlet
3. DispatcherServlet handles the request
4. It scans controller classes
5. It finds the matching method (like `@RequestMapping("/")`)
6. It returns the view (`index.html`)

---

## Sample `web.xml` Configuration

Below is a complete example of a basic Spring MVC `web.xml`.

```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
	http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
	version="3.1">

	<!-- Dispatcher Servlet Definition -->
	<servlet>
		<servlet-name>spring</servlet-name>

		<!-- This is the front controller of Spring MVC -->
		<servlet-class>
			org.springframework.web.servlet.DispatcherServlet
		</servlet-class>

		<!-- Load servlet at startup -->
		<load-on-startup>1</load-on-startup>
	</servlet>

	<!-- URL Mapping -->
	<servlet-mapping>
		<servlet-name>spring</servlet-name>

		<!-- All requests go through DispatcherServlet -->
		<url-pattern>/</url-pattern>
	</servlet-mapping>

</web-app>
```

---

## What each tag means

### 1️⃣ `<servlet>`

Defines a servlet.

```
<servlet>
```

This tells the server:
-> We are creating a servlet named **spring**.

---

### 2️⃣ `<servlet-name>`

```
<servlet-name>spring</servlet-name>
```

Logical name of the servlet.

This name is later used in `<servlet-mapping>`.

---

### 3️⃣ `<servlet-class>`

```xml
<servlet-class>
	org.springframework.web.servlet.DispatcherServlet
</servlet-class>
```

This is the most important part.

-> It tells the server to use Spring’s DispatcherServlet.

This servlet:

* Acts as a **front controller**
* Routes requests to controller methods

---

### `<load-on-startup>`

```xml
<load-on-startup>1</load-on-startup>
```

This means:

* The servlet will be initialized when the server starts
* Not when the first request comes

This improves performance.

---

### 5️⃣ `<servlet-mapping>`

```xml
<servlet-mapping>
```

Maps URL patterns to the servlet.

---

### 6️⃣ `<url-pattern>`

```xml
<url-pattern>/</url-pattern>
```

This means:

-> **Every request will go through DispatcherServlet.**

Examples:

* `/`
* `/addStudent`
* `/home`
* `/api/users`

---

## How this links to your `HomeController`

Your controller:

```java
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("addStudent")
    public String add(Student student) {
        return "result";
    }
}
```

### Request Flow

### Case 1: Home Page

Request:

```url
http://localhost:8080/
```

Flow:

1. `web.xml` → DispatcherServlet handles the request
2. DispatcherServlet looks for controller mapping
3. Finds:

```java
@RequestMapping("/")
```
4. Calls:

```java
home()
```
5. Returns view:

```java
index.html
```

---

### Case 2: Form Submission

Request:

```url
http://localhost:8080/addStudent
```

Flow:

1. DispatcherServlet intercepts
2. Finds:

```java
@RequestMapping("addStudent")
```
3. Calls:

```java
add(Student student)
```
4. Binds form data to Student object
5. Returns:

```java
result.html
```

---

## How Spring knows where controllers are

DispatcherServlet loads a configuration file:

```
spring-servlet.xml
```

This file usually contains:

```xml
<context:component-scan base-package="com.example.controller"/>
```

This tells Spring where to find:

* Controllers
* Services
* Components

---

## Important Notes

### Spring Boot vs Traditional Spring MVC

In Spring Boot:

* You do NOT need `web.xml`
* DispatcherServlet is auto-configured
* Embedded Tomcat is used

In traditional Spring MVC:

* `web.xml` is mandatory
* Manual configuration is required

---

### Why Spring Boot removed `web.xml`

Because:

* XML was verbose
* Hard to maintain
* Annotation and Java config are cleaner
* Auto-configuration is easier

---

## Summary

`web.xml`:

* Registers DispatcherServlet
* Maps URLs to it
* Helps Spring route requests to controller methods

DispatcherServlet:

* Is the heart of Spring MVC
* Handles request → controller → view flow

---
