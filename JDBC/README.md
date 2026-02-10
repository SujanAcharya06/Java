## JDBC steps


#### 1. Import Packages
- `import java.sql.*`
#### 2. Load Driver
- `Class.forname("org.postgresql.Driver")` -> throws `ClassNofoundException`
#### 3. Register Driver
- loading and registering driver is optional after java version  6 

#### 4. Create Connection
```java
Connection con;
con.DriverManager.getConnection(url, user, password);
```

#### 5. Create Statement
#### 6. Execute Statement
#### 7. Close

```java
String selectStatement = "select * from student";
String insertStatement = "insert into student values(6, 'Maki', 96)";
String updateStatement = "update student set sname='Kenjaku' where sid=6";
String deleteStatement = "delete from student where sid=5";


Statement st = con.createStatement();

// for select statement we can also use execute()
ResultSet rs = st.executeQuery(selectStatement);
while (rs.next()) {
	int id = rs.getInt("sid");
	String sname = rs.getString("sname");
	int marks = rs.getInt("marks");
	log.info("id | name | marks | {},{},{}", id, sname, marks);
}

// for insertStatement
st.execute(insertStatement); // true if the first result is a ResultSet object
// here rs1 will be false as the insertStatement executes it returns a update count
// same for updateStatement as well
st.execute(updateStatement);

// same for deleteStatement
st.execute(deleteStatement);
con.close()
```
- for Select Statement
	- we can use `execute()` method
	- it will return
		> [!NOTE]
		> `true` if the first result is a `ResultSet` object; `false` if the first result is an update count or there is no result
	- we can also use `executeQuery(sqlstatement)` -> sqlStatement string
		- this will return `ResultSet`
			- the resultSet object points just before the first record if any record present.
			- we can use `while(rs.next())` to iterate through all the rows returned
				- we can use an inner for loop to iterate through the columns
					- there is a method called `rs.getString(columnLabel)` -> returns String 
						- we can pass column number or column name directly
						- prefer column number here
					- if the column name returns `int` we can use `rs.getInt(columnlabel)`


### PreparedStatement
- When we get user inputs or dynamically we put the values into database 
- if we use normal `Statement`
- The insert statement would look something like this
```java

String insertStatement = "insert into student values(" + sid + ", '" + sname + "'," + marks + ")"; 

Statement st = con.createStatement();

ResultSet rs = st.execute(insertStatement);
```

- Chance of `SQL Injections` if user passes any unwanted strings 

- Using `PreparedStatement`
```java
Connection con;

String insertStatement = "insert into student values(?, ?, ?)";

PreparedStatement pst = con.prepareStatement(insertStatement); // gives a precompiled query which we can use it for caching purpose
pst.setInt(1, sid);
pst.setString(2, sname);
pst.setInt(3, marks);

pst.execute();

con.close();
```

- We have three kinds of Statements
1. `Statement`
	- anything that deals with updating the table itself, like the table structure
		- dropping table or altering the table 
	- for select query also we can use statement
2. `PreparedStatement`
	- for other statements
	- for select when we have a `where` clause always go for `PreparedStatement`
3. `CallableStatement`
	- used to execute `stored procedures`

- `PreparedStatement` is an `inteface` which `extends Statement`
