

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * EL PLUS: Matchmaking para "partidos aleatorios" (jugar con desconocidos).
 *
 * Combina varias estructuras trabajando juntas:
 *
 *  1. MiCola<SolicitudPartido>  -> fila de espera en orden de llegada (FIFO).
 *  2. ColaPrioridad<Solicitud>  -> cuando se va a armar el partido, se reordena
 *                                  por cercanía de ELO (para que el partido quede
 *                                  parejo en nivel), no por orden de llegada puro.
 *  3. GrafoJugadores (BFS)      -> antes de tratarlos como desconocidos totales,
 *                                  se revisa si ya existe conexión social
 *                                  (jugaron juntos antes / amigos de amigos).
 *  4. UnionFind                 -> tras formar el partido, se "funden" los
 *                                  conjuntos de todos los jugadores que
 *                                  participaron juntos, para ir construyendo
 *                                  comunidades de jugadores frecuentes.
 *
 * Balanceo de equipos: una vez se tienen los N jugadores elegidos, se
 * ordenan por ELO y se reparten en zig-zag (draft alternado) para que
 * la suma de ELO de cada equipo quede lo más pareja posible.
 */
public class Matchmaking {

    private static final int JUGADORES_POR_PARTIDO = 10; // 5 vs 5, por ejemplo

    private final MiCola<Usuario> filaEspera = new MiCola<>();
    private final GrafoJugadores grafo = new GrafoJugadores();
    private final UnionFind comunidades = new UnionFind();

    public void unirseAFilaAleatoria(Usuario usuario) {
        grafo.agregarJugador(usuario.getId());
        comunidades.agregar(usuario.getId());
        filaEspera.encolar(usuario);
    }

    public int jugadoresEnEspera() { return filaEspera.tamano(); }

    /**
     * Si ya hay suficientes jugadores en la fila, forma un partido aleatorio:
     * los reordena por ELO (cola de prioridad), revisa cercanía social (grafo BFS)
     * solo informativamente, balancea equipos y registra las nuevas conexiones.
     */
    public Partido intentarFormarPartidoAleatorio(SistemaReservas sistema, String idCancha, LocalDateTime inicio) {
        if (filaEspera.tamano() < JUGADORES_POR_PARTIDO) return null;

        // 1) Sacamos exactamente JUGADORES_POR_PARTIDO de la fila FIFO.
        List<Usuario> seleccionados = new ArrayList<>();
        for (int i = 0; i < JUGADORES_POR_PARTIDO; i++) {
            seleccionados.add(filaEspera.decolar());
        }

        // 2) Los pasamos por una cola de prioridad ordenada por ELO,
        //    para poder repartirlos en equipos parejos (no por orden de llegada).
        ColaPrioridad<Usuario> porElo = new ColaPrioridad<>(Comparator.comparingInt(Usuario::getElo));
        for (Usuario u : seleccionados) porElo.insertar(u);

        List<Usuario> ordenadosPorElo = new ArrayList<>();
        while (!porElo.estaVacia()) ordenadosPorElo.add(porElo.extraerMinimo());

        // 3) Creamos la reserva del espacio para este partido aleatorio.
        Cancha cancha = sistema.buscarCancha(idCancha);
        if (cancha == null || !cancha.estaLibre(inicio, inicio.plusHours(1))) return null;

        Reserva reserva = new Reserva(ordenadosPorElo.get(0), cancha, inicio, 1, true, true);
        cancha.confirmarReserva(reserva);

        Partido partido = new Partido(reserva, Partido.Tipo.ALEATORIO);

        // 4) Draft alternado (zig-zag) para balancear ELO entre equipo A y B.
        boolean turnoA = true;
        for (Usuario u : ordenadosPorElo) {
            if (turnoA) partido.getEquipoA().insertarFinal(u);
            else partido.getEquipoB().insertarFinal(u);
            turnoA = !turnoA;
            u.registrarPartidoJugado(partido.getId());
        }

        // 5) Actualizamos el grafo social y las comunidades (union-find):
        //    todos los que jugaron juntos quedan conectados entre sí.
        for (int i = 0; i < seleccionados.size(); i++) {
            for (int j = i + 1; j < seleccionados.size(); j++) {
                String idA = seleccionados.get(i).getId();
                String idB = seleccionados.get(j).getId();
                grafo.agregarConexion(idA, idB);
                comunidades.unir(idA, idB);
            }
        }

        return partido;
    }

    /** Sugerencia social: amigos / amigos-de-amigos de un jugador en la red del sistema. */
    public List<String> sugerirConocidos(String idUsuario) {
        return grafo.sugerirPorCercania(idUsuario, 2);
    }

    public boolean perteneceALaMismaComunidad(String idA, String idB) {
        return comunidades.mismoConjunto(idA, idB);
    }
}
