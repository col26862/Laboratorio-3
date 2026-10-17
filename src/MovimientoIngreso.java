package tecnihogar;

public final class MovimientoIngreso {
    private final String codigoOrden;
    private final CategoriaServicio categoria;
    private final TipoIngreso tipo;
    private final double monto;

    public MovimientoIngreso(String codigoOrden, CategoriaServicio categoria,
                             TipoIngreso tipo, double monto) {
        this.codigoOrden = codigoOrden;
        this.categoria = categoria;
        this.tipo = tipo;
        this.monto = monto;
    }

    public String getCodigoOrden() { return codigoOrden; }
    public CategoriaServicio getCategoria() { return categoria; }
    public TipoIngreso getTipo() { return tipo; }
    public double getMonto() { return monto; }
}
