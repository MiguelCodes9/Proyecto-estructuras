

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Orquesta el flujo de "rentar el espacio de juego" (partido privado).
 *
 * Estructuras usadas aquí:
 *  - HashMap<String, Usuario/Cancha/Partido>: acceso O(1) por id.
 *  - MiPila<Accion>: pila de deshacer para revertir la última operación.
 *  - Cancha internamente usa su ArbolDisponibilidad (BST) para validar horarios.
 */
public class SistemaReservas {

    private final Map<String, Usuario> usuarios = new HashMap<>();
    private final Map<String, Cancha> canchas = new HashMap<>();
    private final Map<String, Partido> partidos = new HashMap<>();
    private final MiPila<Accion> historialAcciones = new MiPila<>();

    // --- registro básico ---

    public Usuario registrarUsuario(String id, String nombre) {
        Usuario usuario = new Usuario(id, nombre);
        usuarios.put(id, usuario);
        return usuario;
    }

    public Cancha registrarCancha(String id, String nombre, String ubicacion, double precioPorHora) {
        Cancha cancha = new Cancha(id, nombre, ubicacion, precioPorHora);
        canchas.put(id, cancha);
        return cancha;
    }

    public Usuario buscarUsuario(String id) { return usuarios.get(id); }
    public Cancha buscarCancha(String id) { return canchas.get(id); }
    public Map<String, Cancha> getCanchas() { return canchas; }

    /**
     * Intenta programar un partido privado. Devuelve el Partido creado,
     * o null si no hay disponibilidad en ese horario.
     */
    public Partido solicitarPartido(String idUsuario, String idCancha, LocalDateTime inicio,
                                     int duracionHoras, boolean incluyeBalon, boolean incluyeHidratacion) {
        Usuario usuario = usuarios.get(idUsuario);
        Cancha cancha = canchas.get(idCancha);
        if (usuario == null || cancha == null) {
            throw new IllegalArgumentException("Usuario o cancha no existen.");
        }

        LocalDateTime fin = inicio.plusHours(duracionHoras);
        if (!cancha.estaLibre(inicio, fin)) {
            return null; // no hay disponibilidad
        }

        Reserva reserva = new Reserva(usuario, cancha, inicio, duracionHoras, incluyeBalon, incluyeHidratacion);
        cancha.confirmarReserva(reserva);

        Partido partido = new Partido(reserva, Partido.Tipo.PRIVADO);
        partidos.put(partido.getId(), partido);
        usuario.registrarPartidoJugado(partido.getId());

        // Guardamos cómo deshacer esta acción exacta (cerrando sobre las variables locales).
        historialAcciones.push(new Accion(
            "Reserva de " + usuario.getNombre() + " en " + cancha.getNombre() + " (" + inicio + ")",
            () -> {
                cancha.liberarReserva(reserva);
                partidos.remove(partido.getId());
            }
        ));

        return partido;
    }

    /** Deshace la última acción realizada en el sistema (la última reserva). */
    public String deshacerUltimaAccion() {
        if (historialAcciones.estaVacia()) {
            return "No hay acciones para deshacer.";
        }
        Accion accion = historialAcciones.pop();
        accion.deshacer();
        return "Se deshizo: " + accion.getDescripcion();
    }

    public Partido buscarPartido(String idPartido) { return partidos.get(idPartido); }
}
