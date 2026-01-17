


## Regex


Think of regex as a **tiny language** with:

1. **Character classes** (what to match)
2. **Quantifiers** (how many)
3. **Groups** (capture things)
4. **Modifiers** (greedy vs lazy)

Once you know these, *everything else is just combinations*.

---

# 1️⃣ `\d` — “a digit (number)”

### Meaning

```
\d  → any digit from 0 to 9
```

### Example

```regex
\d
```

Matches:

```
0 1 2 3 4 5 6 7 8 9
```

### In Java you must write:

```java
"\\d"
```

Why?

* `\d` → regex escape
* `\\d` → Java string escape

---

# 2️⃣ `+` — “one or more”

### Meaning

```
+ → 1 or more times
```

So:

```regex
\d+
```

Means:

> “One or more digits”

Matches:

```
1
42
31117
999999
```

---

# 3️⃣ `.` — “any character”

### Meaning

```
. → any character (except newline unless DOTALL)
```

Matches:

```
a
Z
1
@
(space)
```

---

# 4️⃣ `*` — “zero or more”

### Meaning

```
* → repeat 0 or more times
```

So:

```regex
.*
```

Means:

> “Any characters, any length”

---

# 5️⃣ `()` — “capture this”

### Meaning

```
(...) → capture what’s inside
```

Used when you want to **extract** part of a match.

Example:

```regex
<name>(.*)</name>
```

Then:

```java
matcher.group(1)
```

Gives:

```
whatever is inside <name>...</name>
```

---

# 6️⃣ `?` — THREE different meanings (important)

### A) Make something optional

```regex
colou?r
```

Matches:

```
color
colour
```

---

### B) Lazy (non-greedy) quantifier THIS IS YOUR `.*?`

Normally:

```regex
.*
```

is **greedy** → it eats everything

Lazy version:

```regex
.*?
```

means:

> “Match as little as possible”

---

### Example (VERY IMPORTANT)

Text:

```xml
<tag>ONE</tag>
<tag>TWO</tag>
```

#### Greedy

```regex
<tag>(.*)</tag>
```

Matches:

```
ONE</tag>
<tag>TWO
```

####  Lazy

```regex
<tag>(.*?)</tag>
```

Matches:

```
ONE
```
 **That’s why `(.*?)` is used for XML-like data**

---

# 7️⃣ Character sets `[]`

### Meaning

```
[abc] → a or b or c
[a-z] → lowercase letters
[A-Z] → uppercase letters
[0-9] → digits
```

Example:

```regex
[0-9]+
```

Same as:

```regex
\d+
```

---

# 8️⃣ Named shortcuts (THE CHEAT CODES)

| Regex | Meaning                   |
| ----- | ------------------------- |
| `\d`  | digit                     |
| `\D`  | not digit                 |
| `\w`  | letter, digit, underscore |
| `\W`  | not word                  |
| `\s`  | whitespace                |
| `\S`  | not whitespace            |

---

# 9️⃣ Anchors (position-based)

| Symbol | Meaning       |
| ------ | ------------- |
| `^`    | start of line |
| `$`    | end of line   |
| `\b`   | word boundary |

---

# 🔟 Escaping special characters

These are **special**:

```
. * + ? ( ) [ ] { } | \ ^
```

To match them literally:

```regex
\.
\(
\)
```

---

# How many are there REALLY?

You don’t need to memorize everything.

### Core set (90% of use cases):

```
\d \w \s
. * + ?
( )
[ ]
^ $
```

That’s it.

---

# Your XML example — fully decoded

```regex
<jsdlejb:servername>(.*?)</jsdlejb:servername>
```

| Part                    | Meaning                            |
| ----------------------- | ---------------------------------- |
| `<jsdlejb:servername>`  | literal text                       |
| `(.*?)`                 | capture as little text as possible |
| `</jsdlejb:servername>` | closing tag                        |

---
