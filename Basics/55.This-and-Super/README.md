

### this() and super()
---

### 🔹 `this()` and `super()` — Key Points

* `this()` calls a **constructor of the same class**
* `super()` calls a **constructor of the parent (super) class**

---

### 🔹 First Statement Rule (MOST IMPORTANT)

* Constructor’s **first line must be either**:

  * `this(...)` **OR**
  * `super(...)`
* ❌ You cannot place any statement before them
* ❌ You cannot use both in the same constructor

---

### 🔹 Default `super()` behavior

* If you don’t write `super()`, Java **automatically inserts** it
* Parent constructor **always executes** before child constructor

---

### 🔹 Constructor execution order

* Execution always starts from the **top of the inheritance chain**
* Order is:

  ```
  Object → Parent → Child
  ```

---

### 🔹 `this()` chaining rules

* `this()` is used for **constructor chaining within the same class**
* Constructor chain **must eventually reach `super()`**
* ❌ Infinite constructor loops are not allowed

---

### 🔹 No constructor loops

* ❌ A constructor cannot directly or indirectly call itself
* Java detects constructor loops at **compile time**

---

### 🔹 Using `super(arguments)`

* Used to call a **specific parent constructor**
* Mandatory if parent class has **no no-arg constructor**

---

### 🔹 Constructors are NOT inherited

* Parent constructors **execute**, but are **not inherited**
* You cannot call parent constructor explicitly from child object

---

### 🔹 What is NOT allowed

* Constructors cannot be:

  * `static`
  * `final`
  * `abstract`
* `this()` and `super()` must **not appear outside constructors**

---
