## Abstract-Keyword

- If we are declaring an abstract method, it must be implemented in the subclass that extends it
- An abstract method must be declared inside an abstract class
- If we are exteding an abstract class we must define/override the abstract method
- We can't create object of abstract class
	- but we can have a `constructor` of abstract class
	- this is because the child class can instantiate it
		- we can have a reference type of abstract class and the object type of child class
			- `A obj = new B()`
- We can create a reference of abstract of class
- It is not compulsory to for an abstract class to have abstract method
- If a car cannot implement all the methods then that class can be made abstract class as well
