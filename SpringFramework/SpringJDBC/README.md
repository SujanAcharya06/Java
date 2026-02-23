

## Spring JDBC

- Initial Dependency
```xml
<!-- JDBC API -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<!-- H2 Database -->
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<scope>runtime</scope>
</dependency>
```

- Creating `Student` class
```java
@Component
public class Student {
	private int rollNo;
	private String name;
	private int marks;

	public int getRollNo() {
		return rollNo;
	}

	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}

	@Override
	public String toString() {
		return "Student [rollNo=" + rollNo + ", name=" + name + ", marks=" + marks + "]";
	}
}

```
- After adding `h2 database` dependency, should we add any additional configuration, no
	- h2 gives `connection pooling` with the help of `hikari`
	- it will give datasource as well
	- gives default in memory database

### JDBCTemplate


## Steps if normal JDBC is used
1. Load Driver
2. Define Connection URL
3. Establish Connection
4. Create Statement Object
5. Execute Query Using Statement
6. Process Result
7. Close Connection

- To solve this problem we use Spring JDBC
	- Templates, Components

# JdbcTemplate
- This template will help to
		1. Connect with database
		1. Fire Queries
		1. Process data
		1. get output

- Data Source -> using some libraries to reuse the connections
