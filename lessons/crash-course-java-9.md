# Memory Modifiers
{: .reading}
## Static

### Variables
When you declare a variable or a method as ``static``, it belongs to the class, rather than to a specific instance. This means that only one instance of a ``static`` member exists, even if you create multiple objects of the class, or if you don't create any. It will be shared by all objects.

Example:
````java
public class Counter {

  public static int COUNT=0;

  Counter() {
    COUNT++;
  }
}
````
The ``COUNT`` variable will be shared by all objects of that class.
Now, we can create objects of our Counter class in ``main``, and access the static variable.
````java
public class Main {

  public static void main(String[ ] args) {

    Counter c1 = new Counter();

    Counter c2 = new Counter();

    System.out.println(Counter.COUNT); // access via class
  }
}
````
````plaintext
2
````
The output is 2, because the ``COUNT`` variable is static and gets incremented by one each time a new object of the ``Counter`` class is created. In the code above, we created 2 objects.\
You can also access the static variable using any object of that class, such as ``c1.COUNT`` *(However, this is not recommended)*.


### Methods
The same concept applies to ``static`` methods.

````java
public class Vehicle {

  public static void horn() {
    System.out.println("Beep");
  }
}
````

Now, the ``horn`` method can be called without creating an object:
````java
public class Main {

  public static void main(String[ ] args) {
    Vehicle.horn(); // no object needed
  }
}
````
````plaintext
Beep
````
Another example of static methods are those of the ``Math`` class, which is why you can call them without creating a ``Math`` object.\
Also, the ``main`` method must always be ``static``.

## Final

Use the ``final`` keyword to mark a variable constant, so that it can be **assigned only once**.

Example:
````java
class Main {

  public static final double PI = 3.14; 

  public static void main(String[ ] args) {
    System.out.println(PI);
  }

}
````

``PI`` is now a constant. Any attempt to assign it a value after the initialization will cause an error.

>Methods and classes can also be marked ``final``. This serves to restrict methods so that they can't be overridden and classes so that they can't be subclassed.
