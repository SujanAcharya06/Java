


## Spring Basics

- We create a `maven project` not a springboot project from `start.spring.io`
- we can quickly create a maven project by using below command
```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```
- Add `Spring-Context` maven dependency in the `pom.xml` file
```xml
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-context</artifactId>
	<version>7.0.3</version>
	<scope>compile</scope>
</dependency>
```
- There are different ways of configuring the Spring project
1. `XML` based configuration
2. Java based configuration
3. Annotations

1. `XML` based configuration

- `ApplicationContext context = new ClassPathXmlApplicationContext();`
	- this sets the spring container
	- we need to tell spring to put the objects here

```java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public App() {
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext();
		Alien obj = (Alien)context.getBean("alien");
		obj.code();
	}
}
```
- If we try to run the App now we get this error
```logs
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
[WARNING] 
java.lang.IllegalStateException: BeanFactory not initialized or already closed - call 'refresh' before accessing beans via the ApplicationContext
    at org.springframework.context.support.AbstractRefreshableApplicationContext.getBeanFactory (AbstractRefreshableApplicationContext.java:167)
    at org.springframework.context.support.AbstractApplicationContext.getBean (AbstractApplicationContext.java:1278)
    at com.example.App.main (App.java:14)
    at jdk.internal.reflect.DirectMethodHandleAccessor.invoke (DirectMethodHandleAccessor.java:103)
    at java.lang.reflect.Method.invoke (Method.java:580)
    at org.codehaus.mojo.exec.AbstractExecJavaBase.executeMainMethod (AbstractExecJavaBase.java:402)
    at org.codehaus.mojo.exec.ExecJavaMojo.executeMainMethod (ExecJavaMojo.java:142)
    at org.codehaus.mojo.exec.AbstractExecJavaBase.doExecClassLoader (AbstractExecJavaBase.java:377)
    at org.codehaus.mojo.exec.AbstractExecJavaBase.lambda$execute$0 (AbstractExecJavaBase.java:287)
    at java.lang.Thread.run (Thread.java:1583)
```

- we have to create the `<spring>.xml` in the class path -> `main`
- Spring want to manage the `beans` objects managed by spring framework


#### We can create objects by using following methods
1. using xml configuration
		- make sure to put the file in resources and path should be classPath (main..)
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	<!--    if we have one more class we can mention it here-->
	<bean id="idName" class="fullclassPath"></bean>
</beans>

```
## Object creation

- `ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");`
	- object is getting created here itself
- The number of beans mentioned those many objects will be created
		- If we mention two different beans for the same class it will create two different objects
		- If we call the reference two times then the object will be created only once for a class
- that means even if there are two different references they are the same objects


## Scopes of beans
- `Singleton`
	- default
- `Prototype`
	- If we mention `scope="prototype"` it will create new object every time we do `getBean()`
```xml
<bean id="alien" class="com.example.Alien" scope="prototype"></bean>
```
- `Request`
- `Session`

> [!NOTE]
> when `scope=singleton` the object will be created at the time of Container initialization itself, but when `scope=prototype` the object will be created only when we use `getBean()` method

- Singleton and Prototype is used in spring core
- Request and session works for web or web sockets


```java
// in the java code we have to mention this
// This line directly creates the object
ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
// we are getting the reference using context
obj = context.getBean("idName"); // this creates the object
```
2. using java based configuration
3. annotations

## Setter Injection
- We are using setter method to assign the value
- It creates the bean then it checks for value if it is present then calls the setter method 
```java
public class Alien {
	private int age;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		System.out.println("Setter called");
		this.age = age;
	}

	public Alien() {
		System.out.println("Object created");
	}

	public void code() {
		System.out.println("Inside Code method");
	}

}
```
```logs
mvn exec:java -Dexec.mainClass="com.example.App" 
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------< com.example:spring1 >-------------------------
[INFO] Building spring1 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
Object created
Setter called
24
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.606 s
[INFO] Finished at: 2026-02-18T06:31:51+05:30
[INFO] ------------------------------------------------------------------------
```

- `<property name="variableName" value=""></property>` in xml file
> [!NOTE]
> `value=""` attribute is only for primitive data types

- reference
```xml
<bean id="alien" class="com.example.Alien">
	<property name="age" value="24"></property>
	<property name="tech" value="Java"></property>
	...
</bean>
```
- for primitive types value works fine

---

### `ref` Attribute

- for Objects or references

- getters and setters must be there
	- `<property name="variableName" ref="idOfrefBean"></property>`
- reference
```xml
<bean id="alien" class="com.example.Alien">
	<property name="lap" ref="lap1"></property>
</bean>
<bean id="lap1" class="com.example.Laptop">
</bean>
```
- If we do not provide `ref="lap1"` we get `NullPointerException`
- For `primitive` we can use `value=""`
- For reference we have to use `ref=""`
	- as we are dealing with the object reference bean must exist.
	- We can have multiple references for the same class the `id` differentiates these.

---

## Constructor Injection
- `<constructor-arg value="">`
- example
```xml
<bean id="alien" class="com.example.Alien">
	<constructor-arg value="16"/> </--if nothing inside tag -->
	<property name="lap" ref="lap1"/>
</bean>
<bean id="lap1" class="com.example.Laptop">
</bean>
```

```java
public class Alien {
	private int age = 15;

	private Laptop lap;

	public Alien(int age) {
		System.out.println("Parameterized constructor called");
		this.age = age;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		System.out.println("Setter called");
		this.age = age;
	}

	public Alien() {
		System.out.println("Object created for Alien");
	}

	public void code() {
		System.out.println("Inside Code method");
		lap.compile();
	}

	public Laptop getLap() {
		return lap;
	}

	public void setLap(Laptop lap) {
		this.lap = lap;
	}

}
```
```logs
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
Parameterized constructor called
Object created for laptop
Inside Code method
Compiling code in Laptop class
```

- If we have an object/reference inside constructor
	- for objects -> `<constructor-arg ref="beanID"/>`
	- when dealing with objects as parameters we have to provide references
- example
```xml
<bean id="alien" class="com.example.Alien">
	<constructor-arg value="16"></constructor-arg>
	<constructor-arg ref="lap1"></constructor-arg>
	<property name="lap" ref="lap1" />
</bean>
<bean id="lap1" class="com.example.Laptop">
</bean>
```

```java
public class Alien {
	private int age = 15;

	private Laptop lap;

	public Alien(int age) {
		System.out.println("Single Parameterized constructor called");
		this.age = age;
	}

	public Alien(int age, Laptop lap) {
		System.out.println("Two Parameterized constructor called");
		this.age = age;
		this.lap = lap;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		System.out.println("Setter called");
		this.age = age;
	}

	public Alien() {
		System.out.println("Object created for Alien");
	}

	public void code() {
		System.out.println("Inside Code method");
		lap.compile();
	}

	public Laptop getLap() {
		return lap;
	}

	public void setLap(Laptop lap) {
		this.lap = lap;
	}

}
```

```output
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
Object created for laptop
Two Parameterized constructor called
Inside Code method
Compiling code in Laptop class
```

- if there are multiple parameters in the constructor the **sequence** will be considered and matched accordingly.
- We can specify the `type=""` attribute as below, it will match the arguments
```xml
<bean id="alien" class="com.example.Alien">
	<constructor-arg type="com.example.Laptop" ref="lap1"></constructor-arg>
	<constructor-arg type="int" value="16"></constructor-arg>
	<property name="lap" ref="lap1" />
</bean>
<bean id="lap1" class="com.example.Laptop">
</bean>
```

- If there are two `int` types then again there will be a mismatch so, 
- we can also use `index` attribute
	- `<constructor-arg index="0" value="">`
	- `<constructor-arg index="1" value="">`
- example
```xml
<bean id="alien" class="com.example.Alien">
	<constructor-arg index="1" type="com.example.Laptop" ref="lap1"></constructor-arg>
	<constructor-arg index="0" type="int" value="16"></constructor-arg>
	<property name="lap" ref="lap1" />
</bean>
<bean id="lap1" class="com.example.Laptop">
</bean>
```

- we can directly use the `variable name` but sequence matters
	- `<constructor-arg name="var1" value="">`
	- `<constructor-arg name="var2" value="">`
- If we are not providing the constructor arguments in sequence then we can use
- `@ConstructorProperties({"var1", "var2"})` annotation on top of the constructor being used.

## Autowire
- if we don't want to mention the property explicitly if we want to ask spring framework to search for the dependency based on name and type we can use
		- `autowire="byName"`
- tries to match the object by name
		- if I have `autowire=byName` then if I explicitly provide
- `<property name="name" ref="reference"` then this will be preferred over `Autowire`
		- `autowire="byType"`
- tries to match the object by type

## Primary Bean
- if there is a confusion say the objects created for the insterfaces in bean and `autowire` is specified then `primary=true` specifed bean will be choosen
		- if we are mentioning explicitly then it will prefer the specifed one

## Lazy init Bean
- The Object will not be created by default only when we want to use it, it will be created.
		- still singleton
		- `lazy-init="true"`
		- if a non-lazy(eager) bean is dependent on a lazy bean still it will create the object of a lazy bean.

## Get bean by type
- we can specify the class of which you want the object in the `getBean()` method
		- Don't have to do type caste for the required class
		- `<T> T getBean(String name, Class<T> requiredType)`
		- We can either specify name of the bean or the type of class the object we want
- if we are specifing the class then in the bean tag there is no need for `id` vale

## Inner Bean
- To limit a bean only for a particular class
```xml
<!-- example -->
<bean id="alien" class="com.example.Alien" autowire="byType">
	<property name="age" value="21"/>
	<property name="com">
		<bean id="com1" class="com.example.Laptop" primary="true">
		</bean>
	</property>
</bean>
```
