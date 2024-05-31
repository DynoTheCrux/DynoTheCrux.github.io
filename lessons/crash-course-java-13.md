# String formatting
{: .reading}

Displaying text to the user is the most common way of conveying the applications current state. However, static text that was written by the developer ahead of time can only contain static information. Usually, we also want to include a dynamic element, i.e. the **content of a variable**.

## String concatenation

Up until this point, we used the `+` operator to concatenate two ``String`` objects or a ``String`` object with a **number**.

````java
int answer = 42;
System.out.println("The Answer to the Ultimate Question of Life, the Universe, and Everything is " + answer)
````
````plaintext
The Answer to the Ultimate Question of Life, the Universe, and Everything is 42
````

This approach has a few downsides, mainly it's inflexible and slow. In the right (or *wrong*) conditions, it can slow down the program significantly.

### String concatenation is inflexible

Compare following code snippets. In the first case we use a precise value of `pi` to perform a precise circumference calculation.
````java
double pi = 3.14159265359;
double r = 2.0;

System.out.println("The cicumference is " + (2*r*pi));
````
````plaintext
The cicumference is 12.56637061436
````
For the user, the output of the precise value is not very readable. When we lower the precision of our representation of the value `pi`, we get more human readable output, but at the expense of our calculation precision.

````java
double pi = 3.1;
double r = 2.0;

System.out.println("The cicumference is " + (2*r*pi));
````
````plaintext
The cicumference is 12.4
````

>Using the concatenation operator `+` we can only choose **one or the other**

# String formatting

**String formatting** is a common solution and most programming languages offer it in some form or the other (e.g. `printf` in *C*).

In **Java**, the method `String.format(...)` offers the functionality to customize the formatting of variables using special conversion placeholder strings (e.g. `%s`, `%d`).

````java
String s = String.format("%s = %d", "joe", 35);
// s contains "joe = 35"
````

This provides better flexibility and customization options. `String.format(...)` results in a `String` value being created. If we want to print text directly to the console, `System.out.printf(...)` can be used instead.

````java
String s = String.format("%s = %d", "joe", 35);
System.out.println(s);

// equivalent
System.out.printf("%s = %d\n", "joe", 35)
````
````plaintext
joe = 35
joe = 35
````
> Notice the newline character `\n` in case of `System.out.printf(...)` as it does not automatically append a newline.

## Common conversion placeholders

There are many format conversions available with lots of options each. You are encouraged to have a look in the [official documentation](https://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html){:target="_blank"}.

The most important conversion placeholders for our use case are
- Strings: `%s`
- Integers: `%d`
- Floating point numbers: `%f`

````java
System.out.printf("%s finished the race in %d hours, %d minutes and %f seconds.\n", "Alice", 2, 35, 4.42);
````
````plaintext
Alice finished the race in 2 hours, 35 minutes and 4.420000 seconds.
````

### Number formatting

The formatting of numbers, especially floating point numbers, can be adjusted further using the general syntax

````nohighlight
%[flags][width][.precision]conversion
````

Flags modify the style, the `width` parameter determines the minimum number of characters used to print the value (including decimal point, negative sign etc.), the `precision` parameter determines the precision (digits after the decimal point).

| Flag    | Description |
|---------|-------------|
| `-`     | The result will be left-justified.
| `+`     | The result will always include a sign.
| <code>&nbsp;</code> (space)    | The result will include a leading space for positive values.
| `0`     | The result will be zero-padded.

### Examples


````java
int h = 2;
int m = 35;
double s = 4.42;
````

**Precision for floating point numbers**
````java
System.out.printf("%s finished the race in %d hours, %d minutes and %.1f seconds.\n", "Alice", h, m, s);
System.out.printf("%s finished the race in %d hours, %d minutes and %.2f seconds.\n", "Alice", h, m, s);
````
````plaintext
Alice finished the race in 2 hours, 35 minutes and 4.4 seconds.
Alice finished the race in 2 hours, 35 minutes and 4.42 seconds.
````

**Width parameter (8)**
````java
System.out.println("Alice finished the race in");
System.out.printf("%8d hours\n", h);
System.out.printf("%80d minutes\n", m);
System.out.printf("%8.2f seconds\n", s);
````
````plaintext
Alice finished the race in
       2 hours
      35 minutes
    4.42 seconds
````
**Width parameter (4)**
````java
System.out.println("Alice finished the race in");
System.out.printf("%4d hours\n", h);
System.out.printf("%4d minutes\n", m);
System.out.printf("%4.2f seconds\n", s);
````
````plaintext
Alice finished the race in
   2 hours
  35 minutes
4.42 seconds
````

**Left align (flag `-`)**
````java
System.out.println("Alice finished the race in");
System.out.printf("%-4d hours\n", h);
System.out.printf("%-4d minutes\n", m);
System.out.printf("%-4.2f seconds\n", s);
````
````plaintext
Alice finished the race in
2    hours
35   minutes
4.42 seconds
````

**Zero padded width (flag `0`)**
````java
System.out.println("Alice finished the race in");
System.out.printf("%04d hours\n", h);
System.out.printf("%04d minutes\n", m);
System.out.printf("%04.2f seconds\n", s);
````
````plaintext
Alice finished the race in
0002 hours
0035 minutes
4.42 seconds
````

**Zero padded width (flag `0`)**
````java
System.out.printf("Alice' time was %02d:%02d:%02.0f\n", h, m, s);
````
````plaintext
Alice' time was 02:35:04
````

**Positive/Negative numbers**
````java
System.out.printf("%d EUR\n%d EUR\n", 123, -123);
````
````plaintext
123 EUR
-123 EUR
````

**Positive/Negative numbers (flag <code>&nbsp;</code>)**
````java
System.out.printf("% d EUR\n% d EUR\n", 123, -123);
````
````plaintext
 123 EUR
-123 EUR
````

**Positive/Negative numbers (flag `+`)**
````java
System.out.printf("%+d EUR\n%+d EUR\n", 123, -123);
````
````plaintext
+123 EUR
-123 EUR
````
