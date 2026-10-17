package tecnihogar;

public class InstalacionElectricaCertificada extends Instalacion {
    private final double valorMateriales;

    public InstalacionElectricaCertificada(String codigo, String cliente, String direccion,
                                           double horasEstimadas, Zona zona,
                                           int cantidadPuntos, double valorMateriales) {
        super(codigo, cliente, direccion, horasEstimadas, zona, cantidadPuntos);
        if (!Double.isFinite(valorMateriales) || valorMateriales < 0) {
            throw new IllegalArgumentException("El valor de materiales no puede ser negativo.");
        }
        this.valorMateriales = valorMateriales;
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + 120.0 + valorMateriales * 0.05;
    }

    @Override
    public int calcularDiasAtencion() {
        return super.calcularDiasAtencion() + 2;
    }

    @Override
    public int obtenerMaximoVisitas() { return 2; }

    @Override
    public CategoriaServicio getCategoria() {
        return CategoriaServicio.INSTALACION_ELECTRICA_CERTIFICADA;
    }

    public double getValorMateriales() { return valorMateriales; }
}
