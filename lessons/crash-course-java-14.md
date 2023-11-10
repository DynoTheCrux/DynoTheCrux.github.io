# Java Collections
{: .reading}

## ArrayList

**Java** provides special classes to store and manipulate groups of objects.

One such class is the ``ArrayList``. Standard Java arrays are of a fixed length, which means that after they are created, they cannot expand or shrink.
On the other hand, ``ArrayList``s are created with an initial size, but when this size is exceeded, the collection is automatically enlarged.

When objects are removed, the ArrayList may shrink in size.

>Note: The ArrayList class is in the ``java.util`` package, so it's necessary to **import** it before using it.

Create an ArrayList as you would any object.

````java
import java.util.ArrayList;
//...

ArrayList colors = new ArrayList();
````

You can optionally specify a capacity and type of objects the ArrayList will hold:

````java
ArrayList<String> colors = new ArrayList<String>(10);
````

The code above defines an ArrayList of Strings with 10 as its initial size.

>ArrayLists store objects. Thus, the type specified must be a class type. You cannot pass, for example, ``int`` as the objects' type. Instead, use the special **class types** that correspond to the desired value type, such as ``Integer`` for int, ``Double`` for double, and so on.

### Using the ArrayList

The ``ArrayList`` class provides a number of useful methods for manipulating its objects.

The ``add()`` method adds new objects to the ``ArrayList``. Conversely, the ``remove()`` method removes objects from the ``ArrayList``.

````java
import java.util.ArrayList;

public class Main {
  public static void main(String[ ] args) {
    // Create new ArrayList to hold String objects
    ArrayList<String> colors = new ArrayList<String>();

    // Add elements
    colors.add("Red");
    colors.add("Blue");
    colors.add("Green");
    colors.add("Orange");

    // remove element
    colors.remove("Green");
    
    // Print first element
    System.out.println(colors.get(0));

    // Print content of whole list
    System.out.println(colors);
  }
}
````
````plaintext
Red
[Red, Blue, Orange]
````


>Other useful methods include the following:
>- ``contains()``: Returns true if the list contains the specified element
>- ``get(int index)``: Returns the element at the specified position in the list
>- ``size()``: Returns the number of elements in the list
>- ``clear()``: Removes all of the elements from the list

>Note: **The indexing starts with 0**!

## LinkedList

The ``LinkedList`` is very similar in syntax to the ``ArrayList``.

You can easily change an ``ArrayList`` to a ``LinkedList`` by changing the object type.

````java
import java.util.LinkedList;

public class Main {
  public static void main(String[ ] args) {
    // Create new LinkedList to hold String objects
    LinkedList<String> colors = new LinkedList<String>();

    // Add elements
    colors.add("Red");
    colors.add("Blue");
    colors.add("Green");
    colors.add("Orange");

    // remove element
    colors.remove("Green");
    
    // Print first element
    System.out.println(colors.getFirst());

    // Print content of whole list
    System.out.println(colors);
  }
}
````
````plaintext
Red
[Red, Blue, Orange]
````

>You cannot specify an initial capacity for the ``LinkedList``.

## LinkedList vs. ArrayList

The most notable difference between the ``LinkedList`` and the ``ArrayList`` is in the way they store objects.\
The ``ArrayList`` is better for storing and accessing data, as it is very similar to a normal array.\
The ``LinkedList`` is better for manipulating data, such as making numerous inserts and deletes.

In addition to storing the object, the ``LinkedList`` stores the memory address (or link) of the element that follows it. It's called a ``LinkedList`` because each element contains a link to the neighboring element.

![LinkedList](../assets/img/cc14_linkedlist.png)

>**Summary**:
>- Use an **``ArrayList``** when you need **rapid access** to your data.
>- Use a **``LinkedList``** when you need to make a **large number of inserts and/or deletes**.

## HashMap

Arrays and Lists store elements as ordered collections, with each element given an integer index.

In contrast, ``HashMap`` is used for storing data collections as **key and value pairs**. One object is used as a key (index) to another object (the value).\
The ``put()``, ``remove()``, and ``get()`` methods are used to add, delete, and access values in the ``HashMap``.

Example:

````java
import java.util.HashMap;

public class Main {
  public static void main(String[ ] args) {
    // create new HashMap with String as keys and Integer as values
    HashMap<String, Integer> points = new HashMap<String, Integer>();

    // add key-value pairs
    points.put("Alice", 154);
    points.put("Bob", 42);
    points.put("Charly", 733);

    // use the key to retrieve the value
    System.out.println(points.get("Bob")); 
  }
}
````
````plaintext
42
````

### Duplicates

A ``HashMap`` **cannot contain duplicate keys** (duplicate values are fine though). Adding a new item with a key that already exists overwrites the old element.\
The ``HashMap`` class provides ``containsKey()`` and ``containsValue()`` methods that determine the presence of a specified key or value.\
If you try to get a value that is not present in your map, it returns the value of ``null``. 

## Sets

A ``Set`` is a collection that **cannot contain duplicate elements**. It models the mathematical set abstraction.

### HashSet

There are many implementations of a ``Set`` each with different use cases. A common implementation of a ``Set`` is the **``HashSet``** class.

````java
import java.util.HashSet;

class Main {
  public static void main(String[] args) {
    // create new HashSet of type String
    HashSet<String> set = new HashSet<String>();

    // add elements to the set
    set.add("Alpha");
    set.add("Bravo");
    set.add("Charlie");

    // try to add a duplicate value
    set.add("Bravo"); // does not change the set

    // Verify the set content
    System.out.println(set);
  }
}
````
````plaintext
[Charlie, Alpha, Bravo]
````

>Note that the order of the elements is **not retained** in a ``HashSet``.

### LinkedHashSet

The ``HashSet`` class does not automatically retain the order of the elements as they're added. To order the elements, use a ``LinkedHashSet``, which maintains a linked list of the set's elements in the order in which they were inserted.

````java
import java.util.LinkedHashSet;

class Main {
  public static void main(String[] args) {
    // create new LinkedHashSet of type String
    LinkedHashSet<String> set = new LinkedHashSet<String>();

    // add elements to the set
    set.add("Alpha");
    set.add("Bravo");
    set.add("Charlie");

    // try to add a duplicate value
    set.add("Bravo"); // does not change the set

    // Verify the set content
    System.out.println(set);
  }
}
````
````plaintext
[Alpha, Bravo, Charlie]
````

**What is hashing?**
A *hash table* stores information through a mechanism called *hashing*, in which a key's informational content is used to determine a unique value called a *hash code*.\
So, basically, each element in the ``HashSet`` is associated with its unique *hash code*.

> You've learned about the various **collection types** that are available in **Java**, including **Lists**, **Maps**, and **Sets**. The choice of which one to use is specific to the data you need to store and manipulate.
