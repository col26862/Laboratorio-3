package tecnihogar;

public enum CategoriaServicio {
    DIAGNOSTICO("Diagnostico"),
    INSTALACION("Instalacion"),
    INSTALACION_ELECTRICA_CERTIFICADA("Instalacion electrica certificada"),
    MANTENIMIENTO_AIRE_ACONDICIONADO("Mantenimiento de aire acondicionado");

    private final String nombre;

    CategoriaServicio(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
