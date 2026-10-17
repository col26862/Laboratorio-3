package tecnihogar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Jornada {
    private final String codigo;
    private final Cuadrilla cuadrilla;
    private final List<OrdenServicio> ordenes = new ArrayList<>();
    private final Set<String> resultadosRegistrados = new HashSet<>();
    private EstadoJornada estado = EstadoJornada.EN_PREPARACION;

    public Jornada(String codigo, Cuadrilla cuadrilla) {
        this.codigo = Validacion.textoObligatorio(codigo, "El codigo");
        if (cuadrilla == null) {
            throw new IllegalArgumentException("La cuadrilla es obligatoria.");
        }
        this.cuadrilla = cuadrilla;
        cuadrilla.asignarJornada(this);
    }

    public void asignarOrden(OrdenServicio orden) {
        exigirEstado(EstadoJornada.EN_PREPARACION, "Solo se asignan ordenes durante la preparacion.");
        if (orden == null) throw new IllegalArgumentException("La orden es obligatoria.");
        if (!orden.esCompatibleCon(cuadrilla)) {
            throw new IllegalStateException("La orden no es compatible con la cuadrilla.");
        }
        if (calcularHorasAsignadas() + orden.getHorasEstimadas() > cuadrilla.getCapacidadMaximaHoras()) {
            throw new IllegalStateException("La asignacion supera la capacidad de la cuadrilla.");
        }
        orden.asignarAJornada(this);
        ordenes.add(orden);
    }

    public void retirarOrden(OrdenServicio orden) {
        exigirEstado(EstadoJornada.EN_PREPARACION, "Solo se retiran ordenes durante la preparacion.");
        if (!ordenes.contains(orden)) {
            throw new IllegalStateException("La orden no esta asignada a la jornada.");
        }
        orden.retirarDeJornada(this);
        ordenes.remove(orden);
    }

    public void iniciar() {
        exigirEstado(EstadoJornada.EN_PREPARACION, "La jornada no puede iniciarse en su estado actual.");
        if (ordenes.isEmpty()) {
            throw new IllegalStateException("No se puede iniciar una jornada vacia.");
        }
        for (OrdenServicio orden : ordenes) orden.iniciarVisita(this);
        estado = EstadoJornada.INICIADA;
    }

    public void registrarResultado(OrdenServicio orden, ResultadoVisita resultado) {
        exigirEstado(EstadoJornada.INICIADA, "La jornada no esta iniciada.");
        if (!ordenes.contains(orden)) throw new IllegalStateException("La orden no pertenece a la jornada.");
        if (resultadosRegistrados.contains(orden.getCodigo())) {
            throw new IllegalStateException("Esta orden ya tiene resultado.");
        }
        orden.registrarResultado(this, resultado);
        resultadosRegistrados.add(orden.getCodigo());
        if (resultadosRegistrados.size() == ordenes.size()) {
            estado = EstadoJornada.CERRADA;
            cuadrilla.liberarJornada(this);
        }
    }

    private void exigirEstado(EstadoJornada esperado, String mensaje) {
        if (estado != esperado) throw new IllegalStateException(mensaje);
    }

    public double calcularHorasAsignadas() {
        return ordenes.stream().mapToDouble(OrdenServicio::getHorasEstimadas).sum();
    }

    public double calcularCapacidadDisponible() {
        return cuadrilla.getCapacidadMaximaHoras() - calcularHorasAsignadas();
    }

    public String getCodigo() { return codigo; }
    public Cuadrilla getCuadrilla() { return cuadrilla; }
    public EstadoJornada getEstado() { return estado; }
    public List<OrdenServicio> getOrdenes() { return Collections.unmodifiableList(ordenes); }

    @Override
    public String toString() {
        return String.format("%s | Cuadrilla: %s | Estado: %s | Ordenes: %d | Horas: %.2f | Disponibles: %.2f",
                codigo, cuadrilla.getCodigo(), estado, ordenes.size(),
                calcularHorasAsignadas(), calcularCapacidadDisponible());
    }
}
