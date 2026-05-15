## `@ConfiguratonProperties`

### Links
- [@ConfigurationProperties](https://www.baeldung.com/configuration-properties-in-spring-boot)

- We start by adding spring-boot-starter-parent as the parent in our pom.xml:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.7</version>
    <relativePath/>
</parent> 
```
### Simple Properties

- **The official documentation advises that we isolate configuration properties into separate POJOs.**

```java
@Configuration
@ConfigurationProperties(prefix = "mail")
public class ConfigProperties {
    
    private String hostName;
    private int port;
    private String from;

    // standard getters and setters
}
```
- We use `@Configuration` so that Spring creates a Spring bean in the application context.

- `@ConfigurationProperties` works best with hierarchical properties that all have the same prefix; therefore, we add a prefix of mail.

It means:

`@ConfigurationProperties` is designed to group related configuration values together using a common starting name (prefix).

Think of it like organizing files into folders.

---

Instead of having unrelated properties like this:

```properties
hostName=localhost
port=8080
from=test@gmail.com
```

we group related mail settings under one “folder” called `mail`:

```properties
mail.hostName=smtp.gmail.com
mail.port=587
mail.from=admin@gmail.com
```

Here:

* `mail` is the common prefix
* `hostName`, `port`, and `from` are child properties

This is called **hierarchical properties** because the properties are organized in levels.

---

# How Spring Maps Them

When you write:

```java
@ConfigurationProperties(prefix = "mail")
public class ConfigProperties {

    private String hostName;
    private int port;
    private String from;

    // getters and setters
}
```

Spring automatically understands:

| Property File   | Java Field |
| --------------- | ---------- |
| `mail.hostName` | `hostName` |
| `mail.port`     | `port`     |
| `mail.from`     | `from`     |

So Spring fills the object automatically.

---

# Real-Life Analogy

Imagine this structure:

```text
mail
 ├── hostName
 ├── port
 └── from
```

`mail` acts like a parent folder.

Without hierarchy:

```properties
hostName=smtp.gmail.com
port=587
from=admin@gmail.com
```

it becomes harder to know:

* which properties belong together
* whether `port` is for mail, database, server, etc.

---

# Better Organized Example

## application.properties

```properties
mail.hostName=smtp.gmail.com
mail.port=587
mail.from=admin@gmail.com

database.url=jdbc:mysql://localhost:3306/test
database.username=root
```

Now properties are grouped clearly:

* `mail.*` → mail configuration
* `database.*` → database configuration

---

# Another Example

## Properties

```properties
app.security.username=admin
app.security.password=1234
```

## Java Class

```java
@Configuration
@ConfigurationProperties(prefix = "app.security")
public class SecurityConfig {

    private String username;
    private String password;

    // getters and setters
}
```

Spring maps:

* `app.security.username` → `username`
* `app.security.password` → `password`

---

simply means:

> Group related configuration values using the same starting name (`mail`, `database`, `app.security`, etc.) so Spring can bind them neatly into one Java object.

---

- The Spring framework uses standard Java bean setters, so we must declare setters for each of the properties.

>If we don’t use @Configuration in the POJO, then we need to add @EnableConfigurationProperties(ConfigProperties.class) in the main Spring application class to bind the properties into the POJO:

```java
@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class EnableConfigurationDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnableConfigurationDemoApplication.class, args);
    }
}
```
- That’s it! Spring will automatically bind any property defined in our property file that has the prefix mail and the same name as one of the fields in the ConfigProperties class.

- Spring uses some relaxed rules for binding properties. As a result, the following variations are all bound to the property hostName:

```properties
mail.hostName
mail.hostname
mail.host_name
mail.host-name
mail.HOST_NAME
```
---

- Therefore, we can use the following properties file to set all the fields:

```properties
#Simple properties
mail.hostname=host@mail.com
mail.port=9000
mail.from=mailer@mail.com
```
---

- As of Spring Boot 2.2, Spring finds and registers `@ConfigurationProperties` classes via classpath scanning. Scanning of `@ConfigurationProperties` needs to be explicitly opted into by adding the `@ConfigurationPropertiesScan` annotation. Therefore, we don’t have to annotate such classes with `@Component` (and other meta-annotations like @Configuration), or even use the @EnableConfigurationProperties:

```java
@ConfigurationProperties(prefix = "mail") 
@ConfigurationPropertiesScan 
public class ConfigProperties { 

    private String hostName; 
    private int port; 
    private String from; 

    // standard getters and setters 
}
```
- The classpath scanner enabled by `@SpringBootApplication` finds the ConfigProperties class, even though we didn’t annotate this class with @Component.

- In addition, we can use the `@ConfigurationPropertiesScan` annotation to scan custom locations for configuration property classes:

```java
@SpringBootApplication
@ConfigurationPropertiesScan("com.baeldung.configurationproperties")
public class EnableConfigurationDemoApplication { 

    public static void main(String[] args) {   
        SpringApplication.run(EnableConfigurationDemoApplication.class, args); 
    } 
}
```
- This way Spring will look for configuration property classes only in the com.baeldung.properties package.

--- 

- Explanation

- This is explaining **how Spring discovers your `@ConfigurationProperties` classes automatically**.

Before Spring Boot 2.2, you had to manually tell Spring:

> “Please create this class as a Spring bean.”

After Spring Boot 2.2, Spring can automatically find these classes by scanning your project.

---

# First Understand the Problem

Normally, Spring only manages classes annotated with things like:

```java
@Component
@Service
@Configuration
@Repository
```

Example:

```java
@Component
public class MyService {
}
```

Spring sees `@Component` and creates an object (bean).

---

# But `@ConfigurationProperties` Is Different

Example:

```java
@ConfigurationProperties(prefix = "mail")
public class ConfigProperties {

    private String hostName;
    private int port;

    // getters/setters
}
```

This class has:

* NO `@Component`
* NO `@Configuration`

So the question is:

> How does Spring know it should create this object?

---

# Before Spring Boot 2.2

You had to manually enable it:

```java
@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class App {
}
```

OR:

```java
@Configuration
@ConfigurationProperties(prefix = "mail")
public class ConfigProperties {
}
```

---

# After Spring Boot 2.2

Spring introduced:

```java
@ConfigurationPropertiesScan
```

Now Spring automatically searches your project for classes annotated with:

```java
@ConfigurationProperties
```

and registers them as beans.

---

# Simple Example

## application.properties

```properties
mail.hostName=smtp.gmail.com
mail.port=587
```

---

## Config Class

```java
@ConfigurationProperties(prefix = "mail")
public class ConfigProperties {

    private String hostName;
    private int port;

    // getters/setters
}
```

Notice:

* no `@Component`
* no `@Configuration`

---

## Main Application

```java
@SpringBootApplication
@ConfigurationPropertiesScan
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

Now Spring:

1. scans the project
2. finds `@ConfigurationProperties`
3. creates the bean automatically
4. injects property values

---

# What “Classpath Scanning” Means

Classpath scanning simply means:

> Spring searches through your project packages looking for specific annotations.

Like:

* `@Component`
* `@Service`
* `@Repository`
* `@ConfigurationProperties`

---

# What This Sentence Means

> “The classpath scanner enabled by @SpringBootApplication finds the ConfigProperties class”

means:

When the application starts, Spring automatically searches your packages and discovers `ConfigProperties`.

---

# Custom Scan Location

Example:

```java
@ConfigurationPropertiesScan("com.baeldung.configurationproperties")
```

This tells Spring:

> “Only look inside this package for `@ConfigurationProperties` classes.”

---

# Why Use Custom Scan Packages?

Useful in:

* large projects
* multi-module applications
* performance optimization
* better organization

---

# Visual Example

Suppose your project is:

```text
com.example
 ├── config
 │     └── MailProperties.java
 ├── service
 └── controller
```

With:

```java id="z4x9tb"
@ConfigurationPropertiesScan("com.example.config")
```

Spring scans ONLY:

```text
com.example.config
```

and ignores other packages for configuration properties.

---

# Important Clarification

This:

```java
@ConfigurationProperties
```

ONLY marks the class as:

> “This class holds configuration values.”

It does NOT automatically make it a Spring bean.

That’s why:

* previously → `@Component` or `@EnableConfigurationProperties` was needed
* now → `@ConfigurationPropertiesScan` can do it automatically

---

# Simple Summary

`@ConfigurationPropertiesScan` means:

> “Spring, automatically search for all classes annotated with `@ConfigurationProperties` and register them as beans.”

---

### Nested Properties

- We can have nested properties in Lists, Maps, and Classes.

- Let’S create a new Credentials class to use for some nested properties:

```java
public class Credentials {
    private String authMethod;
    private String username;
    private String password;

    // standard getters and setters
}
```
- We also need to update the ConfigProperties class to use a List, a Map, and the Credentials class:

```java
public class ConfigProperties {

    private String hostname;
    private int port;
    private String from;
    private List<String> defaultRecipients;
    private Map<String, String> additionalHeaders;
    private Credentials credentials;
 
    // standard getters and setters
}
``` 

- The following properties file will set all the fields:

```properties
#Simple properties
mail.hostname=mailer@mail.com
mail.port=9000
mail.from=mailer@mail.com

#List properties
mail.defaultRecipients[0]=admin@mail.com
mail.defaultRecipients[1]=owner@mail.com

#Map Properties
mail.additionalHeaders.redelivery=true
mail.additionalHeaders.secure=true

#Object properties
mail.credentials.username=john
mail.credentials.password=password
mail.credentials.authMethod=SHA1
```

---

### Using `@ConfigurationProperties` on `@Bean` method

- We can also use the @ConfigurationProperties annotation on @Bean-annotated methods.

- This approach may be particularly useful when we want to bind properties to a third-party component that’s outside of our control.

- Let’s create a simple Item class that we’ll use in the next example:

```java
public class Item {
    private String name;
    private int size;

    // standard getters and setters
}
```
Now let’s see how we can use @ConfigurationProperties on a @Bean method to bind externalized properties to the Item instance:

```java
@Configuration
public class ConfigProperties {

    @Bean
    @ConfigurationProperties(prefix = "item")
    public Item item() {
        return new Item();
    }
}
```
- Consequently, any item-prefixed property will be mapped to the Item instance managed by the Spring context.

---

# Using `@ConfigurationProperties` on a `@Bean` Method

Sometimes we want Spring to fill values into a class that we **cannot modify**.

This usually happens with:

* third-party library classes
* external SDK classes
* old classes we don’t want to annotate

In those cases, we can use:

```java
@ConfigurationProperties
```

directly on a `@Bean` method.

---

# Step-by-Step Example

---

# 1. Imagine This Is a Third-Party Class

Suppose this class comes from an external library.

We cannot add annotations to it.

```java
public class Item {

    private String name;
    private int size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
```

---

# 2. Add Properties in `application.properties`

```properties
item.name=Laptop
item.size=15
```

These are external configuration values.

---

# 3. Create Configuration Class

```java
@Configuration
public class ConfigProperties {

    @Bean
    @ConfigurationProperties(prefix = "item")
    public Item item() {

        return new Item();
    }
}
```

---

# What Happens Here?

Spring does this internally:

```java
Item item = new Item();

item.setName("Laptop");
item.setSize(15);
```

using values from:

```properties
item.name=Laptop
item.size=15
```

---

# Meaning of `prefix = "item"`

Spring looks for all properties starting with:

```properties
item.
```

and maps them to the object fields.

| Property    | Java Field |
| ----------- | ---------- |
| `item.name` | `name`     |
| `item.size` | `size`     |

---

# Why Use `@Bean` Here?

Because the class itself is NOT annotated.

So we manually create the object:

```java
return new Item();
```

and Spring manages it as a bean.

---

# Real-Life Analogy

Think of it like this:

You buy a machine from another company.

You cannot modify the machine itself.

But you can still configure it externally.

So Spring says:

> “Give me the object, and I’ll inject configuration values into it.”

---

# Without `@Bean`

Normally we do:

```java
@ConfigurationProperties(prefix = "item")
@Component
public class Item {
}
```

But this requires:

* access to the class
* ability to add annotations

Sometimes we cannot do that.

So instead:

```java
@Bean
@ConfigurationProperties(prefix = "item")
```

acts as an alternative.

---

# Full Flow

## Properties File

```properties
item.name=Laptop
item.size=15
```

↓

## Spring Creates Object

```java
new Item()
```

↓

## Spring Injects Values

```java
item.setName("Laptop");
item.setSize(15);
```

↓

## Bean Available in Spring Context

You can now inject it anywhere:

```java
@Service
public class ItemService {

    private final Item item;

    public ItemService(Item item) {
        this.item = item;
    }
}
```

---

# Simple Summary

Using:

```java
@Bean
@ConfigurationProperties(prefix = "item")
```

means:

> “Spring, create this object and automatically fill its fields using properties that start with `item.`”

---