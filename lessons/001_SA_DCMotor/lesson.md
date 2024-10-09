# Overview
{: .reading}

* This will become a table of contents (this text will be scrapped).
{:toc}

# Modellierung der Übertragungsfunktion eines DC-Motors

## Einleitung

Ein Gleichstrommotor (DC-Motor) kann durch eine Differentialgleichung beschrieben werden, die die mechanischen und elektrischen Eigenschaften des Motors berücksichtigt. In dieser Anleitung werden wir die Übertragungsfunktion eines DC-Motors herleiten, die Reibung und Trägheit berücksichtigt.

## 1. Physikalisches Modell

Die Dynamik eines DC-Motors lässt sich durch zwei grundlegende Gleichungen beschreiben:
- Die elektrische Gleichung, die das Verhalten des Ankers beschreibt.
- Die mechanische Gleichung, die das Drehmoment und die Rotationsbewegung beschreibt.

### 1.1 Elektrische Gleichung

Die elektrische Gleichung des DC-Motors lautet:

$$
V(t) = L \frac{dI(t)}{dt} + R I(t) + K_e \omega(t)
$$

wobei:
- \( V(t) \): angelegte Spannung [V]
- \( L \): Induktivität der Ankerspule [H]
- \( R \): Widerstand der Ankerspule [Ω]
- \( I(t) \): Ankerstrom [A]
- \( K_e \): EMK-Konstante (elektromotorische Kraft) [V/(rad/s)]
- \( \omega(t) \): Winkelgeschwindigkeit des Motors [rad/s]

### 1.2 Mechanische Gleichung

Die mechanische Gleichung des DC-Motors lautet:

\[
J \frac{d\omega(t)}{dt} + B \omega(t) = K_m I(t) - T_L
\]

wobei:
- \( J \): Trägheitsmoment des Motors [kg·m²]
- \( B \): Viskoser Reibungskoeffizient [N·m·s/rad]
- \( K_m \): Motorkonstante [N·m/A]
- \( T_L \): Lastmoment [N·m]

## 2. Übertragungsfunktion herleiten

Ziel ist es, eine Übertragungsfunktion der Form \( \frac{\Omega(s)}{V(s)} \) zu finden, wobei \( \Omega(s) \) die Laplace-Transformierte der Winkelgeschwindigkeit und \( V(s) \) die Laplace-Transformierte der angelegten Spannung ist.

### 2.1 Laplace-Transformation

Durch Anwenden der Laplace-Transformation auf die Differentialgleichungen (mit Anfangsbedingungen gleich null) erhalten wir:

1. Elektrische Gleichung:
   \[
   V(s) = (L s + R) I(s) + K_e \Omega(s)
   \]

2. Mechanische Gleichung:
   \[
   J s \Omega(s) + B \Omega(s) = K_m I(s) - T_L(s)
   \]

### 2.2 Ankerstrom \( I(s) \) eliminieren

Um die Übertragungsfunktion zu finden, eliminieren wir \( I(s) \) zwischen den beiden Gleichungen:

Aus der elektrischen Gleichung:
\[
I(s) = \frac{V(s) - K_e \Omega(s)}{L s + R}
\]

Einsetzen in die mechanische Gleichung:
\[
J s \Omega(s) + B \Omega(s) = K_m \left( \frac{V(s) - K_e \Omega(s)}{L s + R} \right)
\]

### 2.3 Übertragungsfunktion formen

Umstellen der Gleichung ergibt:
\[
(J s + B) \Omega(s) (L s + R) = K_m V(s) - K_m K_e \Omega(s)
\]

Sortieren nach \( \Omega(s) \) ergibt:
\[
\Omega(s) \left[ (J s + B)(L s + R) + K_m K_e \right] = K_m V(s)
\]

Die Übertragungsfunktion \( \frac{\Omega(s)}{V(s)} \) ergibt sich zu:
\[
\frac{\Omega(s)}{V(s)} = \frac{K_m}{(J s + B)(L s + R) + K_m K_e}
\]

### 2.4 Vereinfachte Form

Häufig werden \( L \) und \( R \) als sehr kleine oder sehr große Werte angenommen, um die Übertragungsfunktion zu vereinfachen. Falls \( L \approx 0 \):
\[
\frac{\Omega(s)}{V(s)} = \frac{K_m}{J R s^2 + (B R + K_m K_e) s}
\]

## 3. Schlussfolgerung

Die resultierende Übertragungsfunktion beschreibt das Verhalten eines DC-Motors unter Berücksichtigung von Reibung und Trägheit. Sie kann für die Analyse des Frequenzverhaltens des Motors oder die Auslegung eines Reglers verwendet werden.

## 4. Beispielparameter

Hier sind typische Parameter, die zur Simulation eines DC-Motors verwendet werden könnten:

| Parameter | Wert | Einheit | Beschreibung |
|-----------|------|---------|--------------|
| \( R \)   | 1.2  | Ω       | Widerstand der Ankerspule |
| \( L \)   | 0.01 | H       | Induktivität der Ankerspule |
| \( K_e \) | 0.01 | V/(rad/s) | EMK-Konstante |
| \( K_m \) | 0.01 | N·m/A   | Motorkonstante |
| \( J \)   | 0.01 | kg·m²   | Trägheitsmoment |
| \( B \)   | 0.1  | N·m·s/rad | Viskoser Reibungskoeffizient |
