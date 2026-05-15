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

### Explanation

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

```java
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

### Property Validation

- `@ConfigurationProperties` provides validation of properties using the JSR-380 format. This allows all sorts of neat things.

- For example, let’s make the hostName property mandatory:

```java
@NotBlank
private String hostName;
```

- Next, let’s make the authMethod property from 1 to 4 characters long

```java
@Length(max = 4, min = 1)
private String authMethod;
```

- Then the port property from 1025 to 65536:

```java
@Min(1025)
@Max(65536)
private int port;
```

- Finally, the from property must match an email address format:

```java
@Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$")
private String from;
```
- This helps us reduce a lot of if – else conditions in our code, and makes it look much cleaner and more concise.

- If any of these validations fail, then the main application would fail to start with an IllegalStateException.

- The Hibernate Validation framework uses standard Java bean getters and setters, so it’s important that we declare getters and setters for each of the properties.

---

### Property Conversion

- `@ConfigurationProperties` supports conversion for multiple types of binding the properties to their corresponding beans.

#### Duration
- We’ll start by looking at converting properties into Duration objects.

- Here we have two fields of type Duration:

```java
@ConfigurationProperties(prefix = "conversion")
public class PropertyConversion {

    private Duration timeInDefaultUnit;
    private Duration timeInNano;
    ...
}
```
- This is our properties file:

```properties
conversion.timeInDefaultUnit=10
conversion.timeInNano=9ns
```

- As a result, the field timeInDefaultUnit will have a value of 10 milliseconds, and timeInNano will have a value of 9 nanoseconds.

- The supported units are ns, us, ms, s, m, h and d for nanoseconds, microseconds, milliseconds, seconds, minutes, hours, and days, respectively.

- The default unit is milliseconds, which means if we don’t specify a unit next to the numeric value, Spring will convert the value to milliseconds.

- We can also override the default unit using @DurationUnit:

```java
@DurationUnit(ChronoUnit.DAYS)
private Duration timeInDays;
```
- This is the corresponding property:

- conversion.timeInDays=2

---

### Explanation

# Understanding `Duration` in `@ConfigurationProperties` (Simple Explanation)

Spring can automatically convert text from `application.properties` into Java objects.

One useful example is converting time values into Java `Duration`.

---

# What Is `Duration`?

`Duration` is a Java class used to represent time.

Examples:

* 5 seconds
* 10 minutes
* 2 days

Instead of storing time as plain numbers, Java stores it as a proper time object.

---

# Example Class

```java
@ConfigurationProperties(prefix = "conversion")
public class PropertyConversion {

    private Duration timeInDefaultUnit;

    private Duration timeInNano;

    // getters and setters
}
```

Here both fields are of type:

```java
Duration
```

---

# Properties File

```properties
conversion.timeInDefaultUnit=10
conversion.timeInNano=9ns
```

---

# What Spring Does Automatically

Spring reads:

```properties
conversion.timeInDefaultUnit=10
```

and converts it into:

```java
Duration.ofMillis(10)
```

because the default unit is **milliseconds**.

---

Spring also reads:

```properties
conversion.timeInNano=9ns
```

and converts it into:

```java
Duration.ofNanos(9)
```

---

# Simple Meaning

| Property Value | Meaning         |
| -------------- | --------------- |
| `10`           | 10 milliseconds |
| `9ns`          | 9 nanoseconds   |
| `5s`           | 5 seconds       |
| `2m`           | 2 minutes       |
| `1h`           | 1 hour          |
| `3d`           | 3 days          |

---

# Supported Time Units

| Unit | Meaning      |
| ---- | ------------ |
| `ns` | nanoseconds  |
| `us` | microseconds |
| `ms` | milliseconds |
| `s`  | seconds      |
| `m`  | minutes      |
| `h`  | hours        |
| `d`  | days         |

---

# Important Point

If you write ONLY a number:

```properties
conversion.timeInDefaultUnit=10
```

Spring assumes:

```text
10 milliseconds
```

because milliseconds are the default.

---

# Using `@DurationUnit`

Sometimes you want a different default unit.

Example:

```java
@DurationUnit(ChronoUnit.DAYS)
private Duration timeInDays;
```

---

# Property

```properties
conversion.timeInDays=2
```

Now Spring interprets it as:

```java
Duration.ofDays(2)
```

instead of milliseconds.

---

# Without `@DurationUnit`

```properties
conversion.timeInDays=2
```

would mean:

```text
2 milliseconds
```

which is probably not what you want.

---

# Real-Life Example

Suppose you have cache settings:

## Properties

```properties
cache.timeout=5m
```

---

## Java Class

```java
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    private Duration timeout;

    // getters/setters
}
```

Spring automatically converts:

```text
5m → 5 minutes
```

---

# Internally Spring Does Something Like

```java
timeout = Duration.ofMinutes(5);
```

automatically for you.

---

# Why This Is Useful

Instead of manually converting values:

```java
int timeout = Integer.parseInt(value);
```

Spring handles:

* parsing
* conversion
* unit handling

automatically.

---

# Simple Summary

`@ConfigurationProperties` can automatically convert text properties into Java `Duration` objects.

Example:

```properties
app.timeout=5s
```

becomes:

```java
Duration.ofSeconds(5)
```

inside your Java class automatically.

---

#### 7.2. DataSize

- Similarly, Spring Boot @ConfigurationProperties supports DataSize type conversion.

- Let’s add three fields of type DataSize:

```java
private DataSize sizeInDefaultUnit;

private DataSize sizeInGB;

@DataSizeUnit(DataUnit.TERABYTES)
private DataSize sizeInTB;
```
- These are the corresponding properties:

```properties
conversion.sizeInDefaultUnit=300
conversion.sizeInGB=2GB
conversion.sizeInTB=4
```
- In this case, the sizeInDefaultUnit value will be 300 bytes, as the default unit is bytes.

- The supported units are B, KB, MB, GB, and TB. We can also override the default unit using @DataSizeUnit.

---

### Explanation

# Understanding `DataSize` in `@ConfigurationProperties` (Simple Explanation)

Just like Spring can convert text into `Duration`, it can also convert text into file sizes using `DataSize`.

`DataSize` represents storage sizes like:

* bytes
* KB
* MB
* GB
* TB

---

# Example Class

```java
@ConfigurationProperties(prefix = "conversion")
public class PropertyConversion {

    private DataSize sizeInDefaultUnit;

    private DataSize sizeInGB;

    @DataSizeUnit(DataUnit.TERABYTES)
    private DataSize sizeInTB;

    // getters and setters
}
```

---

# Properties File

```properties
conversion.sizeInDefaultUnit=300
conversion.sizeInGB=2GB
conversion.sizeInTB=4
```

---

# What Spring Does Automatically

---

# 1. Default Unit Example

Property:

```properties
conversion.sizeInDefaultUnit=300
```

Spring converts it to:

```text
300 bytes
```

because the default unit is **bytes**.

Internally:

```java
DataSize.ofBytes(300)
```

---

# 2. Explicit Unit Example

Property:

```properties
conversion.sizeInGB=2GB
```

Spring converts it to:

```text
2 gigabytes
```

Internally:

```java
DataSize.ofGigabytes(2)
```

---

# 3. Using `@DataSizeUnit`

Field:

```java
@DataSizeUnit(DataUnit.TERABYTES)
private DataSize sizeInTB;
```

Property:

```properties
conversion.sizeInTB=4
```

Normally `4` means:

```text
4 bytes
```

BUT because of:

```java
@DataSizeUnit(DataUnit.TERABYTES)
```

Spring interprets it as:

```text
4 terabytes
```

Internally:

```java
DataSize.ofTerabytes(4)
```

---

# Supported Units

| Unit | Meaning   |
| ---- | --------- |
| `B`  | Bytes     |
| `KB` | Kilobytes |
| `MB` | Megabytes |
| `GB` | Gigabytes |
| `TB` | Terabytes |

---

# Real-World Example

Suppose you want file upload limits.

## application.properties

```properties
upload.max-file-size=10MB
upload.max-request-size=50MB
```

---

## Java Class

```java
@ConfigurationProperties(prefix = "upload")
public class UploadProperties {

    private DataSize maxFileSize;

    private DataSize maxRequestSize;

    // getters/setters
}
```

---

Spring automatically converts:

```text
10MB → DataSize object
50MB → DataSize object
```

---

# Why This Is Useful

Without Spring conversion, you would manually do things like:

```java
long bytes = 10 * 1024 * 1024;
```

Spring removes all that manual calculation.

---

# Simple Analogy

Think of Spring as a smart translator.

You write:

```properties
cache.size=2GB
```

Spring understands:

```text
"Oh, you mean 2 gigabytes."
```

and converts it automatically into a Java object.

---

# Simple Summary

`DataSize` lets Spring automatically convert configuration values like:

```properties
app.file-size=5GB
```

into Java `DataSize` objects.

If no unit is specified:

* default = bytes

You can change the default using:

```java
@DataSizeUnit(...)
```
---

#### Custom Converter
- We can also add our own custom Converter to support converting a property to a specific class type.

- Let’s add a simple class Employee:

```java
public class Employee {
    private String name;
    private double salary;
}
```
- Then we’ll create a custom converter to convert this property:

```property
conversion.employee=john,2000
```

- We will convert it to a file of type Employee:

```java
private Employee employee;
```
- We will need to implement the Converter interface, then use @ConfigurationPropertiesBinding annotation to register our custom Converter:

```java
@Component
@ConfigurationPropertiesBinding
public class EmployeeConverter implements Converter<String, Employee> {

    @Override
    public Employee convert(String from) {
        String[] data = from.split(",");
        return new Employee(data[0], Double.parseDouble(data[1]));
    }
}
```
---

#### Explanation
# Understanding Custom Converter in `@ConfigurationProperties` (Simple Explanation)

Sometimes Spring already knows how to convert values automatically.

For example:

* `"5s"` → `Duration`
* `"2GB"` → `DataSize`

But what if we have our own custom class like:

```java
Employee
```

Spring does NOT know how to convert:

```properties
conversion.employee=john,2000
```

into an `Employee` object automatically.

So we create a **custom converter**.

---

# Goal

We want this property:

```properties
conversion.employee=john,2000
```

to become:

```java
new Employee("john", 2000)
```

automatically.

---

# Step 1 — Create Employee Class

```java
public class Employee {

    private String name;
    private double salary;

    public Employee() {
    }

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    // getters and setters
}
```

---

# Step 2 — Create Configuration Class

```java
@ConfigurationProperties(prefix = "conversion")
@Component
public class PropertyConversion {

    private Employee employee;

    // getters and setters
}
```

---

# Step 3 — Add Property

```properties
conversion.employee=john,2000
```

At this point Spring sees:

```text
"john,2000"
```

But it does not know:

* what is `john`
* what is `2000`
* how to create `Employee`

---

# Step 4 — Create Custom Converter

```java
@Component
@ConfigurationPropertiesBinding
public class EmployeeConverter
        implements Converter<String, Employee> {

    @Override
    public Employee convert(String from) {

        String[] data = from.split(",");

        return new Employee(
                data[0],
                Double.parseDouble(data[1])
        );
    }
}
```
---

# What This Converter Does

Input:

```text
john,2000
```

---

# Split the String

```java
String[] data = from.split(",");
```

Result:

```text
data[0] = "john"
data[1] = "2000"
```

---

# Create Employee Object

```java
new Employee("john", 2000)
```

---

# Why `Converter<String, Employee>`?

It means:

```text
Convert FROM String
TO Employee
```

because properties are always read as text first.

---

# Why `@ConfigurationPropertiesBinding`?

This tells Spring:

> “Use this converter when binding configuration properties.”

Without it, Spring may not use your converter during property binding.

---

# Why `@Component`?

So Spring can discover and register the converter automatically.

---

# What Happens Internally

Spring reads:

```properties
conversion.employee=john,2000
```

↓

Uses your converter:

```java
convert("john,2000")
```

↓

Creates:

```java
Employee("john", 2000)
```

↓

Stores it in:

```java
private Employee employee;
```

---

# Final Result

Now you can use:

```java
propertyConversion.getEmployee()
```

and get a real `Employee` object.

---

# Real-Life Analogy

Imagine Spring is reading configuration files written in plain English.

Spring understands common languages like:

* durations
* file sizes
* numbers

But for your custom object:

* Spring needs a translator

Your converter acts as that translator.

---

# Another Example

Suppose:

```properties
app.color=255,0,0
```

You could create:

```java
Color(red=255, green=0, blue=0)
```

using a custom converter.

---

# Simple Summary

A custom converter teaches Spring:

> “How to convert a text property into my custom Java object.”

Example:

```properties
conversion.employee=john,2000
```

↓

converted into:

```java
Employee("john", 2000)
```
- automatically using your custom converter.

---

### Immutable @ConfigurationProperties Binding

- As of Spring Boot 2.2, we can use the @ConstructorBinding annotation to bind our configuration properties, instead of the older setter injection.

- This essentially means that @ConfigurationProperties-annotated classes may now be immutable.

- In Spring Boot 3 if there’s a single parameterized constructor, then constructor binding is implied and we don’t need to use the annotation. But in case of multiple constructors, we must annotate the preferred one:

```java
@ConfigurationProperties(prefix = "mail.credentials")
public class ImmutableCredentials {

    private final String authMethod;
    private final String username;
    private final String password;

    @ConstructorBinding
    public ImmutableCredentials(String authMethod, String username, String password) {
        this.authMethod = authMethod;
        this.username = username;
        this.password = password;
    }

    public ImmutableCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        this.authMethod = "Default";
    }
    public String getAuthMethod() {
        return authMethod;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
```

- As we can see, when using @ConstructorBinding, we need to provide the constructor with all the parameters we’d like to bind.

- Note that all the fields of ImmutableCredentials are final. Also, there are no setter methods.

- Furthermore, it’s important to emphasize that to use the constructor binding, we need to explicitly enable our configuration class either with `@EnableConfigurationProperties` or with `@ConfigurationPropertiesScan`.

---

#### Java 16 Records

- Java 16 introduced the record types as part of JEP 395. Records are classes that act as transparent carriers for immutable data. This makes them perfect candidates for configuration holders and DTOs. As a matter of fact, we can define Java records as configuration properties in Spring Boot. For instance, the previous example can be rewritten as:

```java
@ConstructorBinding
@ConfigurationProperties(prefix = "mail.credentials")
public record ImmutableCredentials(String authMethod, String username, String password) {
}
```

- Obviously, it’s more concise compared to all those noisy getters and setters.

- Moreover, as of Spring Boot 2.6, for single-constructor records, we can drop the @ConstructorBinding annotation. If our record has multiple constructors, however, @ConstructorBinding should still be used to identify the constructor to use for property binding.

---

### Explanation

# Java Records + `@ConfigurationProperties` (Simple Explanation)

This is about using **Java Records** to make configuration classes **short, clean, and immutable**.

---

# First: What is a Record?

A **record** is a special type of class in Java that is:

* automatically immutable (cannot be changed)
* automatically has constructor
* automatically has getters
* no setters needed
* very compact

---

## Normal Class (old way)

```java
public class Employee {

    private final String name;
    private final double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() { return name; }

    public double getSalary() { return salary; }
}
```

---

## Record (new way)

```java
public record Employee(String name, double salary) {
}
```

That’s it.

---

# Now Apply This to Spring Configuration

We use records for configuration because:

* configuration values should NOT change
* records are perfect for immutable data

---

# Example Properties File

```properties
mail.credentials.authMethod=OAuth
mail.credentials.username=john
mail.credentials.password=secret
```

---

# Record-Based Configuration Class

```java
@ConfigurationProperties(prefix = "mail.credentials")
public record ImmutableCredentials(
        String authMethod,
        String username,
        String password
) {
}
```

---

# What Spring Does

Spring automatically does this:

```java
new ImmutableCredentials("OAuth", "john", "secret");
```

using values from `application.properties`.

---

# What the documentation sentence means

> “As of Spring Boot 2.6, for single-constructor records, we can drop `@ConstructorBinding`”

### Simple meaning:

Spring is smart enough now.

If your record has **only one constructor (which records always do)**:

* Spring automatically uses it
* You do NOT need `@ConstructorBinding`

---

## So THIS is enough in modern Spring Boot:

```java
@ConfigurationProperties(prefix = "mail.credentials")
public record ImmutableCredentials(
        String authMethod,
        String username,
        String password
) {
}
```

---

# When DO we need `@ConstructorBinding`?

Only in older or special cases:

### Case 1: Multiple constructors (not common in records, but in classes)

Spring needs help choosing which one to use:

```java
@ConstructorBinding
public ImmutableCredentials(String username, String password) { }
```

---

### Case 2: Older Spring Boot versions (< 2.6)

You had to explicitly tell Spring:

> “Use constructor binding”

---

# Simple Analogy

Think of Spring like a delivery system:

### Old way:

You had to tell it:

> “Use THIS constructor to build the object.”

### New way (records):

Spring says:

> “There is only one way to build it, I’ll use it automatically.”

---

# Key Benefits of Records for Config

* much shorter code
* immutable by default
* safer (no accidental changes)
* perfect for configuration + DTOs

---

# Final Simple Summary

* Records are **short immutable classes**
* Perfect for `@ConfigurationProperties`
* Spring automatically maps properties into them
* `@ConstructorBinding` is mostly NOT needed anymore (Spring Boot 2.6+)
* Only needed in special cases (like multiple constructors or older versions)

---

If you want, I can also show:
Record vs Class vs Lombok `@Data` comparison (very common interview question)
---
