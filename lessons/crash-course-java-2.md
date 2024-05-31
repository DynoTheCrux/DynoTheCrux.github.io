# Control Structures
{: .reading}

In general, commands in a program are called in **sequence**. This does not necessarily imply that a program runs line by line from top to botton, but that we can **follow the program** instruction by instruction.

**Control structures** allow us to divert the program flow according to set conditions. There are two distinct types of control structures: **loops** and **conditionals** (selections).

![Control structures](../assets/img/cc2_prog_flow.gif)

## Conditional Statements
 
**Conditional statements** are used to perform different actions based on different conditions.

### if Statement
The ``if`` statement is one of the most frequently used conditional statements.
If the condition expression evaluates to **true**, the block of code inside the ``if`` statement is executed. If the expression is found to be false, the first set of code after the end of the ``if`` statement (after the closing curly brace) is executed.

Generally, the syntax of an ``if`` statement is
````java
if (condition) {
   // Executes when the condition is true
}
````

The ``condition`` can be comprised of following **comparison operators**:
- ``<`` less than
- ``>`` greater than
- ``==`` equal to
- ``!=`` not equal to
- ``<=`` less than or equal to
- ``>=`` greater than or equal to

> Note: The equals operator is `==`, the assignment operator is `=`.

````java
int v = 35;
...
if(v < 42) {
   System.out.println("Computer says 'Hi'");
}
````

### if...else Statement
An ``if`` statement can be followed by an *optional* ``else`` statement, which executes when the condition evaluates to **false**.

For example:
````java
int age = 30;

if (age < 16) { // 30 < 16 -> false
   System.out.println("Too Young");
} else { 
   System.out.println("Welcome!");
}
````
````plaintext
Welcome!
````

### Nested if Statements
We can use ``if``(-``else``) statements inside another ``if`` or ``else`` statement. This is called **nesting** and we can nest as many ``if``-``else`` as we want.

````java
int age = 25;
if(age > 0) {
   if(age > 16) { // age > 0 and age > 16
      System.out.println("Welcome!");
   } else { // age > 0 and age <= 16
      System.out.println("Too Young");
   }
} else { // age <= 0
   System.out.println("Error");
}
````
````plaintext
Welcome!
````

### else-if Statements

Instead of using nested ``if``-``else`` statements, you can use the ``else if`` statement to check multiple conditions. The code will check the conditions from first to last and execute the code inside the block of the first condition to evaluate to ``true``.

````java
int age = 25;

if(age <= 0) {
   System.out.println("Error");
} else if(age <= 16) {
   System.out.println("Too Young");
} else if(age < 100) {
   System.out.println("Welcome!");
} else { // age >= 100
   System.out.println("Really?");
}
````
````plaintext
Welcome!
````

## Logical Operators

Logical operators are used to combine multiple conditions.

Let's say you wanted your program to output "Welcome!" only when the variable ``age`` is greater than 18 and the variable ``money`` is greater than 500.
One way to accomplish this is to use nesting:
````java
if (age > 18) {
   if (money > 500) {
      System.out.println("Welcome!");
   }
}
````

However, using the **AND** logical operator ``&&`` is a better way:
````java
if (age > 18 && money > 500) {
   System.out.println("Welcome!");
}
````

The available logical operators are
- ``&&`` and
- ``||`` or
- ``!`` not

> ``!(age > 18)`` reads as "if age is NOT greater than 18".

## Loops

A **loop** statement allows to repeatedly execute a statement or group of statements.

### while Loops
A **while loop** repeatedly executes a statement as long as a given condition is true.

Generally, the syntax of an ``while`` loop is
````java
while(condition) {
    // Executes repeatedly as long as condition is true
}
````

The ``while`` loop checks for the condition. If it evaluates to ``true``, it executes the statements within its body. Then it checks for the statement again and repeats:

````java
int x = 3;

while(x > 0) {
   System.out.println(x);
   x--; // decrements x
}
````
````plaintext
3
2
1
````

### for Loops
Another loop structure is the ``for`` loop. A ``for`` loop allows you to efficiently write a loop that needs to execute a specific number of times.

Generally, the syntax of an ``for`` loop is
````java
for(initialization; condition; increment/decrement) {
    // Executes repeatedly
}
````

- **Initialization**: Expression executes **only once** at the beginning of loop
- **Condition**: Is evaluated **each time** the loop iterates. When the condition evaluates to `false`, the repeat execution stops.
- **Increment/Decrement**: Executes **after** iteration of the loop.

Notice the semicolon `;` after initialization and condition in the syntax.

The following example prints the numbers 1 through 5:
````java
for(int x = 1; x <=5; x++) {
  System.out.println(x);
}
````
````plaintext
1
2
3
4
5
````

The variable ``x`` is initialized to the value 1, and the loop body repeatedly prints the value of ``x``, until the condition ``x<=5`` becomes ``false``. On each iteration, the statement ``x++`` increments ``x`` by one.

