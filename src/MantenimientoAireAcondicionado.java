package tecnihogar;

public class MantenimientoAireAcondicionado extends OrdenServicio {
    private final int capacidadBTU;

    public MantenimientoAireAcondicionado(String codigo, String cliente, String direccion,
                                          double horasEstimadas, Zona zona, int capacidadBTU) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (zona != Zona.METROPOLITANA) {
            throw new IllegalArgumentException("El mantenimiento de aire solo se acepta en zona metropolitana.");
        }
        if (capacidadBTU < 9000 || capacidadBTU > 60000) {
            throw new IllegalArgumentException("La capacidad debe estar entre 9000 y 60000 BTU.");
        }
        this.capacidadBTU = capacidadBTU;
    }

    @Override
    public double calcularCosto() {
        return 100.0 + getHorasEstimadas() * 110.0 + 80.0;
    }

    @Override
    public int calcularDiasAtencion() { return 2; }

    @Override
    public int obtenerMaximoVisitas() { return 1; }

    @Override
    public boolean esCompatibleCon(Cuadrilla cuadrilla) {
        return cuadrilla != null && cuadrilla.isCertificadaRefrigeracion();
    }

    @Override
    public CategoriaServicio getCategoria() {
        return CategoriaServicio.MANTENIMIENTO_AIRE_ACONDICIONADO;
    }

    public int getCapacidadBTU() { return capacidadBTU; }
}
