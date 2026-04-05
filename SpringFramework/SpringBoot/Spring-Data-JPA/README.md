


## Spring Data JPA

- ORM (Obect Relational Mapping)
	- Using this we can directly convert the class to a table
	- the fields to the columns
	- and each object to a record
- We get a table by using the class

- We can do this with the help of ORM tools
	- for example
		- Hibernate
		- toplink
		- eclipse link

- Say we have used hibernate and if we like to change from hibernate to a different tool
	- we do not have to change lot of our code if we are using specification (JPA), as we have not specifically writing for a particular tool

- In `Spring` we can use `Spring ORM` to achieve this

### Spring Data JPA methods

1. `save()`
2. `findAll()`
3. `findById(Integer id)`
	- returns `Optional<T>` 
		- `Optional` handles `null` values as well

### JPQL Query

- Say from this student class
```java
@Component
@Scope("prototype")
@Entity
public class Student {
	@Id
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
		return "Student{" +
				"rollNo=" + rollNo +
				", name='" + name + '\'' +
				", marks=" + marks +
				'}';
	}
}
```
- We need to get a List of students by their name or marks
- In the `student Repo` we can make use of this
```java
public interface StudentRepo extends JpaRepository<Student, Integer> {

	@Query("select s from Student s where s.name ?1")
	List<Student> findByName(String string);

}
```
- We have to make use of `@Query("select s from Student s where s.name ?1")` annotation and specify the query(JPQL) we want to use
- the `?1` replaces the argument and so on
- So here `Student s` refers to the `arise`

- Even if we remove the `@Query` annotation it works as
- JPA uses a domain specific language (DSL)
- Using this DSL it creates certain `methods` behind the scenes
- It will look for the column name or the property name(the fields inside the class)
	- for example we can have property `name`, `marks` inside a student class
	- It will create these queries directly and we can use those methods
> [!NOTE]
> It should begin with `findBy...`
- And we mention the `variable` name for example
	- `findByName()`
	- `findByMarks()`

- If we would like to get data based on certain `constraints`
- Say for example we need to get all students whose marks is greater than 90
- JPQL have methods for them as well we can use `findByMarksGreaterThan(Integer id)`
```java
@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {

	// @Query("select s from Student s where name = 'Yuji'")
	List<Student> findByName(String string);

	List<Student> findByMarksGreaterThan(Integer id);

}
```

### Update and Delete

- For updating the data as well we can use the `save(Entity)` method
- We are just using the object and calling this method
- Before calling the update method JPA fires a select query to check whether the data we want to update exists then it fires a update query
```logs
Hibernate: select s1_0.roll_no,s1_0.marks,s1_0.name from student s1_0 where s1_0.roll_no=?
Hibernate: update student set marks=?,name=? where roll_no=?
```

- For Deleting the data we have `delete(Entity)` method
- and similar to `update` it checks if the record is present then fires the delete query 
```logs
Hibernate: select s1_0.roll_no,s1_0.marks,s1_0.name from student s1_0 where s1_0.roll_no=?
Hibernate: delete from student where roll_no=?
```

