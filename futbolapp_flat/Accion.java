
/**
 * Representa una acción reversible dentro del sistema (patrón Command).
 * Cada vez que algo se reserva, se mete una Accion a la pila MiPila<Accion>
 * en SistemaReservas. Si el usuario quiere deshacer, se hace pop()
 * y se ejecuta deshacer().
 */
public class Accion {
    private final String descripcion;
    private final Runnable deshacer;

    public Accion(String descripcion, Runnable deshacer) {
        this.descripcion = descripcion;
        this.deshacer = deshacer;
    }

    public void deshacer() { deshacer.run(); }
    public String getDescripcion() { return descripcion; }
}
