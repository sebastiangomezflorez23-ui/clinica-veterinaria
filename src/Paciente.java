/**
 * Clase base de todos los pacientes de la clínica.
 * No lee ni imprime: solo guarda datos y aplica sus propias validaciones.
 */
public abstract class Paciente {

    public static final String INGRESADO = "INGRESADO";
    public static final String CUARENTENA = "CUARENTENA";
    public static final String EN_TRATAMIENTO = "EN_TRATAMIENTO";

    private static final double EPSILON = 1e-9;
    private static final int MULTIPLO_ACUMULADO = 3;

    private String nombre;
    private double peso;
    private int edad;
    private String estado;
    private final double factorDosis;
    private double dosisAcumulada;
    private Jaula jaula;

    /**
     * @throws IllegalArgumentException si algún dato es inválido
     */
    protected Paciente(String nombre, double peso, int edad, double factorDosis) {
        this.factorDosis = factorDosis;
        this.estado = INGRESADO;
        this.dosisAcumulada = 0;
        this.jaula = null;
        if (!setNombre(nombre) || !setPeso(peso) || !setEdad(edad)) {
            throw new IllegalArgumentException("datos invalidos");
        }
    }

    // ---------- Métodos que cada subclase define ----------

    /** Peso máximo permitido para el tipo de paciente. */
    protected abstract double getPesoMaximo();

    /** Nombre del tipo, usado en el reporte. */
    public abstract String getTipo();

    /** Zona de jaulas donde puede alojarse ('A' o 'B'). */
    public abstract char getZonaRequerida();

    // ---------- Reglas comunes ----------

    /** R4: tamaño de jaula según el peso (S, M o L). */
    public char getTamanoRequerido() {
        if (peso < 5) {
            return 'S';
        }
        if (peso <= 25) {
            return 'M';
        }
        return 'L';
    }

    /** R7: dosis máxima por aplicación (mg). */
    public double getDosisMaxima() {
        return peso * factorDosis;
    }

    /** R7: tope de dosis acumulada (mg). */
    public double getAcumuladoMaximo() {
        return MULTIPLO_ACUMULADO * getDosisMaxima();
    }

    public boolean excedeDosisMaxima(double mg) {
        return mg > getDosisMaxima() + EPSILON;
    }

    public boolean superaAcumulado(double mg) {
        return dosisAcumulada + mg > getAcumuladoMaximo() + EPSILON;
    }

    /** Por defecto ningún paciente tiene estrés alto; Gato lo redefine. */
    public boolean tieneEstresAlto() {
        return false;
    }

    /** R9: registra una medicación ya validada. */
    public void registrarMedicacion(double mg) {
        dosisAcumulada += mg;
        estado = EN_TRATAMIENTO;
    }

    public boolean estaEnCuarentena() {
        return CUARENTENA.equals(estado);
    }

    public boolean tieneJaula() {
        return jaula != null;
    }

    // ---------- Getters ----------

    public String getNombre() {
        return nombre;
    }

    public double getPeso() {
        return peso;
    }

    public int getEdad() {
        return edad;
    }

    public String getEstado() {
        return estado;
    }

    public double getFactorDosis() {
        return factorDosis;
    }

    public double getDosisAcumulada() {
        return dosisAcumulada;
    }

    public Jaula getJaula() {
        return jaula;
    }

    // ---------- Setters con validación ----------

    public boolean setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        this.nombre = nombre.trim();
        return true;
    }

    public boolean setPeso(double peso) {
        if (!(peso > 0) || peso > getPesoMaximo()) {
            return false;
        }
        this.peso = peso;
        return true;
    }

    public boolean setEdad(int edad) {
        if (edad < 0 || edad > 30) {
            return false;
        }
        this.edad = edad;
        return true;
    }

    public boolean setEstado(String estado) {
        if (INGRESADO.equals(estado) || CUARENTENA.equals(estado) || EN_TRATAMIENTO.equals(estado)) {
            this.estado = estado;
            return true;
        }
        return false;
    }

    public boolean setDosisAcumulada(double dosisAcumulada) {
        if (!(dosisAcumulada >= 0)) {
            return false;
        }
        this.dosisAcumulada = dosisAcumulada;
        return true;
    }

    /** Acepta null para indicar que el paciente no tiene jaula. */
    public void setJaula(Jaula jaula) {
        this.jaula = jaula;
    }
}
