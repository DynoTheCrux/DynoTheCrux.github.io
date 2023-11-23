AndroidStudio
=============

AndroidStudio is the official *IDE* from Google for developing apps for Android based devices (smartphones, smartTVs, wearables, in-car entertainment, ...). The programming language is `Java`.

New project
=============

After starting AndroidStudio the welcome screen appears where existing projects can be opened or a new project can be created.

``` menu
Create New Project
```

<figure>
<img src="../assets/img/002_snake/welcome.png" id="fig:000_welcome" alt="Welcome Screen" /><figcaption aria-hidden="true">Welcome Screen</figcaption>
</figure>

In the next window the project can be started with different predefined views.

``` menu
Empty Activity
```

<figure>
<img src="../assets/img/002_snake/new_project.png" id="fig:001_new_project" alt="Dialog &quot;New Project&quot;" /><figcaption aria-hidden="true">New Project"</figcaption> dialog.
</figure>

The figure shows the settings to be made. The location can be chosen freely in principle, but experience shows that paths without spaces work best.

``` menu
Location: C:\Android\StudioProjects\AndroidTutorial
```

The next window is to select on which products the app should run later. When selecting the "Minimum SDK", AndroidStudio automatically calculates on which percentage of Android devices the app can run. If special features of the newer Android versions are not needed, an older version can be used. For our purposes, a current version is best.

``` menu
Minimum SDK: API 28: Android 9.0 (Pie)
```

``` menu
Finish
```

<figure>
<img src="../assets/img/002_snake/main_view.png" id="fig:002_main_view" alt="Main window of AndroidStudio." /><figcaption aria-hidden="true">Main window of AndroidStudio.</figcaption>
</figure>

The figure shows the main view of AndroidStudio. On the right is the project structure and in the left part is the open editor.
The entry point of the app is the file `MainActivity.java`.

An overview of the program window and the functions can be found in the [official documentation](https://developer.android.com/studio/intro/index.html).

MainActivity and Lifecycle
--------------------------

Views (cf. program window) in Android are called ``Activities`` and are all direct or indirect subclasses of `Activity`. In our case, the ``MainActivity`` is a direct subclass of ``AppCompatActivity``.

``` java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```

The program code shows that the newly created main view `MainActivity` is a subclass of the `AppCompatActivity` class defined by Android. The `onCreate` method is one of several "lifecycle" methods:

- `onCreate()`

- `onStart()`

- `onResume()`

- `onRestart()`

- `onPause()`

- `onStop()`

- `onDestroy()`

Via lifecycle methods an Activity is informed by the operating system in which state it is currently.

<figure>
<img src="../assets/img/002_snake/activity_lifecycle1.png" id="fig:activity_lifecycle" alt="State diagram of an activity with lifecycle methods." /><figcaption aria-hidden="true">State diagram of an Activity with lifecycle methods.<span class="citation" data-cites="androidlifecycle"></span></figcaption>
</figure>

For example, the task of a "Stock market" app is to display the most up-to-date stock data possible. Therefore, the app's Activity connects to a web service and updates the data every second. Impatient bankers want to open this app and be immediately informed about the situation on the stock market. So the Activity has to connect to the web service as soon as it is displayed. For example, the lifecycle method `onCreate()`, which is executed immediately after startup, is suitable for this. On the other hand, the data should no longer be updated every second when the app is running in the background. For this, some kind of throttling could be implemented in the `onStop()` method.

`onCreate(...)` in `MainActivity` overwrites the method from the superclass. Code in `AppCompatActivity.onCreate(...)` would not be executed. The call to `super.onCreate(...)` therefore explicitly calls the method in the superclass.

> It is convention when overriding lifecycle methods to also call code in the super class using `super.method()`.

The call `setContentView(...)` sets the layout of the view. More about this later.

The emulator
------------

The created project can already be transferred to the hardware and executed. In the early development phase, however, it is a good idea to use the Android emulator. This virtualized hardware behaves almost exactly like its real counterpart and is an important tool for testing the program. The key combination

``` menu
Shift + F10
```

or the click on the green play button starts the
hardware selection dialog if no emulator is available yet. With "Create New Emulator" a new emulator can be created. The figure shows the dialog after selecting device `Pixel 3a` and system
`Android 11 (x86)`.

<figure>
<img src="../assets/img/002_snake/new_emulator.png" id="fig:003_new_emulator" />
</figure>
 

Now the created emulator can be selected as the target device and after the startup process the virtual smartphone with the started project appears

<figure>
<img src="../assets/img/002_snake/hello_world.png" id="fig:004_hello_world" />
</figure>

The Layout Editor
-----------------

Attentive natures will have already wondered about the displayed text "Hello World", as there is nothing of it in the program code. An important design principle in Android is the separation of design and program logic. The code in `MainActivity` has (optimally) no influence on the design and the displayed elements.

For the displayed content the layout is responsible which is defined in the file `activity_main.xml` and can be found under `res\layout`.
Here you can also find the text element "Hello World".

For an introduction to the layout editor, please refer to the [Android Documentation](https://developer.android.com/studio/write/layout-editor.html) in. The figure shows the finished layout for the next steps. There are 2 text fields as well as 2 buttons included: 
- txtCounter (TextView)
- txtCounterValue (TextView)
- btnStart (Button)
- btnStop (Button)

<figure>
<img src="../assets/img/002_snake/layout_editor.png" id="fig:005_layout_editor" alt="Layout for tutorial." /><figcaption aria-hidden="true">Layout for tutorial.</figcaption>
</figure>

Connection between layout and program
---------------------------------------

In order for the layout not merely to be displayed but also to be provided with logic, a connection between *GUI* elements and the program code must be established. This is done in Android with the help of the Reflection object ``R``. This converts elements from the layout file in *XML* format into a Java object.
With the function `findByViewId(id)` elements from the layout can be loaded into the program code and modified.

``` java
public class MainActivity extends AppCompatActivity {

    // define variables representing the layout elements
    Button btnStart, btnStop;
    TextView txtCounterValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set linked layout
        setContentView(R.layout.activity_main);

        // connect layout elements to program variables
        txtCounterValue = (TextView) findViewById(R.id.txtCounterValue);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);

        // change some properties
        txtCounterValue.setTextColor(Color.RED);
        txtCounterValue.setText("something");
    }
}
```

View in layout editor:
<figure>
<img src="../assets/img/002_snake/layout_link_before.png" id="fig:006_layout_link_before" />
</figure>

View of the launched program:
<figure>
<img src="../assets/img/002_snake/layout_link_after.png" id="fig:006_layout_link_after" />
</figure>


Android Library "rtloop
========================

``` c
int _value;

int main(int argc, char *argv[])
{ 
    // do some initialization stuff
    
    while(1) { // loop infinitely
    
        // do something with _value
        
    }
}

void interrupt_handler(void)
{
    _value = // get value from some interface
}
```

This program code shows the typical structure of a program written in `C` for a microcontroller. From this example different structures can be seen:

- Entry point

- Setup

- Endless loop

- Asynchronous events

- End of program

At the start of the program (`main`) initializations (ports, interfaces, ...) are executed. In the endless loop the main task of the program (get/process/save data) is executed cyclically. At the same time, asynchronous tasks triggered by events (button pressed, counter value reached, ...) are executed.

Also PC programs and even the PC itself runs in principle after this scheme (boot process, operating system execute, asynchronous inputs process). 

However, the complexity increases considerably with increasing abstraction from the hardware level. However, a program in an operating system never runs alone and may therefore not implement simply a naive `while(1)` loop because thereby the entire *CPU* time of a program would be hogged by that program.

In this course, therefore, the basis for executing a program according to the scheme of a microcontroller is provided in the form of the Android library "rtloop". This library contains all necessary program parts to implement a "normal" Activity as simple as possible according to the scheme shown above without having to deal with the details.

FixedPeriodLooper
-----------------

The class ``FixedPeriodLooper`` represents the basic framework. The following code shows the basic structure of the class. The execution time of the
loop is monitored and a warning is issued if the time is exceeded (soft-realtime).

``` java
class FixedPeriodLooper {
    int period;
    FixedPeriodLooper(int period) { // set cycle time
        this.period = period;
    }

    void setup() {
        // perform setup tasks
    }
    
    void loopFixed() {
        // perform repeating tasks with fixed rate
    }
    
    void tearDown(boolean error) {
        if(!error) {
            // Perform cleanup when finished normally
        } else {
            // Perform cleanup/logging/analysis in case of error
        }
    }
}
```

FixedRateLoopActivity
--------------------

The first principle of a program with a user interface (UI) is that it must respond to input at all times (responsiveness). The effect when this is not the case is known when a program "freezes". Therefore it is important that no code is executed directly in the Activity, which can block the UI by too long execution times.

The Activity `FixedRateLoopActivity` is a subclass of `AppCompatActivity` (cf. `MainActivity`) and provides the functionality to execute a `FixedPeriodLooper`s in the background, i.e. without blocking the UI. It provides methods to control the loop execution:

- loopStart()

- loopStop()

- loopPause()

- loopResume()

- loopReset()

and provides lifecycle methods to react on status changes (e.g. abort on error):

- onLoopCreate()

- onLoopStart()

- onLoopPause()

- onLoopResume()

- onLoopStop()

Implementation
---------------

First of all, the module "rtloop" must be loaded into our program. This can be done in the menu under

``` menu
File -> New -> Import Module ...
```

(don't forget to unpack it). 

To use the rtloop library in the main program you have to link it first. Under "Gradle Scripts" there is a file ``build.gradle (Module: app)``. In this file, a line has to be added to "dependencies":

``` menu
implementation project(':rtloop')
```

So in the end the section "dependencies" will look similar to this:

``` menu
dependencies {
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.2.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation project(':rtloop')
}
```

Afterwards the project must be implemented with the menu items

``` menu
Tools -> Android -> Sync Project with Gradle Files
```

and

``` menu
Build -> Make Project (Ctrl + F9)
```

must be recompiled. Now the library can be used in the main program.

Instead of `AppCompatActivity` now `FixedRateLoopActivity` is used as superclass:

``` java
public class MainActivity extends FixedRateLoopActivity { // change from AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```

Next, start and stop of the loop is put on the two buttons.
For this, so-called "listeners" are set on the buttons. Listeners can be compared to interrupts and execute certain methods when defined events occur. The event "Button clicked" can be used here. The listener for the event `OnClick` is created by the method `setOnClickListener()`. The naming scheme of these methods is always "setOn**Event**Listener" or "addOn**Event**Listener" depending on whether only one or more listeners are allowed at the same time.

So under the assignment of the buttons in `onCreate` the listeners are added and `loopStart()` or `loopStop()` is called in it:

``` java
protected void onCreate(Bundle savedInstanceState) {
    // ... some more code above

    btnStart = (Button) findViewById(R.id.btnStart);
    btnStop = (Button) findViewById(R.id.btnStop);
    
    // add onClick listeners
    btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this code will be executed when start is clicked
                loopStart();
            }
        });
    btnStop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // this code will be executed when stop is clicked
            loopStop();
        }
    });
    // ... some more code below
}
```

To see if the loop is running or stopped, the counter value `txtCounterValue` in the title bar is updated per cycle. For this the two methods ``loopSetup`` and ``loopIteration`` must be overridden.

``` java
@Override
protected void loopSetup() {
    score = 0;
}

@Override
protected void loopIteration() {
    score++;
    setWidgetText(txtCounter, String.valueOf(score));
}
```

The method `setWidgetText(TextView,String)` is a method from `FixedRateLoopActivity` and was introduced because by a special feature of Android the user interface may be changed only directly from the Activity itself. Changes e.g. from lifecycle methods must therefore take the detour via methods provided by the Activity.



### Counter Pause/Resume

To demonstrate the lifecycle methods of the loop, a pause function is implemented: When the counter has been started, the label of the start button should change to "Pause" and a click should pause the counter. Once the counter is paused, the label shall change to "Resume".

<figure>
<img src="../assets/img/002_snake/loop_pause_states.svg" id="fig:loop_pause_states" alt="State diagram with button label." /><figcaption aria-hidden="true">State diagram with button label.</figcaption>
</figure>

In the `onClick` listener of the Start button, a distinction must be made between Start, Pause and Resume depending on the current state of the loop:

``` java
btnStart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        LoopState state = getLoopState();
        switch(state) {
            case CREATED:
            case STOPPED:
                loopStart(); // start if CREATED or STOPPED
                break;
            case RUNNING:
                loopPause(); // pause if RUNNING
                break;
            case PAUSED:
                loopResume(); // resume if paused
                break;
        }
    }
});
```

Additionally, the label of the start button is changed using the lifecycle methods:

``` java
public class MainActivity extends FixedRateLoopActivity {
    // ... some more code above
    
    @Override
    protected void onLoopResume() {
        super.onLoopResume();
        setWidgetText(btnStart, "Pause");
    }
    
    @Override
    protected void onLoopStart() {
        super.onLoopStart();
        setWidgetText(btnStart, "Pause");
    }
    
    @Override
    protected void onLoopPause() {
        super.onLoopPause();
        setWidgetText(btnStart, "Resume");
    }
    
    @Override
    protected void onLoopStop() {
        super.onLoopStop();
        setWidgetText(btnStart, "Start");
    }
    
    // ... some more code below
}
```

<figure>
<img src="../assets/img/002_snake/layout_slider.png" id="fig:007_layout_slider" alt="Layout with additional Seekbar and TextView." /><figcaption aria-hidden="true">Layout with additional Seekbar and TextView.</figcaption>
</figure>

### Change period

To be able to control the frequency of loop calls from the UI a `SeekBar` ``skbFreq`` is created in the layout as well as another ``TextView` ``txtFreq`` as in the figure.

``` java
public class MainActivity extends FixedRateLoopActivity {
    // ... some more code above
    
    Button btnStart, btnStop;
    TextView txtCounterValue;
    
     // add new variables for UI elements
    TextView txtFreq;
    SeekBar skbFreq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCounter = findViewById(R.id.txtCounter);
        txtCounter.setText("");

        txtFreq = findViewById(R.id.txtFreq);

        skbFreq = findViewById(R.id.seekBar);
        skbFreq.setOnSeekBarChangeListener(new AbstractOnSeekBarChangeListener() {
            final double minValue = 1;
            final double maxValue = 50;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int freq = (int) (minValue + ((maxValue-minValue)*(progress/100.0)));
                setLoopFrequency(freq);
                setWidgetText(txtFreq, "" + freq + " Hz");
            }
        });
        skbFreq.setProgress(5);
    // ... some more code below
```



Application
=========

Another Android library "snake" is to show a simple application of the looper. It is a simple version of the Nokia classic "Snake".

To be able to use the module after the import, the module must be added as a dependency in the file "build.gradle (Module: app)". Both modules are defined as dependencies:

``` menu
dependencies {
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.2.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation project(':rtloop')
    implementation project(':snake')
}
```

The custom element `SnakeView` is then inserted into the existing layout. Since this is not a standard element, it cannot be inserted via the layout editor. The layout file ``activity_main.xml`` must be opened in text mode and in the already created `FrameLayout` the new element is added:

``` xml
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_weight="1">

    <edu.mci.snake.views.SnakeView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/snakeView" />
</FrameLayout>
```

The next step is to modify the loop methods to control the game instead of the simple counter.

``` java
// ... more code above
    @Override
    protected void loopSetup() {
        score = 0;
        snakeView.setup(20,30,5,true, 10, 20);
        snakeView.start();
    }

    @Override
    protected void loopIteration() {
        score++;
        setWidgetText(txtCounter, String.valueOf(snakeView.getScore()));

        GameState state = snakeView.update();
        if(state == GameState.GameOver) {
            loopStop();
        }
    }

    @Override
    protected void loopTearDown(boolean error) {
        snakeView.stop();
    }
// ... more code below
```

With a click on the start button the game is started. The only thing missing is the control functionality.

Control
====================
At the end we implement the keyboard control for the emulator:

``` java
public class MainActivity extends FixedRateLoopActivity {
    
    // ... SOME OTHER CODE ABOVE

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getLoopState() == LoopState.RUNNING) {
            Direction dir = directionFromKeyEvent(event);

            if (dir != Direction.Forward) {
                snakeView.turnSnake(dir);
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getLoopState() == LoopState.RUNNING) {
            Direction dir = directionFromMotionEvent(event);

            if (dir != Direction.Forward) {
                snakeView.turnSnake(dir);
                return true;
            }
        }
        return false;
    }

    private Direction directionFromKeyEvent(KeyEvent event) {
        Direction dir = Direction.Forward; // neutral direction

        if (event.getAction() == KeyEvent.ACTION_DOWN) { // only when first pressed
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_A:
                    dir = Direction.Left;
                    break;
                case KeyEvent.KEYCODE_D:
                    dir = Direction.Right;
                    break;
            }
        }

        return dir;
    }

    private Direction directionFromMotionEvent(MotionEvent event) {
        Direction dir = Direction.Forward; // neutral direction

        if (event.getAction() == MotionEvent.ACTION_DOWN) { // only when first pressed
            if (event.getX() <= ((snakeView.getWidth() / 2))) { // left side of @SnakeView
                dir = Direction.Left;
            } else { // right side of @SnakeView
                dir = Direction.Right;
            }
        }

        return dir;
    }
}
```

In the emulator the snake can now be steered with the keys "A" and "D" to the left or to the right. With touchscreen a tap on the left side of the field causes a left turn and a tap on the right side causes a right turn.



Final view
====================

<figure>
<img src="../assets/img/002_snake/snake_final.png" id="fig:008_snake_final" alt="Finished view of the Workshop &quot;Snake&quot; app." /><figcaption aria-hidden="true">Final view of the Workshop "Snake" app.</figcaption>
</figure>
