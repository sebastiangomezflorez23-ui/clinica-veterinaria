public class AveExotica extends Paciente {

    private static final double FACTOR_DOSIS = 3;
    private static final double PESO_MAXIMO = 15;
    private static final double ENVERGADURA_GRANDE = 60;

    private double envergadura;

    /**
     * @throws IllegalArgumentException si algún dato es inválido
     */
    public AveExotica(String nombre, double peso, int edad, double envergadura) {
        super(nombre, peso, edad, FACTOR_DOSIS);
        if (!setEnvergadura(envergadura)) {
            throw new IllegalArgumentException("datos invalidos");
        }
    }

    @Override
    protected double getPesoMaximo() {
        return PESO_MAXIMO;
    }

    @Override
    public String getTipo() {
        return "Ave";
    }

    @Override
    public char getZonaRequerida() {
        return 'B';
    }

    /** R4: un ave con envergadura mayor a 60 cm necesita como mínimo M. */
    @Override
    public char getTamanoRequerido() {
        char porPeso = super.getTamanoRequerido();
        if (envergadura > ENVERGADURA_GRANDE && porPeso == 'S') {
            return 'M';
        }
        return porPeso;
    }

    public double getEnvergadura() {
        return envergadura;
    }

    public boolean setEnvergadura(double envergadura) {
        if (!(envergadura > 0) || Double.isInfinite(envergadura)) {
            return false;
        }
        this.envergadura = envergadura;
        return true;
    }
}
