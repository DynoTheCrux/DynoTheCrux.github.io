# Value and Reference Types
{: .reading}
When variables are passed as parameters to a function, there is a very important difference depending on the variable type.

## Value Types
**Value types** are the basic types, and include ``int``, ``long``, ``float``, ``double``, ``boolean``, and ``char``.
These data types store the values assigned to them in the corresponding memory locations.\
So, when you pass them to a method, you basically operate on the variable's **value**, rather than on the variable itself. 

Example:
````java
public class Main {

  public static void main(String[ ] args) {
    int x = 5;

    addOneTo(x); // x's VALUE is given as parameter

    System.out.println(String.format("In main, x=%d", x));
  }

  static void addOneTo(int num) {
    num = num + 1;
    System.out.println(String.format("Inside function, num=%d", num));
  }
}
````
````plaintext
Inside function, num=6
In main, x=5
````
>The method from the example above takes the **value** of its parameter, which is why the original variable is not affected and 5 remains as its value.


## Reference Types

A **reference type** stores a reference (or address) to the memory location where the corresponding data is stored.
>When you create an object using the constructor, you create a reference variable.

For example, consider having a ``Person`` class defined:

````java
public class Main {

  public static void main(String[ ] args) {

    Person j;
    j = new Person("John");
    j.setAge(20);

    celebrateBirthday(j); // call by reference
    System.out.println(j.getAge());
  }

  static void celebrateBirthday(Person p) {
    p.setAge(p.getAge() + 1);
  }
}
````
````plaintext
21
````

The method ``celebrateBirthday`` takes a ``Person`` object as its parameter, and increments its attribute.\
Because ``j`` is a reference type, the method affects the object itself, and is able to change the actual value of its attribute.

> ``String`` and ``List`` are also reference data types.


## The null Value

Reference types have a special reference called ``null`` for when there is **no reference**. Consider following code:

````java
public class Main {

  public static void main(String[ ] args) {

    Person j; // declare variable j
    
    // j.setAge(26); // Not possible: j has no reference so far

    // create a new object and store the reference in j
    j = new Person("John");
    j.setAge(26);
    ...
  }
}
````

Until an object is created and assigned to a variable, that variable stores no reference. The special reference that means "no reference" is `null`. The above example is equivalent to:

````java
public class Main {

  public static void main(String[ ] args) {

    Person j = null; // declare variable j and EXPLICITLY assign null
    
    // j.setAge(26); // Not possible: j has no reference so far (null)

    // create a new object and store the reference in j
    j = new Person("John");
    j.setAge(26);
    ...
  }
}
````

Assigning `null` to a variable can be used to **dereference** a variable (delete the reference). `null` can also be used in with the `==` operator to check if a variable has a reference or not, i.e. if an object exists or not.

Example:
````java
Person j = new Person("John");
j.setAge(26);

// dereference j
j = null;

// check if j holds a reference
if(j == null) {
  System.out.println("There is no object j");
} else {
  System.out.println("j exists");
}
````

> `null` is only assignable to reference types, e.g `int i = null` is not valid!
