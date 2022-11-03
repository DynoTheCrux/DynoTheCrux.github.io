# Welcome to Java
{: .reading}

**Java** is a high level programming language designed in the 1990s by Sun Microsystems, and is currently owned by Oracle.

Java is **platform independent**, which means that in many cases you only need to write the program once to be able to run it on a number of different platforms.
Java is **portable**, **robust**, and **dynamic**, with the ability to fit the needs of virtually any type of application.

> Write Once, Run Anywhere

Java is used to develop apps for various Desktop Applications, such as media players, antivirus programs, Web Applications and Enterprise Applications (i.e. banking).

It has also be adopted as the language of choice for Google's **Android** OS, which we will be focus on in this course.

# Hello World!
{: .reading}

The infamous first example when learning a new programming language is to create a minimal programm which outputs the text `Hello World!` on the screen.

````java
class MyClass {
  public static void main(String[] args) {
    System.out.println("Hello World!");
  }
}
````
````plaintext
Hello World!
````
[>Try it yourself<](https://repl.it/@m0stlyharmless/MCIAppExHelloWorld#Main.java){:target="_blank"}

[>Try it yourself<](https://replit.com/@SimWi/MCITest#Main.java){:target="_blank"}


In Java, every line of code that can actually run needs to be inside a `class`.
In our example, we named the class `MyClass`. We will explore the concept of classes in more detail later.

In Java, each application has an **entry point**, or a starting point, which is a [method](https://en.wikipedia.org/wiki/Method_(computer_programming) "a named collection of statements that are grouped together to perform an operation (also called function or procedure)"){:target="_blank"} called `main`. Along with `main`, the keywords `public` and `static` will also be explained later.

> #### As a summary
> - Every program in Java must have a **class**.
> - Every Java program starts from the **main method**.

## The *main* Method

To run our program, the ``main`` method must be identical to this signature:

````java
public static void main(String[] args)
````

Let's dissect this line word by word:
- ``public``: anyone can access it
- ``static``: method only exists once for each class and can be accessed without create an [instance](https://en.wikipedia.org/wiki/Instance_(computer_science) "an object is called an 'instance' of a class"){:target="_blank"} of that class
- ``void`` method does not return any value
- ``main`` name of the method
- ``String[]`` type of the method's parameter
- ``args`` name of the method's parameter

For example, the code `void test()` declares a method called ``test``, which does not return anything and has no parameters:

## System.out.println()

Next is the *body* of the main method, enclosed in curly braces ``{ }``:

````java
{
   System.out.println("Hello World!");
}
````
- The ``println`` method prints a line of text to the screen.
The ``System`` class and its ``out`` stream are used to access the ``println`` method.

> In classes, methods, and other flow-control structures, code is always enclosed in curly braces ``{ }``.

**Note:** In Java, each code statement must end with a semicolon ``;``.

## Comments

The purpose of including comments in your code is to explain what the code is doing.
Java supports both single and multi-line comments. All characters that appear within a comment are ignored by the Java compiler.

A **single-line comment** starts with two forward slashes ``//`` and continues until it reaches the end of the line.

For example:
````java
// This is a single-line comment
````
````java
int ans = 42; // A single-line comment after code
````

Java also supports **multi-line comments** (also called *block comment*). You start it with ``/*``, and end it with ``*/``.

````java
/*  This is also a
    comment spanning
    multiple lines */
````

> Adding comments as you write code is a good practice, because they provide clarification and understanding when you need to refer back to it, as well as for others who might need to read it.

# Variables in Java
{: .reading}

Variables store data for processing.
A variable is given a name (or **identifier**), such as area, age, height, and the like. The name *uniquely* identifies each variable, assigning a value to the variable and retrieving the value stored.

Variables have **types**. Some examples:
- ``int``: for integers (whole numbers) such as 42 and -1337
- ``double``: for floating-point or real numbers with optional decimal points and fractional parts in fixed or scientific notations, such as 3.1416, -67.33.
- ``String``: for texts such as "Hello" or "Good Morning!". Text strings are enclosed within double quotes `"`. Note 'S' is a capital letter!
- ``char``: for single characters such as 'a' or 'A'
- ``boolean``: two possible values `true` and `false`

You can declare a variable of a type and assign it a value (**initialization**):
````java
String name = "Alice";
int answer = 42;
double pi = 3.14159;
char c = 'A';
boolean finished = false;
````

It is also possible to defer assigning a value to a later time. However, it is important to initialize a variable **before** you can access (read) the variable.

The following code will **not** work
````java
String drinks; // not initialized
int amount = 4;

System.out.println("Let's have " + amount + " " + drinks); // Illegal!
````
````plaintext
error: variable answer might not have been initialized
````

Let's fix the code by properly initializing the variable ``drinks`` before the first usage:
````java
String drinks; // not initialized
int amount = 4;

drinks = "beers"; // initialization
System.out.println("Let's have " + amount + " " + drinks);
````
````plaintext
Let's have 4 beers
````
[>Try it yourself<](https://repl.it/@m0stlyharmless/MCIAppExInit#Main.java){:target="_blank"}

It is important to note that a variable is associated with a type, and **is only capable of storing values of that particular type**. For example, an ``int`` variable can store integer values, such as ``123``; but it cannot store real numbers, such as ``12.34``, or texts, such as "Hello".

## Type Casts
We know from the previous section that variables only can hold values that match their type. However, sometimes a different type is needed than the variable was defined with.

````java
double pi = 3.141592;

// an int cannot hold a value of type double
int x = pi; // Illegal

System.out.println(x);
````
````plaintext
error: incompatible types: possible lossy conversion from double to int
````

The error states that ``int`` and ``double`` are incompatible and that a conversion would result in **loss of information** (the fraction part. But what if we are aware of what is happening and we accept the loss of information because we only care about the integer part of ``pi``? This is were **type casts** come in. A **type cast** can **temporarily** change the type of a value of a variable, if the two types can be converted. Note that the variable itself remains unchanged, only it's type representation is changed. The syntax is the new type inside parenthesis in front of the value to be converted: e.g. ``(int) 3.14;``

````java
double pi = 3.141592;

// we can type cast a double to an int
// we loose the fraction part of the double
int x = (int) pi;

System.out.println(x);
````
````plaintext
3
````

Some conversions can be done implicitly, generally when a value can be fully represented as another type. For example: Storing the integer value ``3`` can be stored in a floating point variable without any loss of information: ``3.0``; In this case, no type casts are needed.

# Operators
{: .reading}

## Math Operators
Java provides **operators** to use in manipulating variables. A value used on either side of an operator is called an **operand**.
For example, in the expression below, the numbers ``4`` and ``5`` are operands of the `+` operator:
````java
int x = 4 + 5;
````

The arithmetic operators `+`, `-`, `*`, `/`, `%` (modulo) work as you would expect in algebraic equations. Just like in algebra, you can use multiple the operations in a single line to write an equation. For example:
````java
int val = 10 + 5 - 2;
````
The operator priority the same as in mathematics. To force a custom priority, round brackets ``( )`` can be used.
````java
int val = 4 * 2 + 5;   // = 8 + 5 = 13
int val = 4 * (2 + 5); // = 4 * 7 = 28
int val = ((1 + 2) * (5-3)) / (2 + 3);
````

## The Modulo Operator

The **modulo** (or remainder) math operation performs an integer division of one value by another, and returns the remainder of that division. The operator for the modulo operation is the percentage character `%`.

````java
int div = 23 / 6; // integer division is 3
int rem = 23 % 6; // remainder is 5

System.out.println("23 / 6 is " + div + " with " + rem + " rest.");
````
````plaintext
23 / 6 is 3 with 5 rest.
````
[>Try it yourself<](https://repl.it/@m0stlyharmless/MCIAppExModulo#Main.java){:target="_blank"}

## Increment Operators

An **increment** or **decrement** operator provides a more convenient and compact way to increase or decrease the value of a variable by **one**.
For example, the statement ``x=x+1;`` can be simplified to ``x++``;

````java
int ans = 41;
ans++; // ans is now 42

System.out.println("The answer to the Ultimate Question of Life, The Universe, and Everything is " + ans + ".");
````
````plaintext
The answer to the Ultimate Question of Life, The Universe, and Everything is 42.
````

Similarly, the **decrement** operator ``--`` is used to decrease the value of a variable by **one**:
````java
int x = 5;
x--; // x is now 4
````

## Assignment Operators

We are obviously already familiar with the assigment operator `=`, which assigns a value to a variable.

Java also provides a set of **combined** assignment operators for convenience: `+=`, `-=`, `*=`, `/=` and `%=`. They perform the relevant arithmetic operation **and assign the result** to the variable. Example

````java
int num1 = 4;
int num2 = 8;

num2 += num1; // num2 = num2 + num1;
num2 -= 2;    // num2 = num2 - 2;
num1 *= 5;    // num1 = num1 * 5;
num1 /= num2; // num1 = num1 / num2;
````

# Strings
{: .reading}


A ``String`` is an object that represents a **sequence of characters**.
For example, "Java" is a string of 4 characters.

````java
String s = "Java";
````

> We are allowed to define an empty string. For example, ``String str = "";``

## String Concatenation

in fact, we already used string concatenation. The ``+`` operator between strings adds them together to make a new string. This process is called **concatenation**.
The resulting string is the first string put together with the second string.

````java
String firstName, lastName;
firstName = "Arthur";
lastName = "Dent";

System.out.println("My name is " + firstName + " " + lastName);
````
````plaintext
My name is Arthur Dent
````
[>Try it yourself<](https://repl.it/@m0stlyharmless/MCIAppExStringConc#Main.java){:target="_blank"}
