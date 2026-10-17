public class Gato extends Paciente {

    private static final double FACTOR_DOSIS = 6;
    private static final double PESO_MAXIMO = 15;
    private static final int ESTRES_MAXIMO = 10;
    private static final int ESTRES_BLOQUEO = 8;
    private static final int INCREMENTO_ESTRES = 2;

    private int nivelEstres;

    /**
     * @throws IllegalArgumentException si algún dato es inválido
     */
    public Gato(String nombre, double peso, int edad) {
        this(nombre, peso, edad, 0);
    }

    public Gato(String nombre, double peso, int edad, int nivelEstres) {
        super(nombre, peso, edad, FACTOR_DOSIS);
        if (!setNivelEstres(nivelEstres)) {
            throw new IllegalArgumentException("datos invalidos");
        }
    }

    @Override
    protected double getPesoMaximo() {
        return PESO_MAXIMO;
    }

    @Override
    public String getTipo() {
        return "Gato";
    }

    @Override
    public char getZonaRequerida() {
        return 'A';
    }

    /** R9: con estrés de 8 o más no se le puede medicar. */
    @Override
    public boolean tieneEstresAlto() {
        return nivelEstres >= ESTRES_BLOQUEO;
    }

    /** R9: cada medicación sube el estrés 2 puntos (tope 10). */
    @Override
    public void registrarMedicacion(double mg) {
        super.registrarMedicacion(mg);
        setNivelEstres(Math.min(ESTRES_MAXIMO, nivelEstres + INCREMENTO_ESTRES));
    }

    public int getNivelEstres() {
        return nivelEstres;
    }

    public boolean setNivelEstres(int nivelEstres) {
        if (nivelEstres < 0 || nivelEstres > ESTRES_MAXIMO) {
            return false;
        }
        this.nivelEstres = nivelEstres;
        return true;
    }
}
