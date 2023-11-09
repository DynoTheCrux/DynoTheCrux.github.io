# Core Concepts of Object-Oriented Programming
{: .reading}

There are 4 core concepts in OOP:
- [Encapsulation](https://en.wikipedia.org/wiki/Encapsulation_(computer_programming) "the packing of data and methods into a single component"){:target="_blank"},
- [Inheritance](https://en.wikipedia.org/wiki/Inheritance_(object-oriented_programming) "a process by which one class inherits the members and methods of another class"){:target="_blank"},
- [Polymorphism](https://en.wikipedia.org/wiki/Polymorphism_(computer_science) "polymorphism means that a call to a member method will cause a different method to be executed depending on the type of object that invokes the method"){:target="_blank"},
- [Abstraction](https://en.wikipedia.org/wiki/Abstraction_(computer_science) "to define objects that represent abstract 'actors' that can perform work, report on and change their state, and 'communicate' with other objects in the system"){:target="_blank"}.

# Encapsulation
The idea behind **encapsulation** is to ensure that implementation details are not visible to users. The variables of one class will be hidden from the other classes, accessible only through the methods of the current class. This is called **data hiding**.
To achieve encapsulation in Java, declare the class' variables as ``private`` and provide **public** **setter** and **getter** methods to modify and view the variables' values.

For example:
````java
class BankAccount {
  private double balance=0;
  public void deposit(double x) {
    if(x > 0) { // Ensure positive amount
      balance += x;
    }
  }
}
````
This implementation hides the ``balance`` variable, enabling access to it only through the ``deposit`` method, which validates the amount to be deposited before modifying the variable.


> In brief, **encapsulation** provides the following benefits:
> - Control of the way data is accessed or modified
> - More flexible and easily changed code
> - Ability to change one part of the code without affecting other parts

# Inheritance
**Inheritance** is the process that enables one class to acquire the properties (methods and variables) of another. With inheritance, the information is placed in a more manageable, **hierarchical order**.

The class inheriting the properties of another is the **subclass** (also called derived class, or child class); the class whose properties are inherited is the **superclass** (base class, or parent class).

To inherit from a class, we use the ``extends`` keyword.

This example shows how to have the class ``Dog`` to inherit from the class ``Animal``:

````java
class Dog extends Animal {
 // some code
}
````
> Here, ``Dog`` is the **subclass**, and ``Animal`` is the **superclass**.

## Inherited variables and methods
When one class is inherited from another class, it inherits all of the superclass' **non-private** variables and methods.

Example:
````java
class Animal {
  protected int legs;
  public void eat() {
    System.out.println("Animal eats");
  }
}

class Dog extends Animal {
  Dog() {
    legs = 4;
  }
}
````

As you can see, the ``Dog`` class inherits the ``legs`` variable from the ``Animal`` class.
We can now declare a ``Dog`` object and call the ``eat`` method of its superclass:

````java
class Main {
  public static void main(String[ ] args) {
    Dog d = new Dog();
    d.eat();
  }
}
````
````plaintext
Animal eats
````

> Recall the protected access modifier, which makes the members visible only to the subclasses.


## Inheritance and Constructors

Constructors are **not** member methods, and so are **not** inherited by subclasses.
However, the constructor of the superclass is called when the subclass is instantiated.

````java
class A {
  public A() {
    System.out.println("New A");
  }
}
class B extends A {
  public B() {
    System.out.println("New B");
  }
}

class Program {
  public static void main(String[ ] args) {
      B obj = new B();
  }
}
````
````plaintext
New A
New B
````

> You can access the superclass from the subclass using the ``super`` keyword.
For example, ``super.var`` accesses the var member of the superclass.

# Polyphormism
**Polymorphism**, which refers to the idea of "having many forms", occurs when there is a hierarchy of classes related to each other through inheritance.
A call to a member method will cause a different implementation to be executed, depending on the type of the object invoking the method.

Here is an example: ``Dog`` and ``Cat`` are classes that inherit from the ``Animal`` class. Each class has its own implementation of the ``makeSound()`` method. 

````java
class Animal {
  public void makeSound() {
    System.out.println("Grr...");
  }
}
class Cat extends Animal {
  public void makeSound() {
    System.out.println("Meow");
  }
}
class Dog extends Animal {
  public void makeSound() {
    System.out.println("Woof");
  }
}
````

As all ``Cat`` and ``Dog`` objects are ``Animal`` objects, we can do the following in ``main``: 

````java
public static void main(String[ ] args) {
  Animal a = new Dog();
  Animal b = new Cat();

  a.makeSound();
  b.makeSound();
}
````
````plaintext
Woof
Meow
````


As the reference variable of type ``Animal`` ``a`` contains a ``Dog`` object, the ``makeSound()`` method of the ``Dog`` class will be called.
The same applies to the ``b`` variable.

> This demonstrates that you can use the ``Animal`` variable without actually knowing that it contains an object of the subclass.
> 
> This is very useful when you have multiple subclasses of the superclass.
