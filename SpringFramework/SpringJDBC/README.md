

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

```java
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.springjdbcdemo.model.Student;

@Repository
public class StudentRepo {

	private static final Logger logger = LoggerFactory.getLogger(StudentRepo.class);

	private JdbcTemplate jdbcTemplate;

	public void save(Student s) {
		String sql = "insert into students(rollNo, name, marks) values(?, ?, ?)";

		int rows = jdbcTemplate.update(sql, s.getRollNo(), s.getName(), s.getMarks());
		logger.info("Rows effected, {}", rows);
	}

	public List<Student> findAll() {
		List<Student> dummyStudent = new ArrayList<>();
		return dummyStudent;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
```
```output
Caused by: org.h2.jdbc.JdbcSQLSyntaxErrorException: Table "STUDENTS" not found (this database is empty); SQL statement:
insert into students(rollNo, name, marks) values(?, ?, ?) [42104-240]
	at org.h2.message.DbException.getJdbcSQLException(DbException.java:514)
	at org.h2.message.DbException.getJdbcSQLException(DbException.java:489)
	at org.h2.message.DbException.get(DbException.java:223)
	at org.h2.message.DbException.get(DbException.java:199)
```
- Create two files in `resources` dir
	- `schema.sql`
		- creating a table...
	- `data.sql`
		- inserting values
			- starting with initial values

```sql
<!-- schema.sql -->
CREATE TABLE students (
	rollno int primary key,
	name varchar(50),
	marks int
);

<!-- data.sql -->
insert into students(rollNo, name, marks) values(101, 'Gojo', 99);
insert into students(rollNo, name, marks) values(102, 'Yuji', 96);
insert into students(rollNo, name, marks) values(103, 'Fushiguro', 96);
```

- When used h2 inmemory database

```output
2026-02-23T19:43:34.434+05:30  INFO 25069 --- [springjdbcdemo] [           main] c.e.s.SpringjdbcdemoApplication          : Starting SpringjdbcdemoApplication using Java 21.0.7 with PID 25069 (/home/sujanacharya/Documents/repos/test/spring-jdbc/springjdbcdemo/target/classes started by sujanacharya in /home/sujanacharya/Documents/repos/test/spring-jdbc/springjdbcdemo)
2026-02-23T19:43:34.436+05:30  INFO 25069 --- [springjdbcdemo] [           main] c.e.s.SpringjdbcdemoApplication          : No active profile set, falling back to 1 default profile: "default"
2026-02-23T19:43:34.759+05:30  INFO 25069 --- [springjdbcdemo] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2026-02-23T19:43:34.844+05:30  INFO 25069 --- [springjdbcdemo] [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:89273966-f3b2-45e8-88c8-e4af8ec31775 user=SA
2026-02-23T19:43:34.846+05:30  INFO 25069 --- [springjdbcdemo] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2026-02-23T19:43:34.970+05:30  INFO 25069 --- [springjdbcdemo] [           main] c.e.s.SpringjdbcdemoApplication          : Started SpringjdbcdemoApplication in 0.774 seconds (process running for 0.972)
2026-02-23T19:43:34.982+05:30  INFO 25069 --- [springjdbcdemo] [           main] c.e.s.StudentRepo.StudentRepo            : 1 : Rows effected 
All students []
2026-02-23T19:43:34.985+05:30  INFO 25069 --- [springjdbcdemo] [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2026-02-23T19:43:35.011+05:30  INFO 25069 --- [springjdbcdemo] [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
[INFO] ------------------------------------------------------------------------
```

### Viewing the existing and inserted data

```java
public List<Student> findAll() {

	String sql = "select * from students";

	RowMapper<Student> rowMapper = new RowMapper<Student>() {

		@Override
		public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
			Student s = new Student();
			s.setRollNo(rs.getInt("rollNo"));
			s.setName(rs.getString("name"));
			s.setMarks(rs.getInt("marks"));
			return s;
		}

	};

	return jdbcTemplate.query(sql, rowMapper);

}
```
- In `raw JDBC` we used `exuecuteQuery(Statement)`, which returns boolean or `ResultSet` based on the update count 
- here in `Spring JDBC` we use `query(statement, rowMapper)`
	- here `statement` -> `Statement`, `PreparedStatemet`, `CallableStatement`
	- `rowMapper` -> is the object of `RowMapper`
		- `RowMapper<T>`
			- is a `Functional Interface`
			- which have single method `mapRow(ResultSet rs, int rowNum)` and throws `SQLException`
			- `ResultSet` maps to single row from select statement and sets it to our new object which we return
			- we can optionally use `rowNum` also for other operations

- Using `lambda` expression

```java
public List<Student> findAll() {

	String sql = "select * from students";

	RowMapper<Student> rowMapper = (rs, rowNum) -> {
		Student s = new Student();
		s.setRollNo(rs.getInt("rollNo"));
		s.setName(rs.getString("name"));
		s.setMarks(rs.getInt("marks"));
		return s;
	};

	return jdbcTemplate.query(sql, rowMapper);
}

// or

public List<Student> findAll() {

	String sql = "select * from students";

	return jdbcTemplate.query(sql, (rs, rowNum) -> {
		Student s = new Student();
		s.setRollNo(rs.getInt("rollNo"));
		s.setName(rs.getString("name"));
		s.setMarks(rs.getInt("marks"));
		return s;
	});

}
```

---
### Connecting database

- We need to specify these properties so that spring can connect to the database

```properties
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=
```

---
### H2 JDBC Configuration (In memory)
- As it is in memory, no need to specify the datasource. 
- If we want we can optionally specify as below

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=xxxx
spring.datasource.initialization-mode=always
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.defer-datasource-initialization=true
```

---
### Postgres JDBC Configuration

- As it is an external database
- we need to specify how spring needs to connect to it
- either by specifying properties through `.xml` or `Java based` or by using `application.properties`
	- when specified in `application.properties` spring sees this at startup and ensures these properties to be applied.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/DATABASENAME
spring.datasource.username=postgresxxx
spring.datasource.password=xxxx
spring.datasource.driver-class-name=org.postgresql.Driver
```

```output
:: Spring Boot ::                (v4.0.3)

2026-02-23T20:44:42.125+05:30  INFO 35207 --- [springjdbcdemo] [           main] c.e.s.SpringjdbcdemoApplication          : Starting SpringjdbcdemoApplication using Java 21.0.7 with PID 35207 (/home/sujanacharya/Documents/repos/test/spring-jdbc/springjdbcdemo/target/classes started by sujanacharya in /home/sujanacharya/Documents/repos/test/spring-jdbc/springjdbcdemo)
2026-02-23T20:44:42.126+05:30  INFO 35207 --- [springjdbcdemo] [           main] c.e.s.SpringjdbcdemoApplication          : No active profile set, falling back to 1 default profile: "default"
2026-02-23T20:44:42.371+05:30  INFO 35207 --- [springjdbcdemo] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2026-02-23T20:44:42.474+05:30  INFO 35207 --- [springjdbcdemo] [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@3cee53dc
2026-02-23T20:44:42.475+05:30  INFO 35207 --- [springjdbcdemo] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2026-02-23T20:44:42.554+05:30  INFO 35207 --- [springjdbcdemo] [           main] c.e.s.SpringjdbcdemoApplication          : Started SpringjdbcdemoApplication in 0.621 seconds (process running for 0.778)
2026-02-23T20:44:42.565+05:30  INFO 35207 --- [springjdbcdemo] [           main] c.e.s.StudentRepo.StudentRepo            : 1 : Rows effected 
2026-02-23T20:44:42.568+05:30  INFO 35207 --- [springjdbcdemo] [           main] c.e.s.SpringjdbcdemoApplication          : All Students: [Student [rollNo=101, name=Gojo, marks=99], Student [rollNo=102, name=Yuji, marks=88], Student [rollNo=103, name=Fushiguro, marks=88], Student [rollNo=104, name=Hakri, marks=98]]
2026-02-23T20:44:42.571+05:30  INFO 35207 --- [springjdbcdemo] [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2026-02-23T20:44:42.572+05:30  INFO 35207 --- [springjdbcdemo] [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```

---
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
