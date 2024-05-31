# Special Classes
{: .reading}

There are a number of special uses of classes that are important for us: **anonymous classes** and **inner classes**.

## Anonymous Classes
**Anonymous classes** are a way to extend the existing classes **on the fly**.\
For example, consider having a class ``Machine``:
````java
class Machine {

  public void start() {
    System.out.println("Starting...");
  }

}
````
When creating the ``Machine`` object, we can change the ``start`` method on the fly.
````java
public static void main(String[ ] args) {

  Machine m = new Machine() {

    public void start() {
      System.out.println("Wooooo");
    }

  };

  m.start();

}
````
````plaintext
Wooooo
````
After the constructor call, we have opened the curly braces and have overridden the ``start`` method's implementation on the fly.

>It is called anonymous, because the subclass that is defined on the fly has no name, only a type.

The modification is applicable only to the current object, and not the class itself. So if we create another object of that class, the start method's implementation will be the one defined in the class.

````java
class Machine {

  public void start() {
    System.out.println("Starting...");
  }
  
}
````
````java
public class Main {

public static void main(String[ ] args) {

  Machine m1 = new Machine() {

    @Override public void start() {
      System.out.println("Wooooo");
    }

  };

  Machine m2 = new Machine();
  m2.start();
  }
}
````
````plaintext
Starting...
````

## Inner Classes

Java supports **nesting** classes; a class can be a member of another class.\
Creating an inner class is quite simple. Just write a class within a class. Unlike a class, an inner class can be private. Once you declare an inner class private, it cannot be accessed from an object outside the class.

Example:
````java
class Robot {

  int id;

  Robot(int i) {
    id = i;
    Brain b = new Brain();
    b.think();
  }

  private class Brain {

    public void think() {
      System.out.println(id + " is thinking");
    }

  }

}
````

>The class ``Robot`` has an inner class ``Brain``. The inner class can access all of the member variables and methods of its outer class, but it cannot be accessed from any outside class.

