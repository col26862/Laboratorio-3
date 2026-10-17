package tecnihogar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SistemaTecniHogar {
    private final List<OrdenServicio> ordenes = new ArrayList<>();
    private final List<Cuadrilla> cuadrillas = new ArrayList<>();
    private final List<Jornada> jornadas = new ArrayList<>();
    private final List<MovimientoIngreso> movimientos = new ArrayList<>();

    public Cotizacion cotizar(OrdenServicio ordenTemporal) {
        if (ordenTemporal == null) throw new IllegalArgumentException("Los datos de la cotizacion son obligatorios.");
        return new Cotizacion(ordenTemporal);
    }

    public void registrarOrden(OrdenServicio orden) {
        agregarOrden(orden);
        movimientos.add(new MovimientoIngreso(orden.getCodigo(), orden.getCategoria(),
                TipoIngreso.REGISTRO_ORDEN, orden.calcularCosto()));
    }

    public void cargarOrdenInicial(OrdenServicio orden) {
        agregarOrden(orden);
    }

    private void agregarOrden(OrdenServicio orden) {
        if (orden == null) throw new IllegalArgumentException("La orden es obligatoria.");
        if (buscarOrden(orden.getCodigo()) != null) throw new IllegalArgumentException("Ya existe una orden con ese codigo.");
        ordenes.add(orden);
    }

    public void registrarCuadrilla(Cuadrilla cuadrilla) {
        if (cuadrilla == null) throw new IllegalArgumentException("La cuadrilla es obligatoria.");
        if (buscarCuadrilla(cuadrilla.getCodigo()) != null) throw new IllegalArgumentException("Ya existe una cuadrilla con ese codigo.");
        cuadrillas.add(cuadrilla);
    }

    public Jornada crearJornada(String codigo, String codigoCuadrilla) {
        if (buscarJornada(codigo) != null) throw new IllegalArgumentException("Ya existe una jornada con ese codigo.");
        Cuadrilla cuadrilla = requerirCuadrilla(codigoCuadrilla);
        Jornada jornada = new Jornada(codigo, cuadrilla);
        jornadas.add(jornada);
        return jornada;
    }

    public void asignarOrden(String codigoJornada, String codigoOrden) {
        requerirJornada(codigoJornada).asignarOrden(requerirOrden(codigoOrden));
    }

    public void retirarOrden(String codigoJornada, String codigoOrden) {
        requerirJornada(codigoJornada).retirarOrden(requerirOrden(codigoOrden));
    }

    public void iniciarJornada(String codigoJornada) {
        Jornada jornada = requerirJornada(codigoJornada);
        jornada.iniciar();
        for (OrdenServicio orden : jornada.getOrdenes()) {
            if (orden.getVisitasRealizadas() > 1) {
                movimientos.add(new MovimientoIngreso(orden.getCodigo(), orden.getCategoria(),
                        TipoIngreso.VISITA_ADICIONAL, 40.0));
            }
        }
    }

    public void registrarResultado(String codigoJornada, String codigoOrden, ResultadoVisita resultado) {
        Jornada jornada = requerirJornada(codigoJornada);
        OrdenServicio orden = requerirOrden(codigoOrden);
        jornada.registrarResultado(orden, resultado);
    }

    private OrdenServicio requerirOrden(String codigo) {
        OrdenServicio orden = buscarOrden(codigo);
        if (orden == null) throw new IllegalArgumentException("Orden no encontrada.");
        return orden;
    }

    private Cuadrilla requerirCuadrilla(String codigo) {
        Cuadrilla cuadrilla = buscarCuadrilla(codigo);
        if (cuadrilla == null) throw new IllegalArgumentException("Cuadrilla no encontrada.");
        return cuadrilla;
    }

    private Jornada requerirJornada(String codigo) {
        Jornada jornada = buscarJornada(codigo);
        if (jornada == null) throw new IllegalArgumentException("Jornada no encontrada.");
        return jornada;
    }

    public OrdenServicio buscarOrden(String codigo) {
        if (codigo == null) return null;
        return ordenes.stream().filter(o -> o.getCodigo().equalsIgnoreCase(codigo.trim())).findFirst().orElse(null);
    }

    public Cuadrilla buscarCuadrilla(String codigo) {
        if (codigo == null) return null;
        return cuadrillas.stream().filter(c -> c.getCodigo().equalsIgnoreCase(codigo.trim())).findFirst().orElse(null);
    }

    public Jornada buscarJornada(String codigo) {
        if (codigo == null) return null;
        return jornadas.stream().filter(j -> j.getCodigo().equalsIgnoreCase(codigo.trim())).findFirst().orElse(null);
    }

    public List<OrdenServicio> getOrdenes() { return Collections.unmodifiableList(ordenes); }
    public List<Cuadrilla> getCuadrillas() { return Collections.unmodifiableList(cuadrillas); }
    public List<Jornada> getJornadas() { return Collections.unmodifiableList(jornadas); }
    public List<MovimientoIngreso> getMovimientos() { return Collections.unmodifiableList(movimientos); }
}
