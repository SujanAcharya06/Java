## Hibernate

- ORM(Object Relational Mapping) framework
	- ORM understands the object
		- the class represents -> entity like a table
		- the fields represents the column names

![](assets/2026-02-10-17-50-52.png)
- we can implement this using hibernate

- `Session`
	- is an interface
	- using its reference we can process the object

- we can use `save()` method to save the object to database
> [!NOTE]
> this method has been deprecated 6.6.3, and removed in 7.1.0, we have to use `session.persist(object)`


- To create a `Session` we have to follow some steps
	- Who can create a `Session`
		- it can be created by a `SessionFactory`
		- `SessionFactory`
			- is an interface 
	- we can create a reference of `Session` using `SessionFactory`'s `openSession()` method
		- there are other methods as well like `getCurrentSession()` which gives the reference to current open session
		- to connect to a database and perform some actions is costly 
		- we can however any number of sessions
			- as for any unit of work we will be using a new session 
		- `SessionFactory` is a heavy weight object, it will consume lot of resources
	- To get the reference of `SessionFactory` we need to first get an object of `Configuration`
		- `Configuration`
			- > [!NOTE]
			> org.hibernate.cfg
			- is a class
			- we can create its object by using `new` keyword
			- once we get the `Configuration` object we can call the `SessionFactory`'s `buildSessionFactory()` method
				- which will give the object of `SessionFactory`

- for configuring jdbc so that hibernate can talk to database, we need to configure the database details, by default it will search for `hibernate.cfg.xml`
	- we can use different names
- if we don't we get below error
	- by default 
	- `Could not locate cfg.xml resource [hibernate.cfg.xml]`
![](assets/2026-02-10-18-52-36.png)
```java
<hibernate-configuration xmlns="http://www.hibernate.org/xsd/orm/cfg">
	<session-factory>
		<property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
		<property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/dbname</property>
		<property name="hibernate.connection.username">db_username</property>
		<property name="hibernate.connection.password">passwd</property>

		// <property name="hibernate.hbm2ddl.auto">update</property> // in production we won't use this
		// <property name="hibernate.hbm2ddl.auto">create-drop</property> // will create and drop each time 
		<property name="hibernate.hbm2ddl.auto">create</property> 

		<property name="hibernate.diablect">org.hibernate.dialect.PostgresPlusDialect</property>

		<property name="hibernate.show_sql">true</property>
		<property name="hibernate.format_sql">true</property>
	</session-factory>
</hibernate-configuration>
```
- `cfg.configure()`
	- to configure hibernate related details
- After adding this we still get error
	- `Unable to locate perister: com.example.Student`
		`Caused by: org.hibernate.UnknownEntityTypeException: Unknown entity type: com.example.hibernate_test.Student`
- We need to map/add our class we have to add the configuration 
	- we can do this in two ways
		1. by adding the property in `hibernate.cfg.xml`
		2. using `cfg.addAnnotatedClass(com.example.hibernate_test.Student.class);`
	- Even after adding the above configuration
		- we still get the same error as before
	- So if we want this class to be managed by the hibernate, we have to use the Annotation `@Entity`
		- now we get this error
![](assets/2026-02-10-20-12-37.png)
	- `Caused by: org.hibernate.AnnotationException: Entity 'com.example.hibernate_test.Student' has no identifier (every '@Entity' class must declare or inherit at least one '@Id' or '@EmbeddedId' property)`
	- So while we are using `s.perist(s1)`
		- We are doing a transaction
			- every transaction should be `committed`
			- we can use `commit()` method on the `Transaction` reference
		- `Transaction`  -> `org.hibernate`
			- is an interface
			- we can create a reference of it by using session object 
				- the method would be `session.beginTransaction()`
					- there is also other method -> `session.getTransaction()`
			- once the `perist()` method is called
				- we can call `transaction.commit()` which will commit the data

> [!NOTE]
> Do not use spring-boot-devtools dependency along with hibernate

> [!NOTE]
> we can close the `Session` object and then `SessionFactory` object 

> [!NOTE]
> `SessionFactory` is a heavy resource object, we can use `try with resources` so that the resources are closed automatically
- as of now we use `close()` method

### Fetching The data

- When `fetching` values we don't need `Transaction` reference
	- the reference is needed only when we manipulate the database 

- We can use `get()` method which will return the `Object`
	- `get(Type.class,primaryKey)` method is `deprecated` in 7.1.0 but not yet removed
		- Type.class -> the type of object to be returned
		- primaryKey -> Id
		> [!NOTE]
		> we can use `find()` method
		- `find(Type.class, primaryKey)`;
	- If we fire this query and `don't` find anything then we get `null`
		- then if we try to perform any action on this we get
			- `NullPointerException`
	
### Update and Delete Records

- We use `update()` but this is deprecated
	- we need to use `merge()` method
> [!NOTE]
> update and delete actions must be performed inside a transaction
-	 if the data is not there, hibernate will first perform a `select` query and then `insert` query
```sql
Hibernate: 
    select
        s1_0.rollNo,
        s1_0.sAge,
        s1_0.sName 
    from
        Student s1_0 
    where
        s1_0.rollNo=?
Hibernate: 
    insert 
    into
        Student
        (sAge, sName, rollNo) 
    values
        (?, ?, ?)
Student [rollNo=105, sName=Todo, sAge=26]
```

- For deleting
	- we have `delete()` method which was deprecated
		- we can use `remove(Object o)` method which will take Object type
		- if we have only primary key
			- we can first use `find()` method to get the object then use this object to delete`remove()` that particular record only

### Changing Table and Column names

- If `<property name="hibernate.hbm2ddl.auto">update</property>`
	- it will create if the table is not there, if it is there it will just update the record
```log
Hibernate: 
    create table Alien (
        aId integer not null,
        aName varchar(255),
        aTech varchar(255),
        primary key (aId)
    )
Hibernate: 
    insert 
    into
        Alien
        (aName, aTech, aId) 
    values
        (?, ?, ?)
Alien [aId=101, aName=Jhon, aTech=Data Engineering]
```

- If `<property name="hibernate.hbm2ddl.auto">create</property>`
	- hibernate will check if the table is already present or not
	- if present it will will drop
		- basically it will create new table each time the app runs
```log
Hibernate: 
    drop table if exists Alien cascade
Hibernate: 
    create table Alien (
        aId integer not null,
        aName varchar(255),
        aTech varchar(255),
        primary key (aId)
    )
2026-02-11T07:04:00.405+05:30  INFO 20966 --- [demo] [           main] org.hibernate.orm.connections.access     : HHH10001501: Connection obtained from JdbcConnectionAccess [org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess@39a2e77d] for (non-JTA) DDL execution was not in auto-commit mode; the Connection 'local transaction' will be committed and the Connection will be set into auto-commit mode.
Hibernate: 
    insert 
    into
        Alien
        (aName, aTech, aId) 
    values
        (?, ?, ?)
Alien [aId=101, aName=Jhon, aTech=Data Engineering]
```

- From the class name we get the entity name
	- We have three different layers
		- `Class name`
		- `Entity name`
		- `Table name`
- The `table name` we get it from the `Entity name`
	- Entity name we get it from `class` name by default

- We can change the `Entity` name, if we don't want our class name to be the `Entity` name using below syntax
	- `@Entity(name=entity_name)`
		- this will create a table with name `entity_name`
- If we do not wish to change the Entity name and only wish to change the column name we can do it using
	-  annotation `@Table(name='new_table_name')`

- Hibernate by default considers all variables and maps it to columns
- If we do not wish to store any of the variable, if we require it for processing purpose
	- we can use `@Transient` annotation on top the variable name

### `@Embeddable`

- If we have a complex type other than basic types, hibernate get confused on which type to use and throws this exception
	`Exception in thread "main" org.hibernate.type.descriptor.java.spi.JdbcTypeRecommendationException: Could not determine recommended JdbcType for Java type 'com.example.hibernate_test.Laptop'`
- We can use `@Embeddable` on top of this complex type here `Laptop`
	- we are trying to embed `Laptop` details inside `Alien`
```sql
Hibernate: 
    drop table if exists Alien cascade
Hibernate: 
    create table Alien (
        aId integer not null,
        ram integer,
        aName varchar(255),
        aTech varchar(255),
        brand varchar(255),
        model varchar(255),
        primary key (aId)
    )
Hibernate: 
    insert 
    into
        Alien
        (aName, aTech, brand, model, ram, aId) 
    values
        (?, ?, ?, ?, ?, ?)
Alien [aId=101, aName=Jhon, aTech=Data Engineering, laptop=Laptop [brand=Asus, model=Tuf, ram=16]]
```

- Now, here when we try to fire a select query, hibernate does a insert first based on these logs
```logs
Hibernate: 
    drop table if exists Alien cascade
Hibernate: 
    create table Alien (
        aId integer not null,
        ram integer,
        aName varchar(255),
        aTech varchar(255),
        brand varchar(255),
        model varchar(255),
        primary key (aId)
    )
2026-02-11T07:53:16.978+05:30  INFO 34302 --- [demo] [           main] org.hibernate.orm.connections.access     : HHH10001501: Connection obtained from JdbcConnectionAccess [org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess@37196d53] for (non-JTA) DDL execution was not in auto-commit mode; the Connection 'local transaction' will be committed and the Connection will be set into auto-commit mode.
Hibernate: 
    insert 
    into
        Alien
        (aName, aTech, brand, model, ram, aId) 
    values
        (?, ?, ?, ?, ?, ?)
Alien [aId=101, aName=Jhon, aTech=Data Engineering, laptop=Laptop [brand=Asus, model=Tuf, ram=16]]
```
