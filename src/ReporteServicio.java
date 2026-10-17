package tecnihogar;

import java.util.EnumMap;
import java.util.Map;

public class ReporteServicio {
    private final SistemaTecniHogar sistema;

    public ReporteServicio(SistemaTecniHogar sistema) {
        this.sistema = sistema;
    }

    public void mostrarReporteOrdenes() {
        System.out.println("\n--- ORDENES POR CATEGORIA Y ESTADO ---");
        for (CategoriaServicio categoria : CategoriaServicio.values()) {
            System.out.println(categoria + ":");
            for (EstadoOrden estado : EstadoOrden.values()) {
                long cantidad = sistema.getOrdenes().stream()
                        .filter(o -> o.getCategoria() == categoria && o.getEstado() == estado).count();
                System.out.printf("  %-15s %d%n", estado, cantidad);
            }
        }
    }

    public void mostrarReporteIngresos() {
        double registros = sistema.getMovimientos().stream()
                .filter(m -> m.getTipo() == TipoIngreso.REGISTRO_ORDEN)
                .mapToDouble(MovimientoIngreso::getMonto).sum();
        double visitas = sistema.getMovimientos().stream()
                .filter(m -> m.getTipo() == TipoIngreso.VISITA_ADICIONAL)
                .mapToDouble(MovimientoIngreso::getMonto).sum();
        Map<CategoriaServicio, Double> porCategoria = new EnumMap<>(CategoriaServicio.class);
        for (CategoriaServicio categoria : CategoriaServicio.values()) porCategoria.put(categoria, 0.0);
        for (MovimientoIngreso movimiento : sistema.getMovimientos()) {
            porCategoria.merge(movimiento.getCategoria(), movimiento.getMonto(), Double::sum);
        }
        System.out.println("\n--- INGRESOS ---");
        System.out.printf("Por registro: Q%.2f%n", registros);
        System.out.printf("Por visitas adicionales: Q%.2f%n", visitas);
        System.out.printf("Total: Q%.2f%n", registros + visitas);
        porCategoria.forEach((categoria, monto) -> System.out.printf("%s: Q%.2f%n", categoria, monto));
    }

    public void mostrarJornadasAbiertas() {
        System.out.println("\n--- JORNADAS NO CERRADAS ---");
        sistema.getJornadas().stream()
                .filter(j -> j.getEstado() != EstadoJornada.CERRADA)
                .forEach(System.out::println);
    }
}
