package tecnihogar;

import java.util.Scanner;
import java.util.function.Supplier;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final SistemaTecniHogar SISTEMA = new SistemaTecniHogar();

    public static void main(String[] args) {
        cargarDatosIniciales();
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            try {
                ejecutarOpcion(opcion);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Operacion rechazada: " + e.getMessage());
            }
        } while (opcion != 0);
        System.out.println("Programa finalizado.");
    }

    private static void mostrarMenu() {
        System.out.println("\n========== TECNIHOGAR ==========");
        System.out.println("1. Cotizar servicio");
        System.out.println("2. Registrar orden");
        System.out.println("3. Registrar cuadrilla");
        System.out.println("4. Crear jornada");
        System.out.println("5. Asignar orden a jornada");
        System.out.println("6. Retirar orden de jornada");
        System.out.println("7. Iniciar jornada");
        System.out.println("8. Registrar resultado de visita");
        System.out.println("9. Consultar ordenes");
        System.out.println("10. Consultar cuadrillas");
        System.out.println("11. Consultar jornadas");
        System.out.println("12. Generar reportes");
        System.out.println("0. Salir");
    }

    private static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> System.out.println("Cotizacion: " + SISTEMA.cotizar(solicitarOrden("COT-TEMP")));
            case 2 -> { OrdenServicio orden = solicitarOrden(leerTexto("Codigo: ")); SISTEMA.registrarOrden(orden); System.out.println("Orden registrada y cobrada."); }
            case 3 -> registrarCuadrilla();
            case 4 -> { SISTEMA.crearJornada(leerTexto("Codigo de jornada: "), leerTexto("Codigo de cuadrilla: ")); System.out.println("Jornada creada."); }
            case 5 -> { SISTEMA.asignarOrden(leerTexto("Codigo de jornada: "), leerTexto("Codigo de orden: ")); System.out.println("Orden asignada."); }
            case 6 -> { SISTEMA.retirarOrden(leerTexto("Codigo de jornada: "), leerTexto("Codigo de orden: ")); System.out.println("Orden retirada."); }
            case 7 -> { SISTEMA.iniciarJornada(leerTexto("Codigo de jornada: ")); System.out.println("Jornada iniciada."); }
            case 8 -> registrarResultado();
            case 9 -> SISTEMA.getOrdenes().forEach(System.out::println);
            case 10 -> SISTEMA.getCuadrillas().forEach(System.out::println);
            case 11 -> SISTEMA.getJornadas().forEach(System.out::println);
            case 12 -> generarReportes();
            case 0 -> { }
            default -> System.out.println("Opcion inexistente.");
        }
    }

    private static OrdenServicio solicitarOrden(String codigo) {
        System.out.println("1. Diagnostico  2. Instalacion  3. Instalacion electrica certificada  4. Mantenimiento de aire");
        int tipo = leerEntero("Categoria: ");
        String cliente = leerTexto("Cliente: ");
        String direccion = leerTexto("Direccion: ");
        double horas = leerDouble("Horas estimadas: ");
        Zona zona = leerZona();
        return switch (tipo) {
            case 1 -> new Diagnostico(codigo, cliente, direccion, horas, zona);
            case 2 -> new Instalacion(codigo, cliente, direccion, horas, zona, leerEntero("Cantidad de puntos: "));
            case 3 -> new InstalacionElectricaCertificada(codigo, cliente, direccion, horas, zona,
                    leerEntero("Cantidad de puntos: "), leerDouble("Valor de materiales: Q"));
            case 4 -> new MantenimientoAireAcondicionado(codigo, cliente, direccion, horas, zona,
                    leerEntero("Capacidad en BTU: "));
            default -> throw new IllegalArgumentException("Categoria inexistente.");
        };
    }

    private static void registrarCuadrilla() {
        String codigo = leerTexto("Codigo: ");
        double capacidad = leerDouble("Capacidad maxima de horas: ");
        boolean certificada = leerTexto("Certificada en refrigeracion (S/N): ").equalsIgnoreCase("S");
        SISTEMA.registrarCuadrilla(new Cuadrilla(codigo, capacidad, certificada));
        System.out.println("Cuadrilla registrada.");
    }

    private static void registrarResultado() {
        String jornada = leerTexto("Codigo de jornada: ");
        String orden = leerTexto("Codigo de orden: ");
        int resultado = leerEntero("1. Completada  2. Fallida: ");
        if (resultado != 1 && resultado != 2) throw new IllegalArgumentException("Resultado inexistente.");
        SISTEMA.registrarResultado(jornada, orden,
                resultado == 1 ? ResultadoVisita.COMPLETADA : ResultadoVisita.FALLIDA);
        System.out.println("Resultado registrado.");
    }

    private static void generarReportes() {
        ReporteServicio reporte = new ReporteServicio(SISTEMA);
        reporte.mostrarReporteOrdenes();
        reporte.mostrarReporteIngresos();
        reporte.mostrarJornadasAbiertas();
    }

    private static Zona leerZona() {
        int zona = leerEntero("Zona (1. Metropolitana, 2. Periferica): ");
        if (zona == 1) return Zona.METROPOLITANA;
        if (zona == 2) return Zona.PERIFERICA;
        throw new IllegalArgumentException("Zona inexistente.");
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return SCANNER.nextLine();
    }

    private static int leerEntero(String mensaje) {
        return leerNumeroSeguro(mensaje, () -> Integer.parseInt(SCANNER.nextLine().trim()));
    }

    private static double leerDouble(String mensaje) {
        return leerNumeroSeguro(mensaje, () -> Double.parseDouble(SCANNER.nextLine().trim()));
    }

    private static <T> T leerNumeroSeguro(String mensaje, Supplier<T> lector) {
        while (true) {
            System.out.print(mensaje);
            try { return lector.get(); }
            catch (NumberFormatException e) { System.out.println("Formato incorrecto. Intente nuevamente."); }
        }
    }

    private static void cargarDatosIniciales() {
        SISTEMA.cargarOrdenInicial(new Diagnostico("D-01", "Ana Lopez", "Zona 1", 1.0, Zona.METROPOLITANA));
        SISTEMA.cargarOrdenInicial(new Diagnostico("D-02", "Luis Perez", "Mixco", 1.5, Zona.PERIFERICA));
        SISTEMA.cargarOrdenInicial(new Instalacion("I-01", "Marta Diaz", "Zona 10", 2.5, Zona.METROPOLITANA, 5));
        SISTEMA.cargarOrdenInicial(new Instalacion("I-02", "Carlos Ruiz", "Villa Nueva", 3.2, Zona.PERIFERICA, 3));
        SISTEMA.cargarOrdenInicial(new InstalacionElectricaCertificada("E-01", "Sofia Paz", "Zona 14", 2.5, Zona.METROPOLITANA, 5, 2000));
        SISTEMA.cargarOrdenInicial(new InstalacionElectricaCertificada("E-02", "Mario Leon", "Fraijanes", 4.0, Zona.PERIFERICA, 4, 3500));
        SISTEMA.cargarOrdenInicial(new MantenimientoAireAcondicionado("A-01", "Elena Soto", "Zona 9", 2.0, Zona.METROPOLITANA, 12000));
        SISTEMA.cargarOrdenInicial(new MantenimientoAireAcondicionado("A-02", "Pedro Gil", "Zona 15", 3.5, Zona.METROPOLITANA, 36000));
        SISTEMA.registrarCuadrilla(new Cuadrilla("C-01", 8.0, true));
        SISTEMA.registrarCuadrilla(new Cuadrilla("C-02", 10.0, false));
        SISTEMA.registrarCuadrilla(new Cuadrilla("C-03", 6.0, false));
    }
}
