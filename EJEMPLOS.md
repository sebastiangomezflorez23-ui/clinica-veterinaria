# Ejemplos de uso del programa

Todos los resultados de este archivo salen de ejecutar el programa real. Cada sesión empieza desde cero, con los datos iniciales (Rocky, Misifu y Kiwi, todos sin jaula).

En los ejemplos, las líneas que empiezan con `>` son lo que **tú escribes**; las demás son lo que **responde el programa**. No escribas el símbolo `>`.

## 1. Cómo ejecutarlo

**IntelliJ IDEA:** abre la carpeta del proyecto, ejecuta la clase `Main` y escribe los comandos en el panel *Run*, uno por línea, con Enter al final.

**Terminal:**

```bash
cd src
javac *.java
java Main
```

El programa no muestra ningún prompt: la consola queda en blanco esperando tu comando. Para terminar escribe `salir`.

### Ejecutar con un archivo de comandos

En lugar de escribir a mano, guarda los comandos en un archivo `comandos.txt` (uno por línea, terminando con `salir`) y redirige la entrada:

```bash
java Main < comandos.txt
```

En IntelliJ: *Run → Edit Configurations → Modify options → Redirect input from* y elige el archivo.

## 2. Resumen de comandos

| Comando | Ejemplo |
|---|---|
| `ingresar perro <nombre> <peso> <edad> <raza> <si/no>` | `ingresar perro Luna 20 5 criollo si` |
| `ingresar gato <nombre> <peso> <edad>` | `ingresar gato Tom 5 2` |
| `ingresar ave <nombre> <peso> <edad> <envergadura>` | `ingresar ave Pio 1 2 40` |
| `vacunar <nombre>` | `vacunar Max` |
| `asignar <nombre>` | `asignar Rocky` |
| `medicar <nombre> <mg>` | `medicar Rocky 150` |
| `alta <nombre>` | `alta Rocky` |
| `reporte` | `reporte` |
| `salir` | termina el programa |

## 3. Ver el estado inicial

```
> reporte
--- REPORTE ---
Pacientes: 3/10
Rocky | Perro | 30.0 kg | INGRESADO | jaula: -
Misifu | Gato | 4.0 kg | INGRESADO | jaula: -
Kiwi | Ave | 0.8 kg | INGRESADO | jaula: -
Jaulas libres: 6
```

## 4. Ingresar pacientes

Un perro vacunado, un gato, un ave y un perro **no vacunado**, que entra en cuarentena:

```
> ingresar perro Luna 20 5 criollo si
OK: Luna ingresado
> ingresar gato Tom 5 2
OK: Tom ingresado
> ingresar ave Pio 1 2 40
OK: Pio ingresado
> ingresar perro Max 8 1 pug no
OK: Max ingresado en CUARENTENA
> reporte
--- REPORTE ---
Pacientes: 7/10
Rocky | Perro | 30.0 kg | INGRESADO | jaula: -
Misifu | Gato | 4.0 kg | INGRESADO | jaula: -
Kiwi | Ave | 0.8 kg | INGRESADO | jaula: -
Luna | Perro | 20.0 kg | INGRESADO | jaula: -
Tom | Gato | 5.0 kg | INGRESADO | jaula: -
Pio | Ave | 1.0 kg | INGRESADO | jaula: -
Max | Perro | 8.0 kg | CUARENTENA | jaula: -
Jaulas libres: 6
```

## 5. Flujo completo: cuarentena, vacunar, asignar y alta

Esta sesión ingresa a Luna, Tom, Pio y Max, y luego los asigna a jaulas:

```
> ingresar perro Luna 20 5 criollo si
OK: Luna ingresado
> ingresar gato Tom 5 2
OK: Tom ingresado
> ingresar ave Pio 1 2 40
OK: Pio ingresado
> ingresar perro Max 8 1 pug no
OK: Max ingresado en CUARENTENA
> asignar Luna
OK: Luna asignado a jaula 2
> asignar Tom
OK: Tom asignado a jaula 3
> asignar Pio
OK: Pio asignado a jaula 5
> asignar Max
ERROR: paciente en cuarentena
> vacunar Max
OK: Max vacunado (pasa a INGRESADO)
> asignar Max
ERROR: no hay jaula compatible
```

Qué pasó:

- Luna pesa 20 kg, necesita jaula `M` en zona `A`, y la primera libre es la 2.
- Tom pesa 5 kg, que cuenta como `M` (de 5 a 25 kg), y la primera libre es la 3.
- Pio es un ave, así que va a zona `B`; pesa 1 kg, necesita `S` y la primera libre es la 5.
- Max estaba en cuarentena: no se puede asignar hasta vacunarlo.
- Después de vacunarlo, a Max (8 kg, `M`, zona `A`) ya no le queda jaula: las dos jaulas `A-M` están ocupadas.

Ahora se medica a Luna y se le da el alta:

```
> medicar Luna 100
OK: Luna medicado con 100 mg
> alta Luna
OK: Luna dado de alta
> alta Luna
ERROR: paciente no encontrado
> reporte
--- REPORTE ---
Pacientes: 6/10
Rocky | Perro | 30.0 kg | INGRESADO | jaula: -
Misifu | Gato | 4.0 kg | INGRESADO | jaula: -
Kiwi | Ave | 0.8 kg | INGRESADO | jaula: -
Tom | Gato | 5.0 kg | INGRESADO | jaula: 3
Pio | Ave | 1.0 kg | INGRESADO | jaula: 5
Max | Perro | 8.0 kg | INGRESADO | jaula: -
Jaulas libres: 4
```

Fíjate en el reporte: Luna desapareció, los pacientes siguientes se corrieron una posición (el vector se compactó) y la jaula 2 quedó libre.

## 6. Dosis y tope acumulado

Misifu pesa 4 kg y es gato (factor 6), así que su dosis máxima por aplicación es 4 × 6 = **24 mg** y su tope acumulado es 3 × 24 = **72 mg**.

```
> asignar Misifu
OK: Misifu asignado a jaula 1
> medicar Misifu 24
OK: Misifu medicado con 24 mg
> medicar Misifu 25
ERROR: dosis excede el maximo
> medicar Misifu 24
OK: Misifu medicado con 24 mg
> medicar Misifu 24
OK: Misifu medicado con 24 mg
> medicar Misifu 1
ERROR: tope acumulado superado
> reporte
--- REPORTE ---
Pacientes: 3/10
Rocky | Perro | 30.0 kg | INGRESADO | jaula: -
Misifu | Gato | 4.0 kg | EN_TRATAMIENTO | jaula: 1
Kiwi | Ave | 0.8 kg | INGRESADO | jaula: -
Jaulas libres: 5
```

Después de la primera medicación válida, Misifu pasa a `EN_TRATAMIENTO`. Con 72 mg acumulados ya no se le puede dar ni 1 mg más.

## 7. Estrés alto en un gato

Cada medicación sube el estrés del gato 2 puntos. Con estrés de 8 o más ya no se le puede medicar, aunque la dosis sea pequeña:

```
> asignar Misifu
OK: Misifu asignado a jaula 1
> medicar Misifu 1
OK: Misifu medicado con 1 mg
> medicar Misifu 1
OK: Misifu medicado con 1 mg
> medicar Misifu 1
OK: Misifu medicado con 1 mg
> medicar Misifu 1
OK: Misifu medicado con 1 mg
> medicar Misifu 1
ERROR: gato con estres alto
> alta Misifu
OK: Misifu dado de alta
```

Tras 4 medicaciones el estrés llegó a 8. Con estrés alto no se puede medicar, pero el alta sí se permite porque Misifu está `EN_TRATAMIENTO`.

## 8. Errores comunes

```
> ingresar perro rocky 10 2 pug si
ERROR: nombre duplicado
> ingresar ave Zazu 1 2 0
ERROR: datos invalidos
> ingresar ave Zazu 25 2 30
ERROR: datos invalidos
> ingresar perro Z 90 2 pug si
ERROR: datos invalidos
> ingresar perro Z 10 31 pug si
ERROR: datos invalidos
> ingresar perro Z 10 2 pug quiza
ERROR: datos invalidos
> medicar Rocky 0
ERROR: datos invalidos
> medicar Rocky -5
ERROR: datos invalidos
> medicar Rocky abc
ERROR: datos invalidos
> alta Rocky
ERROR: paciente no apto para alta
> alta Fantasma
ERROR: paciente no encontrado
> volar
ERROR: comando desconocido
```

Motivos, en orden:

| Comando | Por qué falla |
|---|---|
| `ingresar perro rocky ...` | Ya existe "Rocky": el nombre es único sin distinguir mayúsculas |
| `ingresar ave Zazu 1 2 0` | La envergadura debe ser mayor que 0 |
| `ingresar ave Zazu 25 2 30` | Un ave no puede pesar más de 15 kg |
| `ingresar perro Z 90 ...` | Un perro no puede pesar más de 80 kg |
| `ingresar perro Z 10 31 ...` | La edad debe estar entre 0 y 30 |
| `... pug quiza` | El último dato debe ser `si` o `no` |
| `medicar Rocky 0 / -5 / abc` | Los mg deben ser un número mayor que 0 |
| `alta Rocky` | Solo se da de alta a pacientes `EN_TRATAMIENTO` |

## 9. Reglas de las jaulas

| Jaula | Tamaño | Zona |
|---|---|---|
| 1 | S | A |
| 2 | M | A |
| 3 | M | A |
| 4 | L | A |
| 5 | S | B |
| 6 | M | B |

- Tamaño según el peso: menos de 5 kg = `S`, de 5 a 25 kg = `M`, más de 25 kg = `L`.
- Un ave con envergadura mayor a 60 cm necesita como mínimo `M` (por eso Kiwi, de 0.8 kg y 70 cm, va a la jaula 6).
- Perros y gatos solo van a la zona `A`; las aves, a la zona `B`.
- `asignar` elige siempre la primera jaula libre que cumpla tamaño y zona.

## 10. Qué no se puede hacer

- Usar nombres con espacios (`ingresar gato Mi Gato 4 2`): el programa los lee como palabras separadas y da `ERROR: datos invalidos`.
- Usar coma decimal (`12,5`): los decimales se escriben con punto.
- Tener más de 10 pacientes a la vez: el siguiente ingreso da `ERROR: hospital lleno`.
- Guardar los datos: al cerrar el programa todo se pierde y vuelve a empezar con Rocky, Misifu y Kiwi.
- Pasar comandos como argumentos (`java Main reporte`): solo se leen por teclado o con redirección de un archivo.

Se pueden escribir los comandos en mayúsculas o minúsculas (`INGRESAR GATO Tom 5 2` funciona), con varios espacios entre palabras, y las líneas en blanco se ignoran.
