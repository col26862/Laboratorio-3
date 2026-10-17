package tecnihogar;

public class Pruebas {
    private static int aprobadas;

    public static void main(String[] args) {
        probarCostosYTiempos();
        probarRestricciones();
        probarVisitasEIngresos();
        System.out.println("Pruebas aprobadas: " + aprobadas + "/19");
    }

    private static void probarCostosYTiempos() {
        verificarCerca(150, new Diagnostico("1", "A", "D", 1, Zona.METROPOLITANA).calcularCosto(), "Diagnostico metro");
        verificarCerca(225, new Diagnostico("2", "A", "D", 1, Zona.PERIFERICA).calcularCosto(), "Diagnostico periferia");
        Instalacion i = new Instalacion("3", "A", "D", 2.5, Zona.METROPOLITANA, 5);
        verificarCerca(460, i.calcularCosto(), "Horas por puntos y fraccion");
        verificarCerca(680, new InstalacionElectricaCertificada("4", "A", "D", 2.5, Zona.METROPOLITANA, 5, 2000).calcularCosto(), "Certificado y garantia");
        verificarCerca(455, new MantenimientoAireAcondicionado("5", "A", "D", 2.5, Zona.METROPOLITANA, 12000).calcularCosto(), "Mantenimiento");
        verificar(i.calcularDiasAtencion() == 3, "Tiempo instalacion metro");
        verificar(new InstalacionElectricaCertificada("6", "A", "D", 1, Zona.PERIFERICA, 1, 100).calcularDiasAtencion() == 7, "Tiempo certificada periferia");
    }

    private static void probarRestricciones() {
        esperarError(() -> new Diagnostico("7", "A", "D", 2, Zona.METROPOLITANA), "Limite diagnostico");
        esperarError(() -> new MantenimientoAireAcondicionado("8", "A", "D", 1, Zona.PERIFERICA, 12000), "Zona mantenimiento");
        SistemaTecniHogar s = new SistemaTecniHogar();
        s.cargarOrdenInicial(new MantenimientoAireAcondicionado("A", "A", "D", 2, Zona.METROPOLITANA, 12000));
        s.registrarCuadrilla(new Cuadrilla("C", 8, false));
        s.crearJornada("J", "C");
        esperarError(() -> s.asignarOrden("J", "A"), "Certificacion requerida");
        s.cargarOrdenInicial(new Diagnostico("D", "A", "D", 1.5, Zona.METROPOLITANA));
        s.registrarCuadrilla(new Cuadrilla("C2", 1, true));
        s.crearJornada("J2", "C2");
        esperarError(() -> s.asignarOrden("J2", "D"), "Capacidad excedida");
    }

    private static void probarVisitasEIngresos() {
        SistemaTecniHogar s = new SistemaTecniHogar();
        Diagnostico d = new Diagnostico("D", "A", "D", 1, Zona.METROPOLITANA);
        s.cargarOrdenInicial(d);
        s.registrarCuadrilla(new Cuadrilla("C", 8, true));
        verificar(s.getMovimientos().isEmpty(), "Datos iniciales sin ingresos");
        s.cotizar(new Instalacion("TEMP", "A", "D", 2, Zona.METROPOLITANA, 2));
        verificar(s.getMovimientos().isEmpty(), "Cotizacion sin ingresos");
        s.crearJornada("JR", "C");
        s.asignarOrden("JR", "D");
        s.retirarOrden("JR", "D");
        verificar(s.getMovimientos().isEmpty(), "Retiro sin ingresos");
        verificar(d.getEstado() == EstadoOrden.PENDIENTE, "Retiro devuelve la orden a pendiente");
        for (int visita = 1; visita <= 3; visita++) {
            String codigoJornada = visita == 1 ? "JR" : "J" + visita;
            if (visita > 1) s.crearJornada(codigoJornada, "C");
            s.asignarOrden(codigoJornada, "D");
            s.iniciarJornada(codigoJornada);
            s.registrarResultado(codigoJornada, "D", ResultadoVisita.FALLIDA);
        }
        verificar(d.getEstado() == EstadoOrden.CANCELADA, "Cancelacion al agotar visitas");
        verificarCerca(80, s.getMovimientos().stream().mapToDouble(MovimientoIngreso::getMonto).sum(), "Dos visitas adicionales");
        int cantidad = s.getMovimientos().size();
        esperarError(() -> s.asignarOrden("J3", "D"), "Operacion invalida no reasigna");
        verificar(s.getMovimientos().size() == cantidad, "Operacion rechazada no cambia ingresos");
    }

    private static void verificar(boolean condicion, String nombre) {
        if (!condicion) throw new AssertionError("Fallo: " + nombre);
        aprobadas++;
    }

    private static void verificarCerca(double esperado, double obtenido, String nombre) {
        verificar(Math.abs(esperado - obtenido) < 0.001, nombre + " esperado=" + esperado + " obtenido=" + obtenido);
    }

    private static void esperarError(Runnable accion, String nombre) {
        try { accion.run(); }
        catch (IllegalArgumentException | IllegalStateException e) { aprobadas++; return; }
        throw new AssertionError("Fallo: " + nombre);
    }
}
