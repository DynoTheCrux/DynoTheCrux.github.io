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
- know 2 new widgets: ``RecyclerView`` and ``FloatingActionButton``,
- be able to create simple custom UIs for lists
- use logging and logging levels

## New Project

Open Android Studio and create a new project using the "Empty Views Activity" Template.

Choose an appropriate name and save location, e.g. "LifeCycler" and "C:\Android\Projects\LifeCycler"

## Layout

We will start by designing the layout of the app. Switch over to the layout editor by opening "activity_main.xml" under "res/layout"

![activity_main.xml](../../assets/img/004_lifecycle/activity_main_xml.png)

> Start by deleting the predefined "Hello World" ``TextView`` from the component tree, but **leave the ``ConstraintLayout``**.

### RecyclerView

The central widget of this demo app will be a `RecyclerView`, which displays a scrollable list of widgets. In the most basic case, which we will use here, the list consists of `TextView`s which contain text. However, it is also possible to have a list of buttons or even a whole composition of widgets (think: list of facebook/reddit posts with text, images, like buttons, etc).

> Drag and drop a ``RecyclerView`` (Palette: Containers) into your component tree as child of the root layout `ConstraintLayout`.
>
> Set an appropriate `id` (e.g. `recyclerViewEvents`) and **add constraints** so that the ``RecyclerView`` spans the whole screen. *Hint: Check previous lectures if you forgot how this is done.*

You will see a list with sample entries.

[>Layout Code for this step<](../../assets/source/004_ui/activity_main_1.xml){:target="_blank"}

## Populating the RecyclerView in Code

Static text entries in a `RecyclerView` are seldom enough. Typically, we want to display items dynamically when they become available during the execution of the app, at **run time**. So we need a way to add/remove items from the list from our application code. To achieve this, we have to first *link* the `RecyclerView` in the layout with a variable of type `RecyclerView` in our code.

> Open "MainActivity.java"

Here we have you first encounter with a **lifecycle method**, ``onCreate``. This method is called by the system **when the activity is created**, long before the layout is shown to the user. This is also the spot where the layout is created by the `setContentView(layout)` method. The `onCreate` lifecycle method is therefore the perfect place to populate our ``RecyclerView``.

Inside the `onCreate` method, create a new local variable `recyclerView` of type `RecyclerView`. The *linking* of this variable to the widget in the layout is done with the method `findByViewId(id)`. We make use of the `R` object which provides access to resources such as **layouts** and widget **ids**. *Hint: If you use the auto-complete feature when using previously unused types, the necessary import-statement will be added automatically.*


````java

RecyclerView recyclerView;

protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set activity content to layout defined in 'activity_main.xml'
        setContentView(R.layout.activity_main);
        
        // Create local variable and link ListView widget
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewEvents);
    }
````

As explained above, the `RecyclerView` is not simply a list of texts, but actually a list of widgets. So we need to tell the `RecyclerView` which widgets to use as list items and what their content should be. For this purpose, so called `Adapter` objects are required. Such an adapter object translates (adapts) between the widget (what the user interacts with) and the underlying data (what the program interacts with). Here, we will use an `RecyclerViewAdapter` to create a simple list of text items.

### A Simple Layout for List Items
Before we can continue with defining our `Adapter`, we need to create the layout for our list items. We want a simple layout containing just `TextView`s.

> Select the folder "res/layout", right-click and choose "New > Layout Resource File"

![New > Layout Resource File](../../assets/img/004_lifecycle/project_view_create_layout.png)

> Set the name of the new resource file to "message". Populate the layout file with two `TextView`s next to each other. Add some margins/padding to make sure they do not stick to each other. The `ConstraintLayout` should wrap the content to make sure it is only as big as the elements. Set meaningful IDs to the elements.

[>Layout Code for this step<](../../assets/source/004_lifecycle/simple_list_item.xml){:target="_blank"}


### Creating the Adapter

Before we create our `Adapter`, we create a so called `ViewHolder`. It is a class that contains the view elements of our message item layout file, keeping the information at one place, information flow unilateral and easily extendable. It extends the superclass from the `RecyclerView` and the constructor we connect our view IDs. Therefore create a new file containing the java class.

````java

public class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView txtMessage;
    TextView txtTime;
    View view;


    MessageViewHolder(View itemView)
    {
        super(itemView);
        txtMessage = itemView.findViewById(R.id.txt_Message);
        txtTime = itemView.findViewById(R.id.txt_Time);
        view = itemView;

    }
}
````
The data itself will be of object `MessageData`, a class that we create ourselfes. It makes sure that we can extend and change the data that will be shon in our recycler view flexibly.

````java
public class MessageData {

    String message;
    String time;

    MessageData(String message, String time)
    {
        this.message = message;
        this.time = time;
    }
}
````


Next we can create the `Adapter` class. Create a java file with the class name again. We extend the superclass `RecyclerView.Adapter<MessageViewHolder>` and override some of its methods. In the constructor we make sure that the correct `context` can be set and the right data is connected to the view items. The overriden methods `onCreateViewHolder` and `onBindViewHolder` are called when the view is created or a new item is bound to the view. Therefore we inflate the message layout and set the contents in those methods. The method `getItemCount` is abstract in the superclass and also has to be overridden and returns the size of our list.

````java

public class RecyclerViewAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    List<MessageData> list;
    Context context;

    RecyclerViewAdapter(List<MessageData> list, Context context)
    {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View messageView = inflater.inflate(R.layout.message, parent, false);

        MessageViewHolder viewHolder= new MessageViewHolder(messageView);
        return viewHolder;
    }

    @Override
    public void
    onBindViewHolder(final MessageViewHolder viewHolder,
                     final int position)
    {
        viewHolder.txtMessage.setText(list.get(position).message);
        viewHolder.txtTime.setText(list.get(position).time);

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
````

## Preparing the MainActivity
To work with the `RecyclerView` in our `MainActivity` we create a `RecyclerView` variable, `RecyclerViewAdapter` variable variable along with a `List` containing our `MessageData`. 

````java
  // inside the MainActivity class
  RecyclerViewAdapter adapter;
  RecyclerView recyclerView;
  
  List<MessageData> list = new ArrayList<>();

````

Create an instance of the adapter by using the `new` keyword and the constructor. In the constructor we set the _Application Context_ because we want to work with the adapter over the whole set of lifecycles of the activity. 

````java
  // in onCreate, below connecting the recycler view
  adapter = new RecyclerViewAdapter(list, getApplicationContext());
  recyclerView.setAdapter(adapter);
  recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
````

Next we create a function inside our `MainActivity` class to set the new information and notify the adapter on the changed data.

````java
private void setData(String message, String time)
{
    list.add(new MessageData(message, time));
    adapter.notifyDataSetChanged();
}
````

## Lifecycle Methods

The underlying purpose of our app is to show **how lifecycle methods work**. The next step is therefore to add those methods to our activity. All lifecycle methods are defined in the class `Activity` which is a **super class** of our `MainActivity`. As we want to add our own code to these methods, we **override** them in our class.

> Add following methods to your `MainActivity` class: onStart, onResume, onPause, onStop and onDestroy.

The `@Override` is just a marker (annotation) which tells us that this method is intended to be overridden. It could be omitted without any ill effects. However, as a developer you are able to see **immediately** which methods are overridden from super classes and which are defined in your won class. This is very valuable information to have at a glance, so we like to keep those `@Override` annotations around.

One more thing to note is the call to ``super`` in all of those methods. It is important to keep this in all lifecycle methods. If we were to omit the `super` call, then super classes could not react to lifecycle events any longer, because according to the laws of java inheritance, only our lifecycle method gets called by the system. Therefore, it is imperative to include the call to the appropriate `super` method so that the code in the super classes also gets executed. Weird stuff can happen otherwise.

### Adding list items on lifecycle events
In each of the lifecycle methods, call the `setData` method. The message variable should be the name of the lifecycle (e.g. "onCreate" or "created"), and the time can be set by calling `Calendar.getInstance().getTime().toString()`. 

> Run the program and notice the lifecycle events happening when you put the app in the background repeatedly.

Notice the events happening always in a specific order. Also notice the ``onPause`` and ``onStop`` events happening right after one another. However, when putting the app in the background, we see only `onPause` on the screenshot. Even though those two events are happening back to back, `onPause` is the last event happening while the UI is still responsive while `onStop` happens when the UI is no longer updated, hence we don't see the event on the screenshot, even if we see it in the log file.


## FloatingActionButton

As a last step, we will add a ``FloatingActionButton`` to finally *destroy* the app. As the name suggests, this widget is a simple button that has a drop shadow underneath so that it looks like floating above the rest of the screen. We will use it to send the `onDestroy` event to the app.

Go to the layout editor by opening the layout file "activity_main.xml".
Add a ``FloatingActionButton`` as the last child of the ``ConstraintLayout`` (*be careful not to drop the ``FloatingActionButton`` as child of the ``RecyclerView``*). If asked, choose any image you like. Set an appropriate `id` (e.g. `fab_Destroy`). Set the constraints of the ``FloatingActionButton`` be be in the bottom right by dragging it's handles.

## Click and Destroy

Go back to our code in "MainActivity.java". We need to link the `FloatingActionButton` in the layout to our code and install a click listener. A click listener allows us to implement actions when the user clicks on the button. Add following lines to `onCreate`:

````java
        // Create local variable and link FAB widget
        FloatingActionButton fabDestroy = findViewById(R.id.fab_Destroy);
        // Install click listener
        fabDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnLifeCycle", "Finish it!!!!!1");
                MainActivity.this.finish(); // exits the activity
            }
        });
````

> Run the program again. When you click the button, you will find onDestroy() in the log.
