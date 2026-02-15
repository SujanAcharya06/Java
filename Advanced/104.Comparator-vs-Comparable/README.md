

# Comparator vs Comparable in Java

This guide explains the difference between Comparable and Comparator in simple terms, with examples and when to use each.

---

## 1. Why do we need sorting?

In Java, we often need to sort objects such as:

* Students by marks
* Employees by salary
* Products by price

Java provides built-in sorting methods, but it needs to know **how to compare objects**. That is where Comparable and Comparator come in.

---

## 2. Comparable

### Definition

Comparable is an interface used to define the **natural or default sorting order** of objects.

It is implemented inside the class whose objects need to be sorted.

### Key idea

The class itself knows how it should be sorted.

### Method in Comparable

```
int compareTo(Object obj)
```

It compares the current object with another object.

---

## 3. How compareTo works

It returns:

* Negative value → current object is smaller
* Zero → both are equal
* Positive value → current object is greater

---

## 4. Example: Comparable

Sort students by marks.

```
import java.util.*;

class Student implements Comparable<Student> {
    int marks;
    String name;

    Student(int marks, String name) {
        this.marks = marks;
        this.name = name;
    }

    @Override
    public int compareTo(Student s) {
        return this.marks - s.marks;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();

        list.add(new Student(80, "A"));
        list.add(new Student(60, "B"));
        list.add(new Student(90, "C"));

        Collections.sort(list);

        for (Student s : list) {
            System.out.println(s.marks);
        }
    }
}
```

Here:

* Sorting logic is inside the Student class.
* This is called natural ordering.

---

## 5. When to use Comparable

Use Comparable when:

* You want only one default sorting
* The class has a clear natural order
* Example: sorting by ID or marks

---

## 6. Limitations of Comparable

Problem:
What if you want to sort the same object in multiple ways?

For example:

* By marks
* By name
* By age

Comparable allows only one.

This is where Comparator comes in.

---

## 7. Comparator

### Definition

Comparator is an interface used to define **multiple sorting orders**.

It is written outside the class.

### Key idea

Sorting logic is separate from the class.

### Method in Comparator

```
int compare(Object o1, Object o2)
```

---

## 8. Example: Comparator

Sort students by name.

```
import java.util.*;

class Student {
    int marks;
    String name;

    Student(int marks, String name) {
        this.marks = marks;
        this.name = name;
    }
}

class NameComparator implements Comparator<Student> {
    public int compare(Student s1, Student s2) {
        return s1.name.compareTo(s2.name);
    }
}

public class Main {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();

        list.add(new Student(80, "A"));
        list.add(new Student(60, "B"));
        list.add(new Student(90, "C"));

        Collections.sort(list, new NameComparator());

        for (Student s : list) {
            System.out.println(s.name);
        }
    }
}
```

Here:

* Sorting logic is outside the Student class.
* We can create multiple comparators.

---

## 9. Comparator using Lambda (modern Java)

```
Collections.sort(list, (s1, s2) -> s1.name.compareTo(s2.name));
```

This is shorter and commonly used today.

---

## 10. Differences between Comparable and Comparator

| Feature      | Comparable           | Comparator       |
| ------------ | -------------------- | ---------------- |
| Package      | java.lang            | java.util        |
| Method       | compareTo            | compare          |
| Location     | Inside class         | Outside class    |
| Sorting      | Single default       | Multiple sorting |
| Flexibility  | Less                 | More             |
| Modification | Need to modify class | No need          |
| Use case     | Natural order        | Custom order     |

---

## 11. Real world analogy

Comparable:
The person decides their own ranking.

Comparator:
Different judges rank the person differently.

---

## 12. Interview summary

Comparable:

* Defines natural sorting
* Implemented in the class
* Uses compareTo

Comparator:

* Defines custom sorting
* Separate class
* Uses compare

---

## 13. Best practices

* Use Comparable when there is one clear natural order.
* Use Comparator when sorting logic may change.
* In modern Java, prefer Comparator with lambda.

---

## 14. Conclusion

Comparable and Comparator both help sort objects.

Comparable is simple and used when one default order is needed.

Comparator is flexible and used when multiple sorting strategies are required.

Understanding both is important for coding interviews and real-world Java applications.

---
