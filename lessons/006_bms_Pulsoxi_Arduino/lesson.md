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
- 100 Hz Sampling Rate. Damit geht sich nach dem Sampling Theorem locker aus den Puls zu erfassen und für SpO2 ist die Samplingrate praktisch irrelevant.
- 20 Sekunden Aufzeichnung.

Ein Sample besteht dabei aus den Reflexionswerten der roten und infraroten LED. Diese sollen während der Aufzeichnung über die serielle Schnittstelle vom Arduino UNO ausgegeben werden. Die Aufzeichnung soll starten, sobald der User einen Startbefehl gegeben hat.

# Vorbereitung

Es kommt der MAX30101 zum Einsatz. Um die Werte des Sensors auszulesen, benötigt es die passende Library. Diese ist auf Sakai hinterlegt und kann in den Arduino Sketch auf zwei Arten eingebunden werden:
- Die Library wird in den *Libraries* Ordner in einem eigenen Ordner mit gleichen Namen der Library (daher *MAX30105*) kopiert oder 
- Die Library wird in denselben Ordner kopiert wie euer `.ino` File, auch bekannt als der *Sketch Folder*.

> Kleiner C++ Fact am Rande. Werden Libraries mit `"XY.h"` eingebunden, sucht die IDE als erstes im selben Folder wie der Code. Wird die Library mit `<XY.h>` eingebunde, sucht sie erst im Libraries Ordner im Installtionsverzeichnis, bzw. genauer gesagt in den Ordnern der *include path list*.

Denkt daran, dass Libraries in C++ immer aus `.h` und `.cpp` File bestehen. Leider ist die Dokumentationslage für die MAX3010x Sensorfamilie etwas undurchsichtig, für mich hat die *MAX30105* am besten funktioniert und ist auch mit dem MAX30101 und dem Arduino UNO kompatibel. Ihr könnt natürlich auch gerne eine andere Library suchen und ausprobieren.

> Den *Libraries* Ordner findet ihr im Installationsverzeichnis, je nachdem welche Version ihr verwendet und wo ihr sie installiert habt. Bei Version 2.0.4 z.B. hier: *C:\Users\YOURNAME\AppData\Local\Arduino15\libraries\MAX30105*. Der *AppData* Ordner muss manchmal erst eingeblendet werden.

Des weiteren, brauchen wir die `Wire.h`, da die Kommunikation mit dem Board via I2C läuft. Diese ist bereits teil des Arduino Cores und muss daher nicht extra installiert werden. Bindet beide Libraries in den Sketch via `#include` ein.

Timer Library brauchen wir ausnahmsweise keine, da die Samplerate direkt für die Sensor IC konfiguriert wird. Wir wollen aber eine Endzeit festlegen und brauchen Variablen für das Timing und ein Objekt für den Sensor. Am Ende sollten euere Deklarationen _vor_ dem `setup()` so aussehen:


````C++
#include <Wire.h>
#include "MAX30105.h"
MAX30105 pulseSensor;  //define object pulseSensor for reading data

#define END_TIME 20000   //ms limit run time to 20 s
long startTime; // use long to prevent overflow
````


# Implementierung

## Kommunikation und Sensorkonfiguration

Nachdem die Libraries eingebunden sind, sollte der Sketch nun startklar sein, um den Sensorcode zu implementieren. Im `setup()` des Arduino Sketches kümmern wir uns dazu erst einmal um die Konfiguration der Kommunikation und des Sensors. Schreibe dazu erst den Code, um die serielle Kommunikation zwischen Arduino und Rechner/Arduino IDE zu starten indem du `Serial.begin(115200)` aufrufst. Diese könnte auch dazu genutzt werden, um einen Startbefehl für die Aufzeichnung vom Serial Monitor / Plotter an den Arduino UNO zu senden. Baudrate sollte etwas höher gewählt werden (z.B. 115200), da wir einiges an Daten senden wollen.

Im nächsten Schritt wollen wir checken, ob der Sensor wirklich vorhanden ist. Die Bedingung dazu ist:

````C++
pulseSensor.begin(Wire, I2C_SPEED_FAST) == false
````

Ist diese erfüllt, ist am Standard I2C Port (der andere wäre `_SLOW`) der Sensor nicht auffindbar, sollte die Verkabelung etc. gecheckt werden und das Programm nicht weiter fortlaufen.

Wird der Sensor am I2C Port erkannt, können wir ihn für die Aufnahme der Sensordaten konfigurieren. Erstelle dazu ein Objekt für den Sensor von Typ `MAX30105`. Zur Konfiguration wird die Methode `pulseSensor.setup()` (des Sensors, nicht des Arduino Sketches!) mit den folgenden Übergabeparametern aufgerufen:
- `byte powerLevel`: Bestimmt, wie hell die LEDs leuchten. Mögliche Werte gehen von 0 bis 255 (ein Byte eben), wobei 0 ausgeschalten und 255 am hellsten wäre. Eine passende Wahl um zu starten wäre etwa die Hälfe mit 125 oder 0x7D als Hex-Nummer.
- `byte sampleAverage`: Bestimmt, ob der Sensor die Samples Mitteln soll. Wir wollen die Werte im Nachhinein verarbeiten. Wählen wir den Wert von 1 gibt er die Werte 1 zu 1 wie gelesen weiter. Andere Werte wären im Prinzip ein *moving Average* Filter. Zur Verfügung stehen die Werte **1**, 2, 4, 8, 16 und 32.
- `byte ledMode`: Damit können die einzelnen LEDs an- bzw. ausgeschalten werden. So könnte die grüne LED alleine gewählt werden, um nur den Puls zu erfassen. Wir wollen die Rote und infrarote LED lesen und daher den Modus 2.
- `int sampleRate`: Wie oben angegeben, wollen wir 100 Hz Samplerate. Nebenbei sei erwähnt, dass nur bestimmte Werte zur Verfügung stehen (50, **100**, 200, 400, 800, 1000, 1600, 3200).
- `int pulseWidth`: Da der Sensor nur über einen Fototransistor verfügt, aber drei Wellenlängen erkannt werden müssen, wird jeder LED ein Time Slot zugeteilt. Dazu kann die Pulsweite, also die *On-Time* für die LEDs gewählt werden. Das mittlere Setting wäre mit 215 passend, verfügbar sind 69, 118, **215** und 411.
- `int adcRange`: Der ADC welcher das Signal des Fototransistors digitalisiert, kann in dessen Auflösung konfiguriert werden. Das mittlere Setting wäre mit 13 Bit passend, verfügbar sind 2048, 4096, **8192** und 16384.

> Ein Objekt in C++ wird normalerweise in der Sektion unter den Präprozessoranweisungen (`#`) bei den Variablen erstellt. Die Syntax ist folgendermaßen: `Typ nameDesObjektes;`. Der Typ ist dabei von der Library vorgegeben. Mit `nameDesObjektes.methode();` kann dann auf Methoden des Objektes zugegriffen werden.

Damit kannst du die Methode mit den gewählten Parametern aufrufen und den Sensor konfigurieren.

## Timing

Um die Sequenz mit einer bestimmten Länge aufzunehmen, können wir checken ob die 20 Sekunden abgelaufen sind. Ein Standardmuster in der Controller Welt ist die Zeit mit `millis()` zu messen. Diese Funktion gibt den Wert der Controller Clock in Millisekunden aus. Da der Wert vom Zeitpunkt des Einschaltens abhängig ist, muss daher immer in Differenzen gedacht werden So wird eine Zeit gemessen indem:

````C++
startTime = millis();
 // Something else is happening that takes some time. Imagine a Granny walking over a crosswalk //
endTime = millis() - startTime;
````

Wenn nun eine Sequenz von 20 Sekunden aufgenommen werden soll, wollen wir die Startzeit im letzten Schritt vor dem `loop()` nehmen und innerhalb des `loop()` mit den 20 Sekunden vergleichen. Ist die Zeit abgelaufen, stoppen wir den Sensor mit `mySensorName.shutDown();`.

> Es sei erwähnt, dass `millis()` nach etwa 50 Tagen *überläuft*, daher der Wert wieder von vorne zu zählen beginnt.

## Sensor Samples Auslesen

Im `loop()` wollen wir nun die Samples auslesen, die der Sensor aufgenommen hat. Hier ist ein entscheidender Unterschied zur Lesson mit der MPU6050. Da der Sensor einen FIFO-Buffer integriert hat, müssen wir eigentlich keinen dezidierten Timer verwenden. Die Werte werden vom Sensor in der konfigurierten Samplerate aufgenommen und können, wann es uns passt vom Buffer ausgelesen werden. Dies läuft in drei Schritten ab:
````C++
 mySensorName.check(); // Reads the Buffer, up to 3 samples
 mySensorName.getFIFORed(); // Gets the Red Values from the Buffer
 mySensorName.getFIFOIR(); // Gets the IR Values from the Buffer
 mySensorName.nextSample(); // When finished reading, move to the next Sample
````

Außerdem kann `mySensorName.available()` verwendet werden, um zu checken, ob sich nach dem Lesen des Buffers tatsächlich **neue** Daten darin befinden. Im Prinzip sollte der, nachdem der Buffer gelesen wurde, eine Schleife gestartet werde, die überprüft, ob (noch) neue Daten verfügbar sind. Solang dies der Fall ist, werden die roten und infraroten Werte über die serielle Kommunikation ausgegeben und Sample für Sample abgearbeitet.

Damit sollte der Code für das Labor soweit fertig sein.

# Abgabe

Da der Code erst im Labor getestet wird, könnt ihr nur die Syntax überprüfen, indem ihr den Code für einen Arduino UNO kompiliert (*Verify*). Checkt bitte trotzdem so gut es geht, ob der Code denn richtig laufen würde. Hake die Aufgabe unten ab, wir werden sie dann in der Vorlesung durchbesprechen. Es muss kein Code abgegeben werden.


