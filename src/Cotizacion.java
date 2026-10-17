package tecnihogar;

public final class Cotizacion {
    private final CategoriaServicio categoria;
    private final double costo;
    private final int diasAtencion;

    public Cotizacion(OrdenServicio ordenTemporal) {
        this.categoria = ordenTemporal.getCategoria();
        this.costo = ordenTemporal.calcularCosto();
        this.diasAtencion = ordenTemporal.calcularDiasAtencion();
    }

    public CategoriaServicio getCategoria() { return categoria; }
    public double getCosto() { return costo; }
    public int getDiasAtencion() { return diasAtencion; }

    @Override
    public String toString() {
        return String.format("%s | Costo: Q%.2f | Tiempo estimado: %d dias habiles",
                categoria, costo, diasAtencion);
    }
}
