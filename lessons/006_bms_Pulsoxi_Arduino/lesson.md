# Overview
{: .reading}

* This will become a table of contents (this text will be scrapped).
{:toc}

# Hintergrund

Du kennst das Pulsoximeter nun ausreichend aus der Vorlesung und weißt:
- Wie Pulsoximeter im Allgemeinen funktionieren und
- Wie die Elektronik und das poMCI aufgebaut ist.

>Falls du dir bei manchen Punkten nicht sicher bist, schau nochmal in den Slides nach oder Frage deine Kolleg:innen bzw. den Lektor.

Das Ziel dieser Lesson ist nun, den Code für das poMCI und den Arduino UNO eigenständig zu implementieren. Ihr werdet, dann den Code im Labor verwenden und mit dem von euch gelöteten Pulsoximeter Messungen durchführen. Den Matlab Code um die Messungen zu verarbeiten werdet ihr in einer separaten Lesson schreiben.

Der Sketch an sich besteht wie immer aus `setup()` und `loop()`. Am Ende soll dieser eine Sequenz aufnehmen mit folgenden Eckdaten:
- 400 Hz Sampling Rate. Damit geht sich nach dem Sampling Theorem locker aus den Puls zu erfassen und für SpO2 ist die Samplingrate praktisch irrelevant.
- 8000 Samples bzw. 20 Sekunden Aufzeichnung.

Ein Sample besteht dabei aus den Reflexionswerten der Roten und infraroten LED. Diese sollen während der Aufzeichnung über die serielle Schnittstelle vom Arduino UNO ausgegeben werden. Die Aufzeichnung soll starten, sobald der User einen Startbefehl gegeben hat.

# Vorbereitung

Es kommt der MAX30101 zum Einsatz. Um die Werte des Sensors auszulesen, benötigt es die passende Library. Diese ist auf Sakai hinterlegt und kann in den Arduino Sketch auf zwei Arten eingebunden werden:
- Die Library wird in den *Libraries* Ordner in einem eigenen Ordner mit gleichen Namen der Library (daher *MAX30105*) kopiert oder 
- Die Library wird in denselben Ordner kopiert wie euer `.ino` File, auch bekannt als der *Sketch Folder*.

> Kleiner C++ Fact am Rande. Werden Libraries mit `"XY.h"` eingebunden, sucht die IDE als erstes im selben Folder wie der Code. Wird die Library mit `<XY.h>` eingebunde, sucht sie erst im Libraries Ordner im Installtionsverzeichnis, bzw. genauer gesagt in den Ordnern der *include path list*.

Denkt daran, dass Libraries in C++ immer aus `.h` und `.cpp` File bestehen. Leider ist die Dokumentationslage für die MAX3010x Sensorfamilie etwas undurchsichtig, für mich hat die *MAX30105* am besten funktioniert und ist auch mit dem MAX30101 und dem Arduino UNO kompatibel. Ihr könnt natürlich auch gerne eine andere Library suchen und ausprobieren.

> Den *Libraries* Ordner findet ihr im Installationsverzeichnis, je nachdem welche Version ihr verwendet und wo ihr sie installiert habt. Bei Version 2.0.4 z.B. hier: *C:\Users\YOURNAME\AppData\Local\Arduino15\libraries\MAX30105*. Der *AppData* Ordner muss manchmal erst eingeblendet werden.

Des weiteren, brauchen wir die `Wire.h`, da die Kommunikation mit dem Board via I2C läuft. Diese sollte euch ja bereits bekannt sein. Falls ihr sie nicht schon installiert habt, installiert sie über den manuellen Weg oder den eingebauten Library Manager in der Arduino IDE. Bindet beide Libraries in den Sketch via `#include` ein.

Timer Library brauchen wir ausnahmsweise keinen, da die Samplerate direkt für die Sensor IC konfiguriert wird.


# Implementierung

## Kommunikation und Sensorkonfiguration

Nachdem die Libraries eingebunden sind, sollte der Sketch nun startklar sein, um den Sensorcode zu implementieren. Im `setup()` des Arduino Sketches kümmern wir uns dazu erst einmal um die Konfiguration der Kommunikation und des Sensors. Schreibe dazu erst den Code, um die serielle Kommunikation zwischen Arduino und Rechner/Arduino IDE zu starten. Checke, ob die Kommunikation wirklich vorhanden ist. Diese könnte auch dazu genutzt werden, um einen Startbefehl für die Aufzeichnung vom Serial Monitor / Plotter an den Arduino UNO zu senden. Baudrate sollte etwas höher gewählt werden (z.B. 115200), da wir einiges an Daten mit etwa 400 Hz senden wollen.

Im nächsten Schritt wollen wir, ähnlich zum MPU6050 Beispiel, checken, ob der Sensor wirklich vorhanden ist. Die Bedingung dazu ist:

````C++
pulseSensor.begin(Wire, I2C_SPEED_FAST) == false
````

Ist diese Erfüllt, ist am Standard I2C Port (der andere wäre `_SLOW`) der Sensor nicht auffindbar sollte die Verkabelung etc. gecheckt werden und das Programm nicht weiter fortlaufen.

Wird der Sensor am I2C Port erkannt, können wir ihn für die Aufnahme der Sensordaten konfigurieren. Erstelle dazu ein Objekt für den Sensor von Typ `MAX30105`. Zur Konfiguration wird die Methode `setup()` (des Sensors, nicht des Arduino Sketches!) mit den folgenden Übergabeparametern aufgerufen:
- `byte powerLevel`: Bestimmt, wie hell die LEDs leuchten. Mögliche Werte gehen von 0 bis 255 (ein Byte eben), wobei 0 ausgeschalten und 255 am hellsten wäre. Eine passende Wahl um zu starten wäre etwa die Hälfe mit 125 oder 0x7D als Hex-Nummer.
- `byte sampleAverage`: Bestimmt, ob der Sensor die samples Mitteln soll. Wir wollen die Werte im Nachhinein verarbeiten. Wählen wir den Wert von 1 gibt er die Werte 1 zu 1 wie gelesen weiter. Andere Werte wären im Prinzip ein *moving Average* Filter. Zur verfügung stehen die Werte **1**, 2, 4, 8, 16 und 32.
- `byte ledMode`: Damit können die einzelnen LEDs an- bzw. ausgeschalten werden. So könnte die grüne LED alleine gewählt werden, um nur den Puls zu erfassen. Wir wollen die Rote und infrarote LED lesen und daher den Modus 2.
- `int sampleRate`: Wie oben angegeben, in Hz. Nebenbei sei erwähnt, dass nur bestimmte Werte zur Verfügung stehen (50, 100, 200, **400**, 800, 1000, 1600, 3200).
- `int pulseWidth`: Da der Sensor nur über einen Fototransistor verfügt, aber drei Wellenlängen erkannt werden müssen, wird jeder LED ein Time Slot zugeteilt. Dazu kann die Pulsweite, also die *On-Time* für die LEDs gewählt werden. Das mittlere Setting wäre mit 215 passend, verfügbar sind 69, 118, **215** und 411.
- `int adcRange`: Der ADC welcher das Signal des Fototransistors digitalisiert, kann in dessen Auflösung konfiguriert werden. Das mittlere Setting wäre mit 13 Bit passend, verfügbar sind 2048, 4096, **8192** und 16384.

> Ein Objekt in C++ wird normalerweise in der Sektion unter den Präprozessoranweisungen (`#`) bei den Variablen erstellt. Die Syntax ist folgendermaßen: `Typ nameDesObjektes;`. Der Typ ist dabei von der Library vorgegeben. Mit `nameDesObjektes.methode();` kann dann auf Methoden des Objektes zugegriffen werden.

Damit kannst du die Methode mit den gewählten Parametern aufrufen und den Sensor konfigurieren.

## Timing

Um die Sequenz mit einer bestimmten Länge aufzunehmen, können zwei Wege begangen werden. Entweder wir checken, ob die 8000 Samples aufgenommen wurden, oder ob die 20 Sekunden abgelaufen sind. Zweiteres ist etwas vielseitiger einsetzbar und wird daher hier kurz besprochen. Ein Standardmuster in der Controller Welt ist die Zeit mit `millis()` zu Messen. Diese Funktion gibt den Wert der Controller Clock in Millisekunden aus. Da der Wert vom Zeitpunkt des Einschaltens abhängig ist, muss daher immer in Differenzen gedacht werden So wird eine Zeit gemessen indem:

````C++
startTime = millis();
 // Something else is happening that takes some time. Imagine a Granny walking over a crosswalk //
endTime = millis() - startTime;
````

Wenn nun eine Sequenz von 20 Sekunden aufgenommen werden soll, wollen wir die Startzeit im letzten Schritt vor dem `loop()` nehmen und innerhalb des `loop()` mit den 20 Sekunden vergleichen. Ist die Zeit abgelaufen, stoppen wir den Sensor mit `mySensorName.shutDown();`.

## Sensor Samples Auslesen

Im `loop()` wollen wir nun die Samples auslesen, die der Sensor aufgenommen hat. Hier ist ein entscheidender Unterschied zur Lesson mit der MPU6050. Da der Sensor einen FIFO-Buffer integriert hat, müssen wir eigentlich keinen dezidierten Timer verwenden. Die Werte werden vom Sensor in der konfigurierten Samplerate aufgenommen und können, wann es uns passt vom Buffer ausgelesen werden. Dies läuft in drei Schritten ab:
````C++
 mySensorName.check(); // Reads the Buffer, up to 3 samples
 mySensorName.getFIFORed(); // Gets the Red Values from the Buffer
 mySensorName.getFIFOIR(); // Gets the IR Values from the Buffer
 mySensorName.nextSample(); // When finished reading, move to the next Sample
````

Außerdem kann `mySensorName.available()` verwendet werden, um zu checken, ob sich nach dem Lesen des Buffers tatsächlich **neue** Daten darin befinden. Im Prinzip sollte der, nachdem der Buffer gelesen wurde, eine Schleife gestartet werde, die überprüft, ob (noch) neue Daten verfügbar sind. Solang dies der Fall ist, werden die Roten und infraroten Werte über die serielle Kommunikation ausgegeben und Sample für Sample abgearbeitet.

Damit sollte der Code für das Labor soweit fertig sein.

# Abgabe

Da der Code erst im Labor getestet wird, könnt ihr nur die Syntax überprüfen, indem ihr den Code für einen Arduino UNO kompiliert (*Verify*). Checkt bitte trotzdem so gut es geht, ob der Code denn richtig laufen würde. Ladet dann euren Sketch i.e. das `.ino` File beim Assignment hoch.



<!-- {: .reading}

The User Interface (UI) or Graphical User Interface (GUI) is arguably the most important part of a program. True, functionality is crucial too, but all the functions of non-trivial programs are hidden behind the UI. If the UI is poorly designed, users tend to look for alternatives.

In this session, we will have a look at the tools available to design a basic UI. As an example, we will create an activity that shows a simple **contact form** where the user can input personal details and a message:

![Simple contact form](../../assets/img/003_ui/screen_final.png)

In this example, we will use different **Widgets**

- [TextView](https://developer.android.com/reference/android/widget/TextView){:target="_blank"}
- [EditText](https://developer.android.com/reference/android/widget/EditText){:target="_blank"}
- [ImageView](https://developer.android.com/reference/android/widget/ImageView){:target="_blank"}
- [Switch](https://developer.android.com/reference/android/widget/Switch){:target="_blank"}
- [Button](https://developer.android.com/guide/topics/ui/controls/button){:target="_blank"}

and **Layouts**

- [ConstraintLayout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout){:target="_blank"}
- [LinearLayout](https://developer.android.com/guide/topics/ui/layout/linear){:target="_blank"}
- [TableLayout](https://developer.android.com/reference/android/widget/TableLayout){:target="_blank"}

as well as some layout elements to build the user interface according to the picture.

# AndroidStudio Layout Editor
*AndroidStudio* includes a powerful [**layout editor**](https://developer.android.com/studio/write/layout-editor){:target="_blank"} that makes building a functional UI relatively easy. However, as is common with powerful tools, there is a **learning curve** involved. Due to the wide array of possibilities to design the interface, it may be hard to find your way around the editor in the beginning.

The Layout Editor appears when you open an XML layout file.

![AndroidStudio Layout Editor](../../assets/img/003_ui/layout-editor-2x.png)

1. **Palette**: Contains various views and view groups that you can drag into your layout.
2. **Component Tree**: Shows the hierarchy of components in your layout.
3. **Toolbar**: Click these buttons to configure your layout appearance in the editor and change layout attributes.
4. **Design editor**: Edit your layout in Design view, Blueprint view, or both.
5. **Attributes**: Controls for the selected view's attributes.
6. **View mode**: View your layout in either Code code mode icon, Design design mode icon, or Split split mode icon modes. Split mode shows both the Code and Design windows at the same time.
7. **Zoom and pan controls**: Control the preview size and position within the editor.

## Design View and Code View
The layout editor enables us to design the UI by dragging **widgets** like a *TextView* onto the screen and adjusting its attributes with a live preview. This is called the **design view**.

The **actual layout code** can be seen when switching to **code view**. There we see the XML code that the layout is based upon.

````xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="80dp">

        <ImageView
            android:id="@+id/imageView"
            android:layout_width="match_parent"

...
````

The actual layout is **only** defined in code in the XML. The design that we see in design mode is just a representation. When we change something in design mode, the actual change is done in the code and the design is updated accordingly. Very complex designs are done in code, rather than using the design view. However, in this course we will only seldom find a reason to design directly via code view.

# Workshop: Contact Form Activity
{: .reading}

Let's start by creating a new AndroidStudio project using the "Empty Activity" template.

![Empty Activity template](../../assets/img/003_ui/empty_activity.png)

This template creates a first activity with a simple UI. Select "app/res/layouts/activity_main.xml" in the project tree:

![activity_main.xml](../../assets/img/003_ui/activity_main_xml.png)

This opens the layout editor and shows an UI that consists of a ``ConstraintLayout`` that fills the whole screen and a ``TextView`` with the text "Hello World!" written in the center of the screen:

![Simple UI](../../assets/img/003_ui/hello_world.png)

If we have a look at the code view, we see the according XML:

````xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
````

## 'root' Layout
The uppermost layout that is shown in the component tree is called the **root layout**. By default, this is of the type ``ConstraintLayout`` and you should leave it this that way if you don't have compelling reasons to change it. The ``ConstraintLayout`` is one of the most flexible layouts and therefore well suited as the root layout.

## 'TextView' Widget
One of the most basic widgets that is available in the **Palette** is the ``TextView`` to display some text. Some important **attributes** are

- **id**: The *unique* id of the widget
- **text**: The text that is displayed
- **textSize**: The size of the text
- **textStyle**: Normal/bold/italic text style
- **style**: Predefined text style like *header*, *label*, ...
- **textColor**: The font color

**Play around with different attributes to notice their effect.**

>Afterwards, delete the ``TextView`` that displays the "Hello World!" message to continue.

# Creating the Basic Layout Structure
![Basic linear layout](../../assets/img/003_ui/screen_basic_list_layout.png)

When looking at the proposed UI, you should notice the basic layout has a **row-like structure**. There is a header including a background image, a section to input personal information, a row for the message and so on.

The easiest way to build such a structure is using a ``LinearLayout (vertical)``. This layout creates sections that behave like a list of rows. Exactly what we want in this case.

> Drag and drop a ``LinearLayout (Vertical)`` from the Palette (Section: Layouts) onto the root layout.

Beware that the `LinearLayout (Vertical)` is a direct child of the `ConstraintLayout`:

![Component Tree](../../assets/img/003_ui/component_tree_lin_layout1.png)

When you select the ``LinearLayout`` in the component tree, you will notice red exclamation marks in the layout section of the attributes, telling you that it is
- Not horizontally constrained
- Not vertically constrained
  
![Unconstrained](../../assets/img/003_ui/attr_lin_layout1_unconstrained.png)

Any child of ``ConstraintLayout`` needs to have horizontal and vertical constraints defined, hence the name. In our case, we want the ``LinearLayout`` to fill the entire activity screen. So we define constraints such that

- the **left edge** of the ``LinearLayout`` has *0 offset* from the **parent's left edge** (``ConstraintLayout``)
- the **top** of the ``LinearLayout`` has *0 offset* from the **parent's top**
- the **right edge** of the ``LinearLayout`` has *0 offset* from the **parent's right edge**
- the **bottom** of the ``LinearLayout`` has *0 offset* from the **parent's bottom**


> Click on the blue ``+`` signs to create constraints, leaving the default value of ``0``. In our case, this adds the attributes

- ``layout_constraintBottom_toBottomOf="parent"``
- ``layout_constraintTop_toTopOf="parent"``
- ``layout_constraintStart_toStartOf="parent"``
- ``layout_constraintEnd_toEndOf="parent"``

![Unconstrained](../../assets/img/003_ui/attr_lin_layout1_constrained.png)

When you read the created attributes' names carefully, you should notice that you can read the constraints they represent like an english sentence:

>``layout_constraintBottom_toBottomOf="parent"``:
>*Constrain the bottom of [this layout] to the bottom of the parent, with 0 offset*.

In fact, we made the ``LinearLayout`` take up the same screen space as its parent, the ``ConstraintLayout``.

**If you break your layout at any point, feel free to copy the code of the linked activity_main.xml in each step into your own code view:**

[>Layout Code for this step<](../../assets/source/003_ui/01_activity_main.xml){:target="_blank"}

## Image Header
Let's fill our layout with life and create a simple image header. In the picture of the final layout, we see that the header consists of an image with some text printed on top of it.

![Image header](../../assets/img/003_ui/image_header.png)

If we have a look at the **Blueprint view**, we see the structure even clearer.

![Image header blueprint](../../assets/img/003_ui/image_header_blueprint.png)

> Drag and drop following widgets from the palette to your component tree:
> - ``ConstraintLayout`` as **child** of our LinearLayout
> - ``ImageView`` as **first child** of the new ConstraintLayout (choose the image "backgrounds/scenic" for now, when asked)
> - ``TextView`` as **second child** of the new ConstraintLayout

At this point, your component tree should look like this:

![Component tree with image header](../../assets/img/003_ui/component_tree_image_header.png)

### Styling the Image Header

Right now, the header takes up the whole screen. The reason for this is, that the ``ConstraintLayout`` that is a child of the ``LinearLayout`` has the attribute ``layout_height`` set to ``match_parent``, i.e. it has the same height as ``LinearLayout``, which is the whole screen.

> Change the ``layout_height`` to ``80dp``

![ConstraintLayout after setting the layout_height](../../assets/img/003_ui/layout_image_header_80dp.png)

Next, let's have a look at the ``TextView`` and ``ImageView``. their layout attributes are also way off, right now they are located down beneath the bottom of the screen. However, you may have noticed that both have the red exclamation marks shown next to them, meaning that they are still unconstrained. Let's change that now:

> Select ``TextView``
> 
> Set the attribute ``layout_height`` to ``0dp`` (``0dp (match_constraint)`` when using the drop down menu)
> 
> Click on the blue ``+`` circles in the layout section of the attributes and fill in `0` as the offset on each side (like we did earlier)
>
> Repeat this for the ``ImageView``

The image header should now look like this:

![Image Header](../../assets/img/003_ui/layout_image_header_layout_done.png)

### ImageView
Now we see that the image does not fit the header. We need to change an attribute which controls how the image is scaled inside the available space.

> Select the `ImageView` in the component tree
> 
> Change the attribute `scaleType` to `centerCrop`

> **Hint**: To easily find attributes in the list, you can use the search function by clicking on the magnifying glass icon
> 
> ![Attribute search icon](../../assets/img/003_ui/attributes_search.png)

### TextView
Now let's style the text inside the image to something that looks halfway decent.

> Select the `TextView` in the component tree
>
> Change the attributes:
> - ``text``: `Contact us...`
> - ``textSize``: `30sp`
> - ``layout_height``: `wrap_content`
> - ``textColor``: Select `white`
> - ``paddingStart``: ``30dp`` (expand the attribute ``padding`` to see this)

Notice the changes each attribute has on the layout. The values provided are just for reference, **feel free to adjust them to your liking**. In the end, it will look similar to this:

![Image Header finished](../../assets/img/003_ui/layout_image_header_fin.png)

[>Layout Code for this step<](../../assets/source/003_ui/02_activity_main.xml){:target="_blank"}

# Personal Details Input
The next section to work on is where the users should input their personal details. It consists of multiple rows of text labels and input fields. The final version could look something like this:

![Details input section](../../assets/img/003_ui/details_input_ex.png)

![Details input section (Blueprint)](../../assets/img/003_ui/details_input_ex_blueprint.png)

The Blueprint view of this section shows, that the structure resembles a table, so we will use a ``TableLayout``.

> Start by adding a ``TableLayout`` from the palette into your component tree. It should be the last child of the ``LinearLayout``.
>
> Next, add 4 ``TableRow`` as children of ``TableLayout``.

The resulting component tree will look like this:

![Component tree with table layout](../../assets/img/003_ui/component_tree_table1.png)

Now that we have the rows of the table in place, let's place the widgets. The ``TableLayout`` will assign each widget that is a **direct child** of a ``TableRow`` its own column. So if we add 3 direct children to a ``TableRow``, the resulting table will have 3 columns.

Some of our rows have 2 columns, while the "Height" input row has **one extra column**. We have to be careful to add **the same number of child widgets** to each row. So when we want to have an empty space somewhere, we add a `Space` (Palette: Layouts) widget instead.

> Keep adding widgets into your layout according so that it corresponds to the following table:

|        | Column1      |      Column2  |      Column3 |
|--------|:-------------|:---------------|:-------------|
|**Row1**| ``TextView`` | ``EditText`` (Plain Text) | ``Space``    |
|**Row2**| ``TextView`` | ``EditText`` (Date) | ``Space``    |
|**Row3**| ``TextView`` | ``EditText`` (Number(Decimal)) | ``TextView`` |
|**Row4**| ``TextView`` | ``EditText`` (Email) | ``Space``    |


Notice that ``EditText`` takes many specialized forms that differ in the type of text that a user can enter into it, as well as the type of keyboard that is shown to the user. You can find all the available forms in the palette in the "Text" section.

Your component tree should now look similar to this:

![Component tree with table layout](../../assets/img/003_ui/component_tree_table2.png)

## Widget IDs

Before the current input section, we only added layouts and static content (the header), so we did not care about the specific IDs that the widgets had. Now this is different, as we added input fields which at a later point we have to access from our Java code to save or send their values.

It is convenient to adhere to a common naming scheme for your widgets. When you work with the widgets in the code, you only have the variable name to guess what kind of widget a variable holds. Therefore, it is common to add the type as a prefix to the name: `prefixName`

Examples:
- `txtName`: ID of a text input field that is supposed to hold a name.
- ``btnSend``: ID of a button that performs a send operation.

As a suggestion, you can use following prefixes for the most common types:
- `lbl`: For labels (`TextView`)
- `txt`: For text input fields (`EditText`)
- `btn`: For buttons (`Button`)
- `chk`: For checkboxes (`CheckBox`)
- `tb` : For toolbars (`ToolBar`)

For other types you can use the type itself as the prefix:
- `switch` for switches (``Switch``)
- `map` for maps (``MapView``)

> Assign a sensible attribute `id` to each widget in the `TableRow`s (except the spaces).
> 
> Be careful, IDs have to be **unique**.

![Component tree with table layout and sensible widget IDs](../../assets/img/003_ui/component_tree_table3.png)

## Styling
Our details input section still does not look good, so let's get to work.

First of all, the ``TableLayout`` takes up all the extra screen space at the bottom. It's `layout_height` is set to `match_parent` by default.

> Change the attribute `layout_height` to `wrap_content`.

Also, let's change the text of the widgets.

> Change the `text` attribute of the labels accordingly.
>
> Add `text` to the input field so that it does not look so empty.

![Component tree with table layout and sensible widget IDs](../../assets/img/003_ui/component_tree_table_fin.png)

## Layout weight
For the label-input combinations to look nicer, let's add a layout weight.

A layout weight (`layout_weight`) makes widgets grow **if** there is extra space available. The whole extra space is then added to each widget on the same level where the attribute `layout_weight` is greater than 0.

The formula is:

``extra_widget_size = empty_space_available * (layout_weight / cumulative_layout_weight)``

Example:

Suppose there is ``60px`` empty space available in a row containing 3 widgets. ``widget1`` has a `layout_height` set to `2`, ``widget2`` has it set to `1` and ``widget3`` does not have the attribute `layout_height` declared at all.

In this case, ``widget1`` will get an extra ``40px`` (60\*2/3) while ``widget2`` can grow by ``20px`` (60\*1/3). ``widget3``'s size does not change.

Check the [official documentation](https://developer.android.com/guide/topics/ui/layout/linear){:target="_blank"} for details.

> Add the attribute `layout_weight` with a value of `1` to each widget in the first two columns.

## Result

At this point, your layout should look similar to this:

![Finished input section](../../assets/img/003_ui/layout_table_fin.png)

[>Layout Code for this step<](../../assets/source/003_ui/03_activity_main.xml){:target="_blank"}

# Message Input

The next section to work on is the message input. In the end it should look like this:

![Finished message section](../../assets/img/003_ui/layout_message_input_ex.png)

![Finished message section (Blueprint)](../../assets/img/003_ui/layout_message_input_ex_blueprint.png)

So in this section, which is in the **third row of the initial vertical** ``LinearLayout``, we want to have a small header and a larger message body in a vertical layout.

This should be easy, we only have to add things we already know.

> Add a `LinearLayout (vertical)` as the **third** child of the initial `LinearLayout`. Change the new layout's `id` attribute to `LinearLayoutMessage`.
>
> Change the `layout_height` of `LinearLayoutMessage` to `match_content`.
>
> Add a `TextView` and a `EditView` of variant "Multiline Text" as children of `LinearLayoutMessage`.
> Choose sensible `id`s and fill the `text` attributes so that there is appropriate content. *Hint: use `\n` to include a new line in the text*

Afterwards, your component tree will look like this

![Component tree](../../assets/img/003_ui/component_tree_message1.png)

and the layout should look similar to

![Layout](../../assets/img/003_ui/layout_message_input_fin.png)

[>Layout Code for this step<](../../assets/source/003_ui/04_activity_main.xml){:target="_blank"}

# Email Options Section
In order to customize the user experience, we want to include a section where the user is able to select options. 

We will only need the one option to let the user choose to also send a copy of the contact form to his own email address once it is sent.

![Email option](../../assets/img/003_ui/layout_copytoself_ex.png)

![Email option (Blueprint)](../../assets/img/003_ui/layout_copytoself_ex_blueprint.png)

In order to achieve the button alignment on the right, we use a `LinearLayout (horizontal)` with a ``Space`` and a ``Switch``. Adding more option switches adds just more of the same, so we are content with just one.

> Add a `LinearLayout (horizontal)` as the **fourth** child of the initial `LinearLayout`. Change the new layout's `id` attribute to `LinearLayoutOption`.
>
> Change the `layout_height` of `LinearLayoutOption` to `match_content`.
>
> Add a `Space` and a `Switch` (Palette: Buttons) as children of `LinearLayoutOption`.
> Choose sensible `id`s and fill the `text` attribute of the `Switch` to make sense.

At this point it should be easy for you to align the switch to the right side. If not, have a look at the layout code at the end of this section. We get to the resulting component tree

![Component tree](../../assets/img/003_ui/component_tree_options1.png)

and the layout

![Layout](../../assets/img/003_ui/layout_copytoself_fin.png)

[>Layout Code for this step<](../../assets/source/003_ui/05_activity_main.xml){:target="_blank"}

# Send Button

The last section to do is to include a "Send"-Button. This is the fastest section, as we only need to add a `Button` widget.

> Add a `Button` (Palette: Buttons) as the **fifth** child of the initial ``LinearLayout``.
>
> Set attributes `id` and `text` to appropriate values.

You will then get a layout like this:

![Layout](../../assets/img/003_ui/layout_send1.png)

There is one optional step that we can add to force the "Send"-Button to always be at the bottom of the screen.

>**Try to do that on your own**

If you did it correctly, you will get to the resulting layout

![Layout](../../assets/img/003_ui/layout_send2.png)

[>Solution and final layout code<](../../assets/source/003_ui/06_activity_main.xml){:target="_blank"}

>**Play around with this layout to gain a better understanding of the layout editor**. One starting point for your own adventures could be to change the `padding`, so that the widgets do not directly touch the edge of the screen. -->