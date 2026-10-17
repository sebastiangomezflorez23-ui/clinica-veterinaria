import java.util.Locale;

/**
 * Contiene toda la lógica de gestión. Devuelve mensajes (String);
 * la impresión la hace únicamente Main.
 */
public class Hospital {

    public static final String ERROR_DATOS = "ERROR: datos invalidos";
    private static final String ERROR_NO_ENCONTRADO = "ERROR: paciente no encontrado";
    private static final String ERROR_CUARENTENA = "ERROR: paciente en cuarentena";

    private static final int MAX_PACIENTES = 10;
    private static final int NUM_JAULAS = 6;

    private final Paciente[] pacientes = new Paciente[MAX_PACIENTES];
    private int contador;
    private final Jaula[] jaulas = new Jaula[NUM_JAULAS];

    public Hospital() {
        cargarDatosIniciales();
    }

    /** Carga las jaulas y los tres pacientes iniciales (todos sin jaula asignada). */
    public final void cargarDatosIniciales() {
        jaulas[0] = new Jaula(1, 'S', 'A');
        jaulas[1] = new Jaula(2, 'M', 'A');
        jaulas[2] = new Jaula(3, 'M', 'A');
        jaulas[3] = new Jaula(4, 'L', 'A');
        jaulas[4] = new Jaula(5, 'S', 'B');
        jaulas[5] = new Jaula(6, 'M', 'B');

        contador = 0;
        pacientes[contador++] = new Perro("Rocky", 30.0, 4, "labrador", true);
        pacientes[contador++] = new Gato("Misifu", 4.0, 3, 0);
        pacientes[contador++] = new AveExotica("Kiwi", 0.8, 2, 70);
    }

    // ---------- Ingreso (R1, R2, R3) ----------

    public String ingresarPerro(String nombre, double peso, int edad, String raza, boolean vacunado) {
        Paciente nuevo;
        try {
            nuevo = new Perro(nombre, peso, edad, raza, vacunado);
        } catch (IllegalArgumentException e) {
            return ERROR_DATOS;
        }
        return registrar(nuevo);
    }

    public String ingresarGato(String nombre, double peso, int edad) {
        Paciente nuevo;
        try {
            nuevo = new Gato(nombre, peso, edad);
        } catch (IllegalArgumentException e) {
            return ERROR_DATOS;
        }
        return registrar(nuevo);
    }

    public String ingresarAve(String nombre, double peso, int edad, double envergadura) {
        Paciente nuevo;
        try {
            nuevo = new AveExotica(nombre, peso, edad, envergadura);
        } catch (IllegalArgumentException e) {
            return ERROR_DATOS;
        }
        return registrar(nuevo);
    }

    /** R2: orden de validación: datos (ya hechos), duplicado, lleno. */
    private String registrar(Paciente nuevo) {
        if (buscarIndice(nuevo.getNombre()) >= 0) {
            return "ERROR: nombre duplicado";
        }
        if (contador >= MAX_PACIENTES) {
            return "ERROR: hospital lleno";
        }
        pacientes[contador++] = nuevo;
        if (nuevo.estaEnCuarentena()) {
            return "OK: " + nuevo.getNombre() + " ingresado en CUARENTENA";
        }
        return "OK: " + nuevo.getNombre() + " ingresado";
    }

    // ---------- Vacunar ----------

    public String vacunar(String nombre) {
        int indice = buscarIndice(nombre);
        if (indice < 0) {
            return ERROR_NO_ENCONTRADO;
        }
        Paciente paciente = pacientes[indice];
        if (!(paciente instanceof Perro)) {
            return "ERROR: solo aplica a perros";
        }
        Perro perro = (Perro) paciente;
        if (perro.isVacunado()) {
            return "ERROR: ya vacunado";
        }
        perro.vacunar();
        return "OK: " + perro.getNombre() + " vacunado (pasa a INGRESADO)";
    }

    // ---------- Asignar jaula (R4, R5, R6) ----------

    public String asignar(String nombre) {
        int indice = buscarIndice(nombre);
        if (indice < 0) {
            return ERROR_NO_ENCONTRADO;
        }
        Paciente paciente = pacientes[indice];
        if (paciente.estaEnCuarentena()) {
            return ERROR_CUARENTENA;
        }
        if (paciente.tieneJaula()) {
            return "ERROR: paciente ya tiene jaula";
        }
        for (int i = 0; i < jaulas.length; i++) {
            Jaula jaula = jaulas[i];
            if (jaula.estaLibre() && jaula.esCompatibleCon(paciente)) {
                jaula.setOcupante(paciente);
                paciente.setJaula(jaula);
                return "OK: " + paciente.getNombre() + " asignado a jaula " + jaula.getNumero();
            }
        }
        return "ERROR: no hay jaula compatible";
    }

    // ---------- Medicar (R7, R8, R9) ----------

    /** Una entrada no numérica llega como NaN y se rechaza como dato inválido. */
    public String medicar(String nombre, double mg) {
        int indice = buscarIndice(nombre);
        if (indice < 0) {
            return ERROR_NO_ENCONTRADO;
        }
        if (!(mg > 0) || Double.isInfinite(mg)) {
            return ERROR_DATOS;
        }
        Paciente paciente = pacientes[indice];
        if (paciente.estaEnCuarentena()) {
            return ERROR_CUARENTENA;
        }
        if (!paciente.tieneJaula()) {
            return "ERROR: paciente sin jaula";
        }
        if (paciente.tieneEstresAlto()) {
            return "ERROR: gato con estres alto";
        }
        if (paciente.excedeDosisMaxima(mg)) {
            return "ERROR: dosis excede el maximo";
        }
        if (paciente.superaAcumulado(mg)) {
            return "ERROR: tope acumulado superado";
        }
        paciente.registrarMedicacion(mg);
        return "OK: " + paciente.getNombre() + " medicado con " + formatearMg(mg) + " mg";
    }

    // ---------- Alta (R10) ----------

    public String alta(String nombre) {
        int indice = buscarIndice(nombre);
        if (indice < 0) {
            return ERROR_NO_ENCONTRADO;
        }
        Paciente paciente = pacientes[indice];
        if (!Paciente.EN_TRATAMIENTO.equals(paciente.getEstado())) {
            return "ERROR: paciente no apto para alta";
        }
        Jaula jaula = paciente.getJaula();
        if (jaula != null) {
            jaula.setOcupante(null);
            paciente.setJaula(null);
        }
        // Compactación: los siguientes se desplazan una posición.
        for (int i = indice; i < contador - 1; i++) {
            pacientes[i] = pacientes[i + 1];
        }
        pacientes[contador - 1] = null;
        contador--;
        return "OK: " + paciente.getNombre() + " dado de alta";
    }

    // ---------- Reporte ----------

    public String reporte() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- REPORTE ---\n");
        sb.append("Pacientes: ").append(contador).append("/").append(MAX_PACIENTES).append("\n");
        for (int i = 0; i < contador; i++) {
            Paciente p = pacientes[i];
            String numeroJaula = p.tieneJaula() ? String.valueOf(p.getJaula().getNumero()) : "-";
            sb.append(p.getNombre()).append(" | ")
              .append(p.getTipo()).append(" | ")
              .append(String.format(Locale.US, "%.1f", p.getPeso())).append(" kg | ")
              .append(p.getEstado()).append(" | jaula: ")
              .append(numeroJaula).append("\n");
        }
        sb.append("Jaulas libres: ").append(contarJaulasLibres());
        return sb.toString();
    }

    // ---------- Auxiliares ----------

    /** Búsqueda sin distinguir mayúsculas; devuelve -1 si no existe. */
    private int buscarIndice(String nombre) {
        for (int i = 0; i < contador; i++) {
            if (pacientes[i].getNombre().equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    private int contarJaulasLibres() {
        int libres = 0;
        for (int i = 0; i < jaulas.length; i++) {
            if (jaulas[i].estaLibre()) {
                libres++;
            }
        }
        return libres;
    }

    private String formatearMg(double mg) {
        if (mg == Math.rint(mg) && Math.abs(mg) < 1e15) {
            return String.valueOf((long) mg);
        }
        return String.valueOf(mg);
    }
}
