
import java.util.UUID;

public class Partido {

    public enum Tipo { PRIVADO, ALEATORIO }

    private final String id;
    private final Reserva reserva;
    private final Tipo tipo;
    private final MiListaEnlazada<Usuario> equipoA;
    private final MiListaEnlazada<Usuario> equipoB;

    public Partido(Reserva reserva, Tipo tipo) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.reserva = reserva;
        this.tipo = tipo;
        this.equipoA = new MiListaEnlazada<>();
        this.equipoB = new MiListaEnlazada<>();
    }

    public String getId() { return id; }
    public Reserva getReserva() { return reserva; }
    public Tipo getTipo() { return tipo; }
    public MiListaEnlazada<Usuario> getEquipoA() { return equipoA; }
    public MiListaEnlazada<Usuario> getEquipoB() { return equipoB; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Partido [").append(id).append("] (").append(tipo).append(") ===\n");
        sb.append(reserva).append("\n");
        sb.append("Lugar: ").append(reserva.getCancha()).append("\n");
        if (tipo == Tipo.ALEATORIO) {
            sb.append("Equipo A: ");
            for (Usuario u : equipoA) sb.append(u.getNombre()).append(" ");
            sb.append("\nEquipo B: ");
            for (Usuario u : equipoB) sb.append(u.getNombre()).append(" ");
        }
        return sb.toString();
    }
}
