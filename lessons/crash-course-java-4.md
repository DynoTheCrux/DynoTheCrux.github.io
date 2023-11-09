# Using Classes
{: .reading}

In order to create your own custom objects, you must first create the corresponding classes. A minimal (empty) definition of a class is

````java
class ClassName {

} 
````

To define class **behavior**, we implement method. Here, we declare a ``bark()`` method in a ``Animal`` class.

````java
public class Animal {
  void bark() {
    System.out.println("Woof-Woof");
  }
}
````

> Now, in order to use the class and it's methods, we need to declare an **object** of that class.

# Creating Objects

Let's head over to our ``main`` and create a new object of our class ``Animal``.

````java
class MyClass {
  public static void main(String[ ] args) {
    Animal dog = new Animal();
    dog.bark();
  }
}
````

````plaintext
Woof-Woof
````

Now, ``dog`` is an **object** of type ``Animal``. We created it using the ``new`` keyword. Thus we can call its ``bark()`` method, using the name of the object and a dot '``.``' .
The **dot notation** is used to access the object's **attributes** and **methods**.

# Defining Attributes

A class has **attributes** and **methods**. The attributes are basically variables within a class.
Let's create a class called ``Vehicle``, with its corresponding attributes and methods.

````java
public class Vehicle {
  int maxSpeed;
  int wheels;
  String color;
  double fuelCapacity;  

  void horn() {
    System.out.println("Beep!");
  }  
}
````

``maxSpeed``, ``wheels``, ``color``, and ``fuelCapacity`` are the attributes of our ``Vehicle`` class, and ``horn()`` is the only method.

> We can define as many attributes and methods as necessary.

Next, we can create multiple objects of our ``Vehicle`` class, and use the dot syntax to access their attributes and methods. 

````java
class Main {
  public static void main(String[] args) {
    Vehicle v1 = new Vehicle();
    Vehicle v2 = new Vehicle();
    v1.color = "red";
    v2.horn();
  }
}
````

# Access Modifiers

Now let's discuss the ``public`` keyword in front of the ``main`` method.

> **public** ``static void main(String[] args)``

``public`` is an **access modifier**, meaning that it is used to set the level of access. You can use access modifiers for classes, attributes, and methods.

For *classes*, the available modifiers are *public* or *default* (left blank):
- **public**: The class is accessible by any other class.
- **default**: The class is accessible only by classes in the same package.

The following choices are available for *attributes* and *methods*:
- **default**: A variable or method declared with no access control modifier is available to any other class in the same package.
- **public**: Accessible from any other class.
- **protected**: Provides the same access as the default access modifier, with the addition that subclasses can access protected methods and variables of the superclass (Subclasses and superclasses are covered later on).
- **private**: Accessible only within the declared class itself.

Example:
````java
public class Vehicle {
  private int maxSpeed;
  private int wheels;
  private String color;
  private double fuelCapacity;

  public void horn() {
    System.out.println("Beep!");
  }
}
````

> It's a best practice to keep the variables within a class private. The variables are accessible and modified using **Getters** and **Setters**.

## Getters & Setters

**Getters** and **Setters** are used to effectively protect your data, particularly when creating classes. For each variable, the ``get`` method returns its value, while the ``set`` method sets the value.

**Getters** start with ``get``, followed by the variable name, with the first letter of the variable name capitalized.
**Setters** start with ``set``, followed by the variable name, with the first letter of the variable name capitalized.

Example:
````java
public class Vehicle {
  private String color;

  // Getter
  public String getColor() {
    return color;
  }

 // Setter
  public void setColor(String c) {
    this.color = c;
  }
}
````

The **getter method** returns the value of the attribute.
The **setter method** takes a parameter and assigns it to the attribute.

> The keyword ``this`` is used to refer to the current object. Basically, ``this.color`` is the color attribute of the current object.

Once our getter and setter have been defined, we can use it in our ``main``:

````java
public static void main(String[ ] args) {
  Vehicle v1 = new Vehicle();
  v1.setColor("Red");
  System.out.println(v1.getColor());
}
````
````plaintext
Red
````

Getters and setters allow us to have control over the values. You may, for example, validate the given value in the setter before actually setting the value.

> Getters and setters are fundamental building blocks for the concept of **encapsulation**, which will be covered later.

# Constructors
**Constructors** are special methods invoked when an object is created and are used to initialize them.
A constructor can be used to provide initial values for object attributes.

- A constructor name must be same as its class name.
- A constructor must have no explicit return type.

Example of a constructor:
````java
public class Vehicle {
  private String color;
  Vehicle() {
     color = "Red";
  }
}
````

The ``Vehicle()`` method is the constructor of our class, so whenever an object of that class is created, the color attribute will be set to ``"Red"``.

A constructor can also take parameters to initialize attributes. 

````java
public class Vehicle {
  private String color;
  Vehicle(String c) {
    color = c;
  }
}
````
> You can think of constructors as methods that will set up your class by default, so you don’t need to repeat the same code every time.

## Using Contructors
The constructor is called when you create an object using the new keyword.

Example:
````java
public class MyClass {
  public static void main(String[ ] args) {
    Vehicle v = new Vehicle("Blue");
  }
}
````

> This will call the constructor, which will set the color attribute to "Blue".

## Multiple Constructors

A single class can have multiple constructors with different numbers of parameters.
The setter methods inside the constructors can be used to set the attribute values.

Example:
````java
public class Vehicle {
  private String color;

  Vehicle() {
    this.setColor("Red");
  }
  Vehicle(String c) {
    this.setColor(c);
  }

  // Setter
  public void setColor(String c) {
    this.color = c;
  }
}
````

The class above has two **constructors**, one without any parameters setting the ``color`` attribute to a default value of ``"Red"``, and another constructor that accepts a parameter and assigns it to the attribute.

Now, we can use the constructors to create objects of our class:
````java
//color will be "Red"
Vehicle v1 = new Vehicle();

//color will be "Green"
Vehicle v2 = new Vehicle("Green"); 
````
> Java automatically provides a *default constructor* without parameters, so all classes have a constructor, whether one is specifically defined or not.
