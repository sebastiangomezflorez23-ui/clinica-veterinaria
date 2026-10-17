public class Jaula {

    private int numero;
    private char tamano;
    private char zona;
    private Paciente ocupante;

    /**
     * @param tamano 'S', 'M' o 'L'
     * @param zona   'A' o 'B'
     * @throws IllegalArgumentException si algún dato es inválido
     */
    public Jaula(int numero, char tamano, char zona) {
        if (!setNumero(numero) || !setTamano(tamano) || !setZona(zona)) {
            throw new IllegalArgumentException("datos invalidos");
        }
        this.ocupante = null;
    }

    /** Una jaula está libre si no tiene ocupante. */
    public boolean estaLibre() {
        return ocupante == null;
    }

    /** R6: ¿cumple el tamaño y la zona que necesita el paciente? */
    public boolean esCompatibleCon(Paciente paciente) {
        return tamano == paciente.getTamanoRequerido() && zona == paciente.getZonaRequerida();
    }

    public int getNumero() {
        return numero;
    }

    public char getTamano() {
        return tamano;
    }

    public char getZona() {
        return zona;
    }

    public Paciente getOcupante() {
        return ocupante;
    }

    public boolean setNumero(int numero) {
        if (numero <= 0) {
            return false;
        }
        this.numero = numero;
        return true;
    }

    public boolean setTamano(char tamano) {
        if (tamano != 'S' && tamano != 'M' && tamano != 'L') {
            return false;
        }
        this.tamano = tamano;
        return true;
    }

    public boolean setZona(char zona) {
        if (zona != 'A' && zona != 'B') {
            return false;
        }
        this.zona = zona;
        return true;
    }

    /** Acepta null para dejar la jaula libre. */
    public void setOcupante(Paciente ocupante) {
        this.ocupante = ocupante;
    }
}
