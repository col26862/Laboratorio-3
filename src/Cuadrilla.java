package tecnihogar;

public class Cuadrilla {
    private final String codigo;
    private final double capacidadMaximaHoras;
    private final boolean certificadaRefrigeracion;
    private Jornada jornadaActiva;

    public Cuadrilla(String codigo, double capacidadMaximaHoras, boolean certificadaRefrigeracion) {
        this.codigo = Validacion.textoObligatorio(codigo, "El codigo");
        this.capacidadMaximaHoras = Validacion.positivo(capacidadMaximaHoras, "La capacidad");
        this.certificadaRefrigeracion = certificadaRefrigeracion;
    }

    void asignarJornada(Jornada jornada) {
        if (!estaDisponible()) {
            throw new IllegalStateException("La cuadrilla ya tiene una jornada sin cerrar.");
        }
        jornadaActiva = jornada;
    }

    void liberarJornada(Jornada jornada) {
        if (jornadaActiva != jornada) {
            throw new IllegalStateException("La jornada no corresponde a esta cuadrilla.");
        }
        jornadaActiva = null;
    }

    public boolean estaDisponible() { return jornadaActiva == null; }
    public String getCodigo() { return codigo; }
    public double getCapacidadMaximaHoras() { return capacidadMaximaHoras; }
    public boolean isCertificadaRefrigeracion() { return certificadaRefrigeracion; }
    public Jornada getJornadaActiva() { return jornadaActiva; }

    @Override
    public String toString() {
        return String.format("%s | Capacidad: %.2f h | Refrigeracion: %s | %s", codigo,
                capacidadMaximaHoras, certificadaRefrigeracion ? "Si" : "No",
                estaDisponible() ? "Disponible" : "Jornada: " + jornadaActiva.getCodigo());
    }
}
