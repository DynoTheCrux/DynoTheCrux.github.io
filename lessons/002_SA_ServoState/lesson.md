
# Overview
{: .reading}

* This will become a table of contents (this text will be scrapped).
{:toc}

# Ansteuerung eines Servo Motors

## Einleitung

In diesem Projekt steuern wir einen Servo-Motor mit einem Arduino. Der Motor wird über zwei Buttons gesteuert: einer zum Starten und einer zum Stoppen der Bewegung. Wir werden mit der simpelsten Variante starten welche gewisse schwächen aufweist und versuchen den Code stetik zu verbessern.


## 1. Erstelle ein Wokwi Projekt

Erstelle ein Projekt auf Wokwi mit den folgenden Komponenten:
- Arduino (z.B. Arduino Uno)
- Servo-Motor
- 2 Taster (Buttons)

Die Verkabelung ist folgendermaßen:
1. **Servo-Motor**:  
   - Rotes Kabel → 5V (Vcc)  
   - Schwarzes/Braunes Kabel → GND  
   - Oranges/Weißes Kabel → PWM fähiger Arduino Pin (z.B. Pin 3)  

2. **Taster**:  
   - **Start-Taster**:  
     - Ein Pin des Tasters → Arduino Pin (z.B. Pin 4)   
     - Anderer Pin → GND  
   - **Stop-Taster**:  
     - Ein Pin des Tasters → Arduino Pin (z.B. Pin 2)   
     - Anderer Pin → GND 


## Erste Version

Die erste Version ist mehr oder weniger 1:1 die Version die uns auch ChatGPT und die meisten Online-Tutorials ausspucken würden. Ihr könnt diese natürlich von dort beziehen oder selbst schreiben:
- Binde die ``#include <Servo.h>`` in den Code ein.
- Definiere die Pins des Servos sowie der Buttons gleich wie verkabelt, z.B.: ``#define SERVO_PIN 3``.
- Im ``void setup()`` brauchen wir folgenden code:

  

Auf Basis des obigen Bildes finden wir die elektrische Gleichung des DC Motors. Wir bilden eine Schleife im Uhrzeigersinn. Wobei der Spannungsabfall der Induktivität von der zeitlichen Veränderung des Stromes abhängig ist und der Spannungsabfall über den Wiederstand vom absoluten Wert des Stromes. Die elektrische Gleichung des DC-Motors lautet:

$$
V(t) = L \frac{dI(t)}{dt} + R I(t) + e(t)
$$

wobei:
- $$V(t)$$: angelegte Spannung [V]
- $$L$$: Induktivität der Ankerspule [H]
- $$R$$: Widerstand der Ankerspule [Ω]
- $$I(t)$$: Ankerstrom [A]
- $$e(t) = K_e \omega(t)$$: Gegen-EMK (elektromotorische Kraft) [V]
- $$K_e$$: EMK-Konstante [V/(rad/s)]
- $$\omega(t)$$: Winkelgeschwindigkeit des Motors [rad/s]

### 1.2 Mechanische Gleichung

Für die mechanische Gleichung betrachten wir die rechte Seite des Bildes. Wir bilden ein Momentengleichgewicht an der Achse des Motors. Wir berücksichtigen die Trägheit des Rotors, die Reibung in den Lagern und das vom Motor erzeugte Ausgangsmoment. Die mechanische Gleichung des DC-Motors lautet dann:

$$
J \frac{d\omega(t)}{dt} + B \omega(t) = K_m I(t)
$$


wobei:
- $$J$$: Trägheitsmoment des Motors [kg·m²]
- $$B$$: Viskoser Reibungskoeffizient [N·m·s/rad]
- $$K_m$$: Motorkonstante [N·m/A]

### Annahmen

- Der Motor ist unbelastet, daher ist das Lastmoment $$T_L \approx 0$$.
- Es wird ein linearisiertes Modell betrachtet.

## 2. Übertragungsfunktion herleiten

Ziel ist es, eine Übertragungsfunktion der Form $$\frac{\Omega(s)}{V(s)}$$ zu finden, wobei $$\Omega(s)$$ die Laplace-Transformierte der Winkelgeschwindigkeit und $$V(s)$$ die Laplace-Transformierte der angelegten Spannung ist. Daher versuchen wir die mechanische und die elektrische Gleichung zu kombinieren.

### 2.1 Laplace-Transformation

Durch Anwenden der Laplace-Transformation auf die Differentialgleichungen (mit Anfangsbedingungen gleich null) erhalten wir:

1. Elektrische Gleichung: Es wird $$t$$ durch $$s$$ ersetzt und $$\frac{dI(t)}{dt}$$ wird zu $$sI(s)$$.
   
$$
V(s) = (L s + R) I(s) + K_e \Omega(s)
$$

3. Mechanische Gleichung: **Finde die Laplace Transformation der Mechanischen Gleichung und beantworte Frage 1.**
<!--
$$
J s \Omega(s) + B \Omega(s) = K_m I(s)
$$
-->
### 2.2 Ankerstrom $$I(s)$$ eliminieren

Um die Übertragungsfunktion zu finden, eliminieren wir $$I(s)$$ zwischen den beiden Gleichungen:

Aus der elektrischen Gleichung:

$$
I(s) = \frac{V(s) - K_e \Omega(s)}{L s + R}
$$

Durch einsetzen in die mechanische Gleichung.
<!--
$$
J s \Omega(s) + B \Omega(s) = K_m \left( \frac{V(s) - K_e \Omega(s)}{L s + R} \right)
$$
-->
### 2.3 Übertragungsfunktion formen

**Finde die Übertragungsfunktion $$T = \frac{\Omega(s)}{V(s)}$$ und beantworte Frage 2.**
<!--
Umstellen der Gleichung ergibt:

$$
(J s + B) \Omega(s) (L s + R) = K_m V(s) - K_m K_e \Omega(s)
$$

Sortieren nach $$\Omega(s)$$ ergibt:

$$
\Omega(s) \left[ (J s + B)(L s + R) + K_m K_e \right] = K_m V(s)
$$

Die Übertragungsfunktion $$\frac{\Omega(s)}{V(s)}$$ ergibt sich zu:

$$
\frac{\Omega(s)}{V(s)} = \frac{K_m}{(J s + B)(L s + R) + K_m K_e}
$$
-->
<!--
### 2.4 Vereinfachte Form

Wenn die Induktivität $$L$$ im Vergleich zu $$R$$ sehr klein ist ($$L \approx 0$$), kann die Gleichung vereinfacht werden:

$$
\frac{\Omega(s)}{V(s)} = \frac{K_m}{J R s^2 + (B R + K_m K_e) s}
$$
-->
Das vollständige System zeigt den Zusammenhang zwischen der Eingangsspannung $$V(s)$$ und der Ausgangsdrehzahl $$\Omega(s)$$.

## 5. Simulationsparameter

Hier sind typische Parameter, die zur Simulation eines DC-Motors verwendet werden könnten:

| Parameter | Wert | Einheit | Beschreibung |
|-----------|------|---------|--------------|
| $$R$$   | 1.2  | Ω       | Widerstand der Ankerspule |
| $$L$$   | 0.01 | H       | Induktivität der Ankerspule |
| $$K_e$$ | 0.01 | V/(rad/s) | EMK-Konstante |
| $$K_m$$ | 0.01 | N·m/A   | Motorkonstante |
| $$J$$   | 0.01 | kg·m²   | Trägheitsmoment |
| $$B$$   | 0.1  | N·m·s/rad | Viskoser Reibungskoeffizient |

###  Stationäres System
Stationär bedeutet in diesem Zusammenhang, dass sich weder die Eingangsspannung noch das Lastmoment ändert. Das Heißt der Wert von $$s = 0$$, da dieser mit der Frequenz und der Dämpfung zusammenhängt. Plotte **Eingangsspannung (X-Achse) zu Drehzahl für die Übertragungsfunktion bei $$s = 0$$ und beantworte Frage 3.**

### Dynamisches System
Dynamische können wir das System z.B. mithilfe einer Step Response Analysieren. **Plotte die Step Response des Systems und beantworte die Frage 4.**


## 6. Schlussfolgerung

Die resultierende Übertragungsfunktion beschreibt das Verhalten eines DC-Motors unter Berücksichtigung von Reibung und Trägheit. Sie kann für die Analyse des Frequenzverhaltens des Motors oder die Auslegung eines Reglers verwendet werden.
