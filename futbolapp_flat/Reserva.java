
import java.time.LocalDateTime;
import java.util.UUID;

public class Reserva {
    private final String id;
    private final Usuario usuario;
    private final Cancha cancha;
    private final LocalDateTime inicio;
    private final int duracionHoras;
    private final boolean incluyeBalon;
    private final boolean incluyeHidratacion;

    public Reserva(Usuario usuario, Cancha cancha, LocalDateTime inicio, int duracionHoras,
                    boolean incluyeBalon, boolean incluyeHidratacion) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.usuario = usuario;
        this.cancha = cancha;
        this.inicio = inicio;
        this.duracionHoras = duracionHoras;
        this.incluyeBalon = incluyeBalon;
        this.incluyeHidratacion = incluyeHidratacion;
    }

    public LocalDateTime getFin() { return inicio.plusHours(duracionHoras); }

    public double calcularCosto() {
        double costo = cancha.getPrecioPorHora() * duracionHoras;
        if (incluyeBalon) costo += 5000;          // costo plano de ejemplo (pesos COP)
        if (incluyeHidratacion) costo += 8000;    // costo plano de ejemplo (pesos COP)
        return costo;
    }

    public String getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Cancha getCancha() { return cancha; }
    public LocalDateTime getInicio() { return inicio; }
    public int getDuracionHoras() { return duracionHoras; }
    public boolean isIncluyeBalon() { return incluyeBalon; }
    public boolean isIncluyeHidratacion() { return incluyeHidratacion; }

    @Override
    public String toString() {
        return String.format(
            "Reserva[%s] %s en %s | %s, %dh | Balón:%s Hidratación:%s | Total: $%.0f",
            id, usuario.getNombre(), cancha.getNombre(), inicio, duracionHoras,
            incluyeBalon ? "Sí" : "No", incluyeHidratacion ? "Sí" : "No", calcularCosto()
        );
    }
}
