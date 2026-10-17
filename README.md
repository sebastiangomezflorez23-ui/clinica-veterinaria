# Hospital Veterinario – Evaluación POO en Java

Sistema de consola para gestionar los pacientes de una clínica veterinaria.
Java 17+, sin librerías externas, sin archivos ni bases de datos. Todos los datos viven en vectores de capacidad fija (`Paciente[10]` y `Jaula[6]`).

> Nota: este proyecto se desarrolló con asistencia de IA (Claude). Su uso se rige por las condiciones fijadas por el docente.

## Cómo ejecutarlo

**En IntelliJ IDEA:** `File → Open` sobre esta carpeta, ejecutar la clase `Main` y escribir los comandos en la consola de *Run*.

**Desde terminal:**

```bash
cd src
javac *.java
java Main
```

## Comandos

| Comando | Ejemplo |
|---|---|
| `ingresar perro <nombre> <peso> <edad> <raza> <si/no>` | `ingresar perro Toby 12.5 3 beagle no` |
| `ingresar gato <nombre> <peso> <edad>` | `ingresar gato Luna 4 2` |
| `ingresar ave <nombre> <peso> <edad> <envergadura>` | `ingresar ave Pio 1 2 40` |
| `vacunar <nombre>` | `vacunar Toby` |
| `asignar <nombre>` | `asignar Rocky` |
| `medicar <nombre> <mg>` | `medicar Kiwi 2.4` |
| `alta <nombre>` | `alta Kiwi` |
| `reporte` | `reporte` |
| `salir` | termina el programa |

## Estructura del proyecto

```
HospitalVeterinario/
├── src/
│   ├── Paciente.java     (clase base abstracta)
│   ├── Perro.java        (hereda de Paciente, factorDosis = 10)
│   ├── Gato.java         (hereda de Paciente, factorDosis = 6)
│   ├── AveExotica.java   (hereda de Paciente, factorDosis = 3)
│   ├── Jaula.java
│   ├── Hospital.java     (toda la lógica de gestión)
│   └── Main.java         (único con Scanner y System.out)
├── HospitalVeterinario.iml
└── README.md
```

## Cómo se hizo el taller

### 1. Lectura del enunciado
Primero se leyó el enunciado completo y se extrajeron tres listas: las clases con sus atributos, las reglas de negocio R1–R10 y la tabla de comandos con sus mensajes exactos. Lo más importante fue el **orden de las validaciones** de cada comando, porque la primera que falla es la que produce el mensaje.

### 2. Diseño de las clases
- **`Paciente`** es abstracta y concentra todo lo común: nombre, peso, edad, estado, `factorDosis`, `dosisAcumulada` y `jaula`. Así no hay código duplicado en las subclases.
- **`Perro`, `Gato` y `AveExotica`** solo agregan su atributo propio y llaman a `super(nombre, peso, edad, factorDosis)` pasando su factor de dosis (10, 6 y 3).
- **`Jaula`** guarda número, tamaño, zona y ocupante. Está libre cuando `ocupante == null`.
- **`Hospital`** tiene el vector `Paciente[10]` con su contador y el vector `Jaula[6]`. Perros, gatos y aves conviven en el mismo `Paciente[]`.
- **`Main`** es la única clase que lee y escribe en consola.

### 3. Polimorfismo para evitar condicionales por tipo
Las diferencias entre tipos se resolvieron con métodos que las subclases redefinen, en lugar de `if (tipo == ...)` en el `Hospital`:

| Método en `Paciente` | Qué redefine cada subclase |
|---|---|
| `getPesoMaximo()` | 80 (perro), 15 (gato), 15 (ave) |
| `getZonaRequerida()` | `A` para perro y gato, `B` para ave |
| `getTamanoRequerido()` | `AveExotica`: con envergadura > 60 cm necesita mínimo `M` |
| `tieneEstresAlto()` | `Gato`: estrés ≥ 8 |
| `registrarMedicacion(mg)` | `Gato`: además sube el estrés 2 puntos (tope 10) |

### 4. Encapsulamiento y validación
Todos los atributos son `private`. Los setters validan y devuelven `boolean`: si el valor es inválido lo rechazan y **no modifican** el atributo. Los constructores usan esos mismos setters y lanzan `IllegalArgumentException` si algo es inválido; el `Hospital` captura esa excepción y responde `ERROR: datos invalidos` (R1). De esta forma la validación existe en un solo lugar.

### 5. Vectores
- **Contador:** `contador` indica cuántas posiciones están ocupadas.
- **Búsqueda:** recorrido de `0` a `contador - 1` comparando nombres con `equalsIgnoreCase`.
- **Límite:** no se ingresa si `contador == 10`.
- **Compactación en `alta` (R10):** los pacientes siguientes se desplazan una posición hacia la izquierda, la última posición queda en `null` y el contador baja en uno.
- **Asignar (R6):** recorre `Jaula[]` y toma la primera jaula libre que cumpla tamaño y zona; luego enlaza `jaula.setOcupante(paciente)` y `paciente.setJaula(jaula)`.

### 6. Reglas de negocio
Cada comando del `Hospital` valida en el orden exacto del enunciado:

- **ingresar:** datos → duplicado → lleno.
- **asignar:** no encontrado → cuarentena → ya tiene jaula → sin jaula compatible.
- **medicar:** no encontrado → mg ≤ 0 → cuarentena → sin jaula → gato con estrés alto → dosis > máxima → acumulado superado.
- **alta:** no encontrado → no apto (solo procede si el estado es `EN_TRATAMIENTO`).

Fórmulas: dosis máxima = `peso × factorDosis`; tope acumulado = `3 × dosis máxima`.

### 7. Separación dominio / consola
Las clases del dominio y `Hospital` no usan `Scanner` ni `System.out`: los métodos del `Hospital` **devuelven el mensaje como `String`** y `Main` lo imprime. `Main` también convierte el texto a números; si no es un número válido lo pasa como `NaN` (decimales) o `-1` (enteros) y el dominio lo rechaza como dato inválido.

### 8. Datos iniciales
El método `cargarDatosIniciales()` crea las 6 jaulas (1 A-S, 2 A-M, 3 A-M, 4 A-L, 5 B-S, 6 B-M) y los pacientes Rocky (perro), Misifu (gato) y Kiwi (ave), todos sin jaula.

### 9. Pruebas
Se compiló con `javac *.java` y se ejecutó una sesión de comandos para comprobar cada mensaje y cada regla:

- Perro no vacunado entra en `CUARENTENA`; no se puede asignar ni medicar hasta vacunarlo.
- Nombre duplicado sin distinguir mayúsculas.
- Asignación a la primera jaula compatible (Rocky → 4, Misifu → 1, Kiwi → 6).
- Dosis mayor a la máxima y tope acumulado superado.
- Gato bloqueado por estrés alto después de varias medicaciones.
- `alta` rechazada si no está en tratamiento; aceptada, libera la jaula y compacta el vector.
- Comando desconocido, datos inválidos y `reporte` con un decimal en el peso.

### 10. Control de versiones
El repositorio tiene al menos dos commits:
1. Núcleo del dominio (`Paciente`, `Perro`, `Gato`, `AveExotica`, `Jaula`).
2. `Hospital`, menú de consola y reglas de negocio completas.

## Decisiones de interpretación

- La jaula debe tener **exactamente** el tamaño requerido (S, M o L).
- En el reporte el tipo se escribe `Perro`, `Gato` o `Ave`.
- Los nombres no llevan espacios porque los comandos se separan por espacios.
- `ingresar` y `medicar` con la cantidad de argumentos incorrecta responden `ERROR: datos invalidos`; los demás comandos mal formados responden `ERROR: comando desconocido`.
- La salida no imprime ningún mensaje adicional (ni prompts ni bienvenida), para que los mensajes sean idénticos a los del enunciado.
