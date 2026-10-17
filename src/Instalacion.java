package tecnihogar;

public class Instalacion extends OrdenServicio {
    private final int cantidadPuntos;

    public Instalacion(String codigo, String cliente, String direccion,
                       double horasEstimadas, Zona zona, int cantidadPuntos) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (cantidadPuntos <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos debe ser positiva.");
        }
        this.cantidadPuntos = cantidadPuntos;
    }

    public double calcularHorasPorPuntos() {
        return cantidadPuntos * 0.75;
    }

    public int calcularHorasFacturables() {
        return (int) Math.ceil(Math.max(getHorasEstimadas(), calcularHorasPorPuntos()));
    }

    protected double obtenerTarifaBase() {
        return getZona() == Zona.METROPOLITANA ? 100.0 : 175.0;
    }

    @Override
    public double calcularCosto() {
        return obtenerTarifaBase() + calcularHorasFacturables() * 90.0;
    }

    @Override
    public int calcularDiasAtencion() {
        return getZona() == Zona.METROPOLITANA ? 3 : 5;
    }

    @Override
    public int obtenerMaximoVisitas() { return 3; }

    @Override
    public CategoriaServicio getCategoria() { return CategoriaServicio.INSTALACION; }

    public int getCantidadPuntos() { return cantidadPuntos; }
}
