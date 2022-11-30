# Overview
{: .reading}

* This will become a table of contents (this text will be scrapped).
{:toc}

# Workshop: Intents
{: .reading}

![Final app Main](../../assets/img/005_intent/Main.png)
![Final app Show](../../assets/img/005_intent/Show.png)

In this session we will have a look how **Intents** work and what they are used for. Go through the steps to add implicit and explicit intents. Use logging to check whats happening.

## Open the IbkActivityPlanner

Download the .zip archive and save it to a location you prefer and unpack it. Then open Android Studio, choose File->open to open the project.

## Explore the App

If you have not already, have a look around the app. What kind of activites are already there and what are they doing?

We have two activites, the `MainActivity` and the `ShowActivity`. In this case we could guess which one would be launched first when the app is started, but there are cases where the `Main` is actually not the launched activity, e.g. imagine a log-in activity which is only called when opening the app the first time. One possibility is to have a look on the intent-filter in the manifest file discussed later in this lesson. First let's have a look how we switch from first to second activity.

## Intent to start the show activity

In the `MainActivity.java` we find the following code in the onClick Listener of the button to start the show activity:

````java
Intent i = new Intent(MainActivity.this, ShowActivity.class);
startActivity(i);
````
Basically the intent specifies the starting context and where it should move to. The `startActivity()` method then does the actual work. As we stay inside the activity and tell the exact target it is an *implicit* intent.

### Intent with extra information

Sometimes we want to pass on basic information from the starting activity to the target activity. A good example could be the `isGoodWeather` variable. Now it is implemented as a static variable and a getter to be used by the other activity. Let\'s change that to a android specific process of using an intent with extra information.

First you can delete the getter and the static keyword for the variable. Then simply add the line:

````Java
i.putExtra("currentWeather", isGoodWeather);
````
in between creating the intent and starting the next activity. To make it work, switch to the `ShowActivity.java` file. The information is stored as a so called *key-value pair*, i.e. the specified string can be anything you like. You just have to remember the *key* as soon as you need the *value*. At the beginning of the `onCreate()` method (but after `super...` and `setContentView(...` add the lines:

````Java
Intent myIntent = getIntent();
final boolean currentWeather = myIntent.getBooleanExtra("currentWeather", true);
````

Finally, adapt the code to work with the new variable instead of the getter.

Have a testrun of the app and see if the information is passed on successfully.

## Explicit intents

The second type of intents are explicit, that means we ask the system to perform a certain action but we do not specify how and by whom it will be carried out. In this example you will implement two different ones:
- Conducting a websearch for our current weatherforecast in innsbruck and
- Send a message to a friend to ask to join you in your activity.

Before you can do that, add an additional button in the `main_activity.xml` and the `activity_show.xml` and connect them to the code. Create onClick listeners where you can add the intents. The intent to check the weather is an explicit intent using the action `ACTION_WEB_SEARCH` while the other one uses the action `ACTION_SEND`. But how does the system know who can actully perform such an action?

### The intent filter
As mentioned before, the intent filter is to be found in the manifest file of the android project. It belongs to an activity and looks like this:

````XML
        <activity
		...
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
		...
		</activity>
```` 
You can see that for this activity the action `MAIN` is specified whereas the category specifies who can ask for this kind of action. This basically tells us that the activity will be the activity that will be started by the launcher initializing the application.

In the use context of the innsbruck activity planner this means that some other applications have to have one of these two actions specified in their intent filter to be able to perform receive the intent.

### Implement explicit intents

In the listeners created before you can add the websearch intent by adding the lines:

````Java
Intent intentCheck = new Intent(Intent.ACTION_WEB_SEARCH);
intentCheck.putExtra(SearchManager.QUERY, "Wetter Bergfex Innsbruck");
startActivity(intentCheck);
````

and 

````Java
Intent sendIntent = new Intent();
sendIntent.setAction(Intent.ACTION_SEND);
sendIntent.putExtra(Intent.EXTRA_TEXT, "Bisch am Start?");
sendIntent.setType("text/plain");
startActivity(sendIntent);
````

respectively. As you can see, the extra information which is needed or optional for the intent varies by the action. You can have a look on the most common ones and examples [here](https://developer.android.com/guide/components/intents-common){:target="_blank"}.

# Improve the UI


# Add audio

