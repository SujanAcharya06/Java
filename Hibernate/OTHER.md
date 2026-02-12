

## Hibernate Caching

![](assets/2026-02-12-09-55-06.png)

![](assets/2026-02-12-09-55-31.png)

![](assets/2026-02-12-09-55-52.png)

- This is L1 cache
![](assets/2026-02-12-09-56-22.png)

- In between fetching if data got changed
	- we have take care when to use cache and when not to

![](assets/2026-02-12-09-57-03.png)

- By default hybernate provides `L1 Cache`

- If there were two sessions
	- and if one contains the fetched data and other does not
	- can these two sessions interact, `NO` by `default` hybernate will not allow it
![](assets/2026-02-12-09-58-49.png)

- In case we need above behaviour, we need to use some external application that provides `L2 cache`
- Hybernate uses `Jcache` to achieve this
	- there are multiple implementations for it
![](assets/2026-02-12-10-02-07.png)

### HQL (Hibernate Query Language)

- Derived from SQL itself
- We can use methods like
	- session.find(Classname.class, primarykey)
	- session.remove(object)
	- session.merge(object)
- If we want to filter in a Student column say with `name`
	- or `marks > threshold` ...
	- for these we have to write queries
	> [!NOTE]
	> we can also write sql queries which are called (native queries)
	- if we do not wish to write queries
		- we can use `HQL`
![](assets/2026-02-12-10-15-23.png)
- for example
	- if we want to fetch everything from table student
	- in SQL, 
		- we do `select * from student`
	- in HQL
		- we do `from Student`
			- here `Student` is not a table it is entity
