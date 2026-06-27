
import java.time.LocalDateTime;

public class Cancha {
    private final String id;
    private final String nombre;
    private final String ubicacion;
    private final double precioPorHora;
    private final ArbolDisponibilidad disponibilidad; // cada cancha tiene su propio árbol de horarios

    public Cancha(String id, String nombre, String ubicacion, double precioPorHora) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.precioPorHora = precioPorHora;
        this.disponibilidad = new ArbolDisponibilidad();
    }

    public boolean estaLibre(LocalDateTime inicio, LocalDateTime fin) {
        return disponibilidad.hayDisponibilidad(inicio, fin);
    }

    public void confirmarReserva(Reserva reserva) {
        disponibilidad.insertar(reserva);
    }

    public void liberarReserva(Reserva reserva) {
        disponibilidad.eliminar(reserva);
    }

    public ArbolDisponibilidad getDisponibilidad() { return disponibilidad; }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public double getPrecioPorHora() { return precioPorHora; }

    @Override
    public String toString() {
        return nombre + " - " + ubicacion + " ($" + precioPorHora + "/hora)";
    }
}
