public class Perro extends Paciente {

    private static final double FACTOR_DOSIS = 10;
    private static final double PESO_MAXIMO = 80;

    private String raza;
    private boolean vacunado;

    /**
     * R3: un perro no vacunado ingresa en CUARENTENA.
     *
     * @throws IllegalArgumentException si algún dato es inválido
     */
    public Perro(String nombre, double peso, int edad, String raza, boolean vacunado) {
        super(nombre, peso, edad, FACTOR_DOSIS);
        if (!setRaza(raza)) {
            throw new IllegalArgumentException("datos invalidos");
        }
        this.vacunado = vacunado;
        if (!vacunado) {
            setEstado(CUARENTENA);
        }
    }

    @Override
    protected double getPesoMaximo() {
        return PESO_MAXIMO;
    }

    @Override
    public String getTipo() {
        return "Perro";
    }

    @Override
    public char getZonaRequerida() {
        return 'A';
    }

    /** Vacuna al perro y, si estaba en cuarentena, pasa a INGRESADO. */
    public void vacunar() {
        vacunado = true;
        if (estaEnCuarentena()) {
            setEstado(INGRESADO);
        }
    }

    public String getRaza() {
        return raza;
    }

    public boolean isVacunado() {
        return vacunado;
    }

    public boolean setRaza(String raza) {
        if (raza == null || raza.trim().isEmpty()) {
            return false;
        }
        this.raza = raza.trim();
        return true;
    }
}
