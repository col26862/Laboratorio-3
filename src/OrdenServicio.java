package tecnihogar;

public abstract class OrdenServicio {
    private final String codigo;
    private final String nombreCliente;
    private final String direccion;
    private final double horasEstimadas;
    private final Zona zona;
    private EstadoOrden estado;
    private int visitasRealizadas;
    private Jornada jornadaAsignada;

    protected OrdenServicio(String codigo, String nombreCliente, String direccion,
                            double horasEstimadas, Zona zona) {
        this.codigo = Validacion.textoObligatorio(codigo, "El codigo");
        this.nombreCliente = Validacion.textoObligatorio(nombreCliente, "El nombre del cliente");
        this.direccion = Validacion.textoObligatorio(direccion, "La direccion");
        this.horasEstimadas = Validacion.positivo(horasEstimadas, "Las horas estimadas");
        if (zona == null) {
            throw new IllegalArgumentException("La zona es obligatoria.");
        }
        this.zona = zona;
        this.estado = EstadoOrden.PENDIENTE;
        this.visitasRealizadas = 0;
    }

    public abstract double calcularCosto();
    public abstract int calcularDiasAtencion();
    public abstract int obtenerMaximoVisitas();
    public abstract CategoriaServicio getCategoria();

    public boolean esCompatibleCon(Cuadrilla cuadrilla) {
        return cuadrilla != null;
    }

    void asignarAJornada(Jornada jornada) {
        if (estado != EstadoOrden.PENDIENTE || jornadaAsignada != null) {
            throw new IllegalStateException("La orden no esta disponible para asignacion.");
        }
        jornadaAsignada = jornada;
        estado = EstadoOrden.ASIGNADA;
    }

    void retirarDeJornada(Jornada jornada) {
        if (estado != EstadoOrden.ASIGNADA || jornadaAsignada != jornada) {
            throw new IllegalStateException("La orden no pertenece a esta jornada.");
        }
        jornadaAsignada = null;
        estado = EstadoOrden.PENDIENTE;
    }

    void iniciarVisita(Jornada jornada) {
        if (estado != EstadoOrden.ASIGNADA || jornadaAsignada != jornada) {
            throw new IllegalStateException("La orden no puede iniciar atencion.");
        }
        visitasRealizadas++;
        estado = EstadoOrden.EN_ATENCION;
    }

    void registrarResultado(Jornada jornada, ResultadoVisita resultado) {
        if (estado != EstadoOrden.EN_ATENCION || jornadaAsignada != jornada) {
            throw new IllegalStateException("La orden no esta en atencion en esta jornada.");
        }
        if (resultado == null) {
            throw new IllegalArgumentException("El resultado es obligatorio.");
        }
        jornadaAsignada = null;
        if (resultado == ResultadoVisita.COMPLETADA) {
            estado = EstadoOrden.COMPLETADA;
        } else if (visitasRealizadas >= obtenerMaximoVisitas()) {
            estado = EstadoOrden.CANCELADA;
        } else {
            estado = EstadoOrden.PENDIENTE;
        }
    }

    public String getCodigo() { return codigo; }
    public String getNombreCliente() { return nombreCliente; }
    public String getDireccion() { return direccion; }
    public double getHorasEstimadas() { return horasEstimadas; }
    public Zona getZona() { return zona; }
    public EstadoOrden getEstado() { return estado; }
    public int getVisitasRealizadas() { return visitasRealizadas; }
    public Jornada getJornadaAsignada() { return jornadaAsignada; }

    @Override
    public String toString() {
        return String.format("%s | %s | Cliente: %s | Estado: %s | Horas: %.2f | Costo: Q%.2f | Dias: %d | Visitas: %d/%d",
                codigo, getCategoria(), nombreCliente, estado, horasEstimadas,
                calcularCosto(), calcularDiasAtencion(), visitasRealizadas, obtenerMaximoVisitas());
    }
}
