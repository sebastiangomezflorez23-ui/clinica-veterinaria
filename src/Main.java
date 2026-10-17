import java.util.Scanner;

/**
 * Única clase que usa Scanner y System.out.
 * Lee un comando por línea hasta recibir "salir".
 */
public class Main {

    private static final String ERROR_DESCONOCIDO = "ERROR: comando desconocido";

    public static void main(String[] args) {
        Hospital hospital = new Hospital();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            String[] partes = linea.split("\\s+");
            String comando = partes[0].toLowerCase();
            if (comando.equals("salir")) {
                break;
            }
            System.out.println(procesar(hospital, comando, partes));
        }
        scanner.close();
    }

    private static String procesar(Hospital hospital, String comando, String[] p) {
        switch (comando) {
            case "ingresar":
                return ingresar(hospital, p);
            case "vacunar":
                return p.length == 2 ? hospital.vacunar(p[1]) : ERROR_DESCONOCIDO;
            case "asignar":
                return p.length == 2 ? hospital.asignar(p[1]) : ERROR_DESCONOCIDO;
            case "medicar":
                if (p.length != 3) {
                    return Hospital.ERROR_DATOS;
                }
                return hospital.medicar(p[1], leerDecimal(p[2]));
            case "alta":
                return p.length == 2 ? hospital.alta(p[1]) : ERROR_DESCONOCIDO;
            case "reporte":
                return p.length == 1 ? hospital.reporte() : ERROR_DESCONOCIDO;
            default:
                return ERROR_DESCONOCIDO;
        }
    }

    private static String ingresar(Hospital hospital, String[] p) {
        if (p.length < 2) {
            return Hospital.ERROR_DATOS;
        }
        switch (p[1].toLowerCase()) {
            case "perro":
                if (p.length != 7) {
                    return Hospital.ERROR_DATOS;
                }
                String vacunadoTxt = p[6].toLowerCase();
                if (!vacunadoTxt.equals("si") && !vacunadoTxt.equals("no")) {
                    return Hospital.ERROR_DATOS;
                }
                return hospital.ingresarPerro(p[2], leerDecimal(p[3]), leerEntero(p[4]), p[5],
                        vacunadoTxt.equals("si"));
            case "gato":
                if (p.length != 5) {
                    return Hospital.ERROR_DATOS;
                }
                return hospital.ingresarGato(p[2], leerDecimal(p[3]), leerEntero(p[4]));
            case "ave":
                if (p.length != 6) {
                    return Hospital.ERROR_DATOS;
                }
                return hospital.ingresarAve(p[2], leerDecimal(p[3]), leerEntero(p[4]), leerDecimal(p[5]));
            default:
                return Hospital.ERROR_DATOS;
        }
    }

    /** Devuelve NaN si el texto no es un número (el Hospital lo rechaza). */
    private static double leerDecimal(String texto) {
        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    /** Devuelve -1 si el texto no es un entero (el dominio lo rechaza). */
    private static int leerEntero(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
