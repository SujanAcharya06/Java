

## Java-Based Configs


- `ApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class)`
- The `ConfigClass` should have the annotation:
- `@Configuration`
- In Java-based configuration, we have to manually create objects in the Config class:
- `@Bean` annotation for Spring to create the object.
- Even though we are creating the object using `new`, the actual creation, managing, and injecting are done by Spring.

- Example
```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
Desktop desktop = context.getBean(Desktop.class);
desktop.compile();
```
```java
package com.example.config;

public class AppConfig {

}
```
- If we simply use above empty `AppConfig` class we get below error
```output
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
[WARNING] 
org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.example.Desktop' available
    at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBean (DefaultListableBeanFactory.java:386)
    at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBean (DefaultListableBeanFactory.java:377)
    at org.springframework.context.support.AbstractApplicationContext.getBean (AbstractApplicationContext.java:1296)
    at com.example.App.main (App.java:17)
    at jdk.internal.reflect.DirectMethodHandleAccessor.invoke (DirectMethodHandleAccessor.java:103)
    at java.lang.reflect.Method.invoke (Method.java:580)
```


- We have to configure the bean in `AppConfig` as below

```java
@Configuration
public class AppConfig {

	@Bean
	public Desktop desktop() {
		return new Desktop();
	}

}
```

```output
[INFO] 
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
Desktop object created
Compiling using Desktop.
```

---
## Bean Name

- If we use a random name for identifying the bean name
```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
Desktop desktop = context.getBean("com", Desktop.class);
desktop.compile();
```
- we get below error
```output
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
Desktop object created
[WARNING] 
org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'com' available
    at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition (DefaultListableBeanFactory.java:971)
    at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition (AbstractBeanFactory.java:1369)
    at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean (AbstractBeanFactory.java:296)
```

- The bean name is actually the `method name` mentioned in the config class.
- We can specify our own name for the bean in the annotation itself:
  `@Bean(name="newName")`
- The name can then be used in the `getBean()` method:
    - `Object obj = context.getBean("newName", ConfigClassName.class)`
- We can also use multiple names:
  `@Bean(name={"name1", "name2", "name3"})`
    - Any of these names can be used in the `getBean` method.

---
## Scope Annotation
- `@Scope`
- By default, the value will be `singleton`.
- We can change it by using `@Scope("prototype")` annotation on top of the methodname in `AppConfig`.
- Example
1. without using `@Scope("prototype"`) above bean
- we get following output for this java class

```java
public class App {
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		Desktop desktop1 = context.getBean("desktop", Desktop.class);
		desktop1.compile();

		Desktop desktop2 = context.getBean("desktop", Desktop.class);
		desktop2.compile();

	}
}
```

```output
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
Desktop object created
Compiling using Desktop.
Compiling using Desktop.
```

- with `@Scope("prototype")` we get following output
```output
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
Desktop object created
Compiling using Desktop.
Desktop object created
Compiling using Desktop.
```

---
## Autowired

- When we try to run this code
```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
Alien obj1 = context.getBean(Alien.class);
obj1.setAge(23);
System.out.println(obj1.getAge());
obj1.code();
```

```java
@Bean
public Alien alien() {
	return new Alien();
}
```
```java
public class Alien {
	private int age;

	private Computer com;

	public Alien(int age) {
		System.out.println("Single Parameterized constructor called");
		this.age = age;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		// System.out.println("Setter called");
		this.age = age;
	}

	public Alien() {
		System.out.println("Object created for Alien");
	}

	public void code() {
		System.out.println("Inside Code method");
		com.compile();
	}

	public Computer getCom() {
		return com;
	}

	public void setCom(Computer com) {
		this.com = com;
	}

}
```
- We get following error
- As alien object is calling `obj1.code()` which is of type `Computer`
	- we have not set it yet

```output
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
Object created for Alien
23
Inside Code method
[WARNING] 
java.lang.NullPointerException: Cannot invoke "com.example.Computer.compile()" because "this.com" is null
    at com.example.Alien.code (Alien.java:37)
    at com.example.App.main (App.java:21)
    at jdk.internal.reflect.DirectMethodHandleAccessor.invoke (DirectMethodHandleAccessor.java:103)
    at java.lang.reflect.Method.invoke (Method.java:580)
```
- So before this, we can also initialize the value of `age` inside `AppConfig` class

```java
@Bean
public Alien alien() {
	Alien obj = new Alien();
	obj.setAge(24);
	return obj;
}
```

- To resolve the `Computer com` object, we can either
1. We can specify dependencies by passing the object as a parameter while creating the bean in the config class.
```java
@Bean
public Alien alien() {
	Alien obj = new Alien();
	obj.setAge(24);
	obj.setCom(desktop());
	return obj;
}
```
```output
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
Object created for Alien
Desktop object created
24
Inside Code method
Compiling using Desktop.
```

- initializing the reference using above causes a tight coupling between alien object and only desktop object
- As we see `Desktop` and `Laptop` are implementations for `Computer` we can't tell `Alien` object to select laptop object
- To resolve this we can use below second approach

2.
```java
@Bean
public Alien alien(Computer com) {
	Alien obj = new Alien();
	obj.setAge(24);
	obj.setCom(com);
	return obj;
}
```
- Spring while creating `Alien` object sees that it is dependent on `Computer` object, now it checks in the container for any object of type `Computer` and uses it 
- above approach also here as of now produces the required output
```output
INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
Desktop object created
Object created for Alien
24
Inside Code method
Compiling using Desktop.
```

- In previous versions, we should have mentioned `@Autowired`, as shown below
```java
@Bean
public Alien alien(@Autowired Computer com) {
	Alien obj = new Alien();
	obj.setAge(24);
	obj.setCom(com);
	return obj;
}
```
---

## Primary and Qualifier

- Say we have two instance of Computer
```java
@Configuration
public class AppConfig {

	@Bean
	public Alien alien(Computer com) {
		Alien obj = new Alien();
		obj.setAge(24);
		obj.setCom(com);
		return obj;
	}

	@Bean
	public Desktop desktop() {
		return new Desktop();
	}

	@Bean
	public Laptop laptop() {
		return new Laptop();
	}

}
```

```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

Alien obj1 = context.getBean(Alien.class);
System.out.println(obj1.getAge());
obj1.code();
```

- We get below error
	- `No qualifying bean of type 'com.example.Computer' available: expected single matching bean but found 2: desktop,laptop`

```output
[INFO] --- exec:3.6.3:java (default-cli) @ spring1 ---
Feb 21, 2026 5:15:39 PM org.springframework.context.annotation.AnnotationConfigApplicationContext refresh
WARNING: Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'alien' defined in com.example.config.AppConfig: Unsatisfied dependency expressed through method 'alien' parameter 0: No qualifying bean of type 'com.example.Computer' available: expected single matching bean but found 2: desktop,laptop
[WARNING] 
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'alien' defined in com.example.config.AppConfig: Unsatisfied dependency expressed through method 'alien' parameter 0: No qualifying bean of type 'com.example.Computer' available: expected single matching bean but found 2: desktop,laptop
    at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray (ConstructorResolver.java:804)
```
- It finds two beans for `Computer com`, which throws above error

- To resolve this, we can specify the object which want to refer to say either `desktop` or `laptop` using annotation `@Qualifier("desktop")`
```java
@Bean
public Alien alien(@Qualifier("desktop") Computer com) {
	Alien obj = new Alien();
	obj.setAge(24);
	obj.setCom(com);
	return obj;
}
```

```output
Desktop object created
Object created for Alien
Object created for laptop
24
Inside Code method
Compiling using Desktop.
```

- If we do not wish to use `@Qualifier("beanName")` annotation we can use `@Primary` annotation
```java
@Configuration
public class AppConfig {

	@Bean
	public Alien alien(Computer com) {
		Alien obj = new Alien();
		obj.setAge(24);
		obj.setCom(com);
		return obj;
	}

	@Bean
	public Desktop desktop() {
		return new Desktop();
	}

	@Bean
	@Primary
	public Laptop laptop() {
		return new Laptop();
	}

}
```

```output
Object created for laptop
Object created for Alien
Desktop object created
24
Inside Code method
Compiling using Laptop
```

---

## Component Stereotype Annotation
- `@Component` is a stereotype annotation that tells the Spring framework to manage object creation.
- Any class that should be automatically managed by Spring must be annotated with `@Component`.
- After adding `@Component`, we don’t need `@Bean` methods—Spring will handle object creation automatically.
- We can also use `@ComponentScan("package name")` to specify package scanning.

## Autowired Field, Constructor, and Setter
- If Spring is unable to create objects for a specified field, we can use `@Autowired` with `@Qualifier` to explicitly specify which object to create.
- We can also specify a name in `@Component("name")`, which can be referenced in `@Qualifier`.

### Types of Dependency Injection:
1. **Field Injection** → `@Autowired` above a field.
2. **Constructor Injection** → `@Autowired` above a constructor.
3. **Setter Injection** → `@Autowired` above a setter.
    - If not using field injection, it is recommended to use `@Autowired` with setters.

## Primary Annotation
- We can mention any one of the class as `@Primary` then this class will be used by the spring to create an object if there is confusion
- If we mention both `@Qualifier` as well as `@Primary` then the first preference will be given to `@Qualifier` one.

## Scope and Values
-  We can inject values by using `@Value` annotation
-  Advantage of using this is that we can use a separate properties file to inject the values.
