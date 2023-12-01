# Overview
{: .reading}

* This will become a table of contents (this text will be scrapped).
{:toc}

# Activities and their life cycle
{: .reading}

In the Android ecosystem, each screen you see is an `Activity`. An `Activity` can belong to your app (e.g. a **highscore screen** or a **graph display**)  or it can be a system screen (e.g. **phone dialer**, **system settings**).

![Examples of Activities](../../assets/img/004_lifecycle/example_activities.png)

As a user navigates through, out of, and back to your app, the ``Activity`` instances in your app **transition through different states** in their lifecycle. The ``Activity`` class provides a number of **lifecycle methods** that allow the activity to know that a state has changed: that the system is creating, stopping, or resuming an activity, or destroying the process in which the activity resides. 

## Life Cycle Methods

Within the lifecycle methods, you can declare how your activity behaves when the user leaves and re-enters the activity. For example, if you're building a streaming video player, you might pause the video and terminate the network connection when the user switches to another app. When the user returns, you can reconnect to the network and allow the user to resume the video from the same spot. In other words, each lifecycle method allows you to perform specific work that's appropriate to a given change of state.

![https://developer.android.com/guide/components/activities/activity-lifecycle](img/../../../assets/img/004_lifecycle/activity_lifecycle.png)

There exist 6 lifecycle methods which represent pairs of start and end of particular events: create/destroy, start/stop, pause/resume.
  - ``onCreate``
  - ``onStart``
  - ``onResume``
  - ``onPause``
  - ``onStop``
  - ``onDestroy``


# Workshop: LifeCycler
{: .reading}

This sessions app will demonstrate the basic use of the lifecycle events of an activity. We will create a list where all the occurring lifecycle events will be displayed with a timestamp.

![LifeCycler App](../../assets/img/004_lifecycle/app_preview.png)

After this session, you will
- know when lifecycle events occur,
- know how to use lifecycle methods,
- know 2 new widgets: ``ListView`` and ``FloatingActionButton``,
- be able to create simple custom UIs for lists
- use logging and logging levels

## New Project

Open Android Studio and create a new project using the "Empty Views Activity" Template.

Choose an appropriate name and save location, e.g. "LifeCycler" and "C:\Android\Projects\LifeCycler"

## Layout

We will start by designing the layout of the app. Switch over to the layout editor by opening "activity_main.xml" under "res/layout"

![activity_main.xml](../../assets/img/004_lifecycle/activity_main_xml.png)

> Start by deleting the predefined "Hello World" ``TextView`` from the component tree, but **leave the ``ConstraintLayout``**.

### ListView

The central widget of this demo app will be a `ListView`, which displays a scrollable list of widgets. In the most basic case, which we will use here, the list consists of `TextView`s which contain text. However, it is also possible to have a list of buttons or even a whole composition of widgets (think: list of facebook/reddit posts with text, images, like buttons, etc).

> Drag and drop a ``ListView`` (Palette: Legacy) into your component tree as child of the root layout `ConstraintLayout`.
>
> Set an appropriate `id` (e.g. `listViewEvents`) and **add constraints** so that the ``ListView`` spans the whole screen. *Hint: Check previous lectures if you forgot how this is done.*

You will still see a blank, white screen. In order to add a few test entries to the list, we have to set the `entries` attribute.

>Click on the narrow button on the right of the attribute value to choose entries from a predefined set. Choose "imProtocols" (it does not really matter which).

Your screen should now look like this:

![Layout ListView with dummy entries](../../assets/img/004_lifecycle/layout_list1.png)

> Test the app by running it on the emulator to see that it works.

[>Layout Code for this step<](../../assets/source/004_ui/activity_main_1.xml){:target="_blank"}

## Populating the ListView in Code

Static text entries in a `ListView` are seldom enough. Typically, we want to display items dynamically when they become available during the execution of the app, at **run time**. So we need a way to add/remove items from the list from our application code. To achieve this, we have to first *link* the `ListView` in the layout with a variable of type `ListView` in our code.

> Open "MainActivity.java"

Here we have ou first encounter with a **lifecycle method**, ``onCreate``. This method is called by the system **when the activity is created**, long before the layout is shown to the user. This is also the spot where the layout is created by the `setContentView(layout)` method. The `onCreate` lifecycle method is therefore the perfect place to populate our ``ListView``.

> Inside the `onCreate` method, create a new local variable `listViewEvents` of type `ListView`. The *linking* of this variable to the widget in the layout is done with the method `findByViewId(id)`. We make use of the `R` object which provides access to resources such as **layouts** and widget **ids**. *Hint: If you use the auto-complete feature when using previously unused types, the necessary import-statement will be added automatically.*

````java
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set activity content to layout defined in 'activity_main.xml'
        setContentView(R.layout.activity_main);
        
        // Create local variable and link ListView widget
        ListView listViewEvents = findViewById(R.id.listViewEvents);
    }
````

As explained above, the `ListView` is not simply a list of texts, but actually a list of widgets. So we need to tell the `ListView` which widgets to use as list items and what their content should be. For this purpose, so called `Adapter` objects are required. Such an adapter object translates (adapts) between the widget (what the user interacts with) and the underlying data (what the program interacts with). Here, we will use an `ArrayAdapter` to create a simple list of text items.

### A Simple Layout for List Items
Before we can continue with defining our `Adapter`, we need to create the layout for our list items. We want a simple layout containing just a `TextView`.

> Select the folder "res/layout", right-click and choose "New > Layout Resource File"

![New > Layout Resource File](../../assets/img/004_lifecycle/project_view_create_layout.png)

> Set the name of the new resource file to "simple_list_item" and change the root element to `TextView`.

![simple_list_item](../../assets/img/004_lifecycle/view_new_simple_list_item.png)

> Set the ``id`` attribute to `lblListItem` and add some text so that you can see the styling. Set the attributes `paddingHorizontal` to `20dp` and ``paddingVertical`` to ``15dp``. Increase the ``textSize`` to ``18sp``. Feel free to adjust the styling further.

[>Layout Code for this step<](../../assets/source/004_lifecycle/simple_list_item.xml){:target="_blank"}


### Creating the Adapter
> Create the **properties** `listViewAdapter` of type `ArrayAdapter<String>` as well as `listData` of type `ArrayList<String>`.

````java
public class MainActivity extends AppCompatActivity {

    // Properties
    private ArrayAdapter<String> listAdapterEvents;
    private ArrayList<String> listData;
````

> In ``onCreate``, create a java `ArrayList<String>` instance as the underlying data storage for the `String`s that we want to display in the `ListView`.
> 
> Create a new ``ArrayAdapter`` using our list item layout ``R.layout.simple_list_item`` and the data list.

````java
        // Create list data storage (String)
        listData = new ArrayList<>();
        // Create ArrayAdapter to displays a list of texts (String)
        listAdapterEvents = new ArrayAdapter<>(this, R.layout.simple_list_item, listData);
````

Next, we tell the `ListView` to use our adapter

````java
        // Set adapter to be used by list view
        listViewEvents.setAdapter(listAdapterEvents);
````

At this point, we can start adding `String`s to our data list so that they are displayed as items in the `ListView`. A simple `for` loop to add a bunch of `String`s will do for now. For reasons of performance, the actual list widget is not updated everytime we change the underlying data. So we have to tell the system, once we are finished manipulating the data list, so that the graphics can then be updated in one go. The method `notifyDataSetChanged()` of the ``ArrayAdapter`` is called for this. 

````java
        // Fill data list with dummy events
        for(int i = 0; i < 10; i++) {
            listData.add(String.format("Event #%d", i));
        }

        // notify the adapter that the underlying data has changed
        listAdapterEvents.notifyDataSetChanged();
````

> Run the app to verify that everything is working so far.

![App with generated events](../../assets/img/004_lifecycle/app_dummy_data.png)

[>Program Code for this step<](../../assets/source/004_lifecycle/MainActivity_1.java){:target="_blank"}

## Lifecycle Methods

The underlying purpose of our app is to show **how lifecycle methods work**. The next step is therefore to add those methods to our activity. All lifecycle methods are defined in the class `Activity` which is a **super class** of our `MainActivity`. As we want to add our own code to these methods, we **override** them in our class.

> Add following methods to your `MainActivity` class

````java
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
````

The `@Override` is just a marker (annotation) which tells us that this method is intended to be overridden. It could be omitted without any ill effects. However, as a developer you are able to see **immediately** which methods are overridden from super classes and which are defined in your won class. This is very valuable information to have at a glance, so we like to keep those `@Override` annotations around.

One more thing to note is the call to ``super`` in all of those methods. It is important to keep this in all lifecycle methods. If we were to omit the `super` call, then super classes could not react to lifecycle events any longer, because according to the laws of java inheritance, only our lifecycle method gets called by the system. Therefore, it is imperative to include the call to the appropriate `super` method so that the code in the super classes also gets executed. Weird stuff can happen otherwise.

### Adding list items on lifecycle events

Let's go ahead and add a new item to our list whenever one of the lifecycle events occurs. First, we create a new method to prepend the current timestamp to a text in a `String` parameter and append the result to the list.

````java
    private void addListItem(String name) {
        // create formatted timestamp HH:mm:ss.SSS of current time
        String timeNow = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        // combine timestamp and parameter
        listData.add(String.format("[%s] %s", timeNow, name));

        // notify the adapter that the underlying data has changed
        listAdapterEvents.notifyDataSetChanged();
    }
````

Once we created `addListItem(String)`, we can add it to each of the lifecycle methods. Additionally, we add an entry to our Log.

````java
    @Override
    protected void onStart() {
        super.onStart();

        // Log event and add to list
        Log.d("OnLifeCycle", "onStart()");
        addListItem("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Log event and add to list
        Log.d("OnLifeCycle", "onResume()");
        addListItem("onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Log event and add to list
        Log.d("OnLifeCycle", "onPause()");
        addListItem("onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Log event and add to list
        Log.d("OnLifeCycle", "onStop()");
        addListItem("onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Log event and add to list
        Log.d("OnLifeCycle", "onDestroy()");
        addListItem("onDestroy()");
    }
````

Also, don't forget to also add it to ``onCreate``.
````java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        [...]

        // Log event and add to list
        Log.d("OnLifeCycle", "onCreate()");
        addListItem("onCreate()");
    }
````

> Run the program and notice the lifecycle events happening when you put the app in the background repeatedly.

![App showing lifecycle events](../../assets/img/004_lifecycle/app_lifecycle.png)

Notice the events happening always in a specific order. Also notice the ``onPause`` and ``onStop`` events happening right after one another. However, when putting the app in the background, we see only `onPause` on the screenshot. Even though those two events are happening back to back, `onPause` is the last event happening while the UI is still responsive while `onStop` happens when the UI is no longer updated, hence we don't see the event on the screenshot, even if we see it in the log file.

![Log showing lifecycle events](../../assets/img/004_lifecycle/app_lifecycle_log.png)

[>Program Code for this step<](../../assets/source/004_lifecycle/MainActivity_2.java){:target="_blank"}

## FloatingActionButton

As a last step, we will add a ``FloatingActionButton`` to finally *destroy* the app. As the name suggests, this widget is a simple button that has a drop shadow underneath so that it looks like floating above the rest of the screen. We will use it to send the `onDestroy` event to the app.

> Go to the layout editor by opening the layout file "activity_main.xml".
> 
> Add a ``FloatingActionButton`` as the last child of the ``ConstraintLayout`` (*be careful not to drop the ``FloatingActionButton`` as child of the ``ListView``*). If asked, choose any image you like.
> Set an appropriate `id` (e.g. `fabDestroy`).
>
> Set the constraints of the ``FloatingActionButton`` be be in the bottom right by dragging it's handles.

![Constraints for FloatingActionButton](../../assets/img/004_lifecycle/layout_dnd_fab.gif)

> Increase the offset of the constraints so that the button does not hug the sides.

![Constraints for FloatingActionButton](../../assets/img/004_lifecycle/layout_dnd_fab.png)

[>Layout Code for this step<](../../assets/source/004_ui/activity_main_2.xml){:target="_blank"}

## Click and Destroy

Go back to our code in "MainActivity.java". We need to link the `FloatingActionButton` in the layout to our code and install a click listener. A click listener allows us to implement actions when the user clicks on the button. Add following lines to `onCreate`:

````java
        // Create local variable and link FAB widget
        FloatingActionButton fabDestroy = findViewById(R.id.fabDestroy);
        // Install click listener
        fabDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("OnLifeCycle", "Finish it!!!!!1");
                MainActivity.this.finish(); // exits the activity
            }
        });
````

> Run the program again. When you click the button, you will find onDestroy() in the log.

![Final app](../../assets/img/004_lifecycle/app_fin.png)

![Final app log](../../assets/img/004_lifecycle/app_fin_log.png)


[>Program Code for the final step<](../../assets/source/004_lifecycle/MainActivity_3.java){:target="_blank"}
