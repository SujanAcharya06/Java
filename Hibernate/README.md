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

