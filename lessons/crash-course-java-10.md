# Abstraction
{: .reading}

Remember when we talked about **Object-Oriented Programming** and its 4 core concepts **Encapsulation**, **Inheritance**, **Polymorphism**, **Abstraction**?
>If not, now is a good time to head over to that lecture and refresh your memory a bit.

Data **abstraction** provides the outside world with only essential information, in a process of representing essential features without including implementation details.\
A good real-world example is a **book**. When you hear the term book, you don't know the exact specifics, such as the page count, the color, or the size, but you understand the idea, or abstraction, of a book.\
The concept of **abstraction** is that we focus on essential qualities, rather than the specific characteristics of one particular example.

In Java, abstraction is achieved using **abstract classes** and **interfaces**.

## Abstract Classes
An abstract class is defined using the ``abstract`` keyword.
- If a class is declared abstract it cannot be instantiated (you cannot create objects of that type).
- To use an abstract class, you have to inherit it from another class.
- Any class that contains an abstract method must be defined as abstract.

> An abstract method is a method that is declared without an implementation (without braces, and followed by a semicolon):

````java
abstract void walk();
````

For example, we can define our ``Animal`` class as abstract:

````java
abstract class Animal {

  int legs = 0;

  abstract void makeSound();

}
````

The ``makeSound`` method is also abstract, as it has no implementation in the superclass.\
We can inherit from the ``Animal`` class and define ``makeSound()`` for the subclass:

````java
class Cat extends Animal {

  public void makeSound() {
    System.out.println("Meow");
  }

}
````

>Every Animal makes a sound, but each has a different way to do it. That's why we define an abstract class ``Animal``, and leave the implementation of **how** they make sounds to the subclasses.\
This is used when there is no meaningful definition for the method in the superclass.

## Interfaces

An **interface** is a completely abstract class that contains only abstract methods.\
Some specifications for interfaces:
- Defined using the ``interface`` keyword.
- May contain only static final variables.
- Cannot contain a constructor because interfaces cannot be instantiated.
- Interfaces can extend other interfaces.
- A class can implement any number of interfaces.

An example of a simple interface:
````java
interface Animal {

  public void eat();
  public void makeSound();

}
````
Interfaces have the following properties:
- An interface is implicitly abstract. You do not need to use the abstract keyword while declaring an interface.
- Each method in an interface is also implicitly abstract, so the abstract keyword is not needed.
- Methods in an interface are implicitly public.

>A class can inherit from just **one** superclass, but can implement **multiple** interfaces!

## Implementing an Interface

Use the ``implements`` keyword to use an interface with your class.
````java
interface Animal {

  public void eat();
  public void makeSound();

}
````
````java
class Cat implements Animal {

  public void makeSound() {
    System.out.println("Meow");
  }

  public void eat() {
    System.out.println("omnomnom");
  }

}
````
> When you implement an interface, you need to override all of its methods or declare the implementing class as abstract.

