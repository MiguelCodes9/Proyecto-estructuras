
import java.util.*;

/**
 * Grafo NO dirigido de jugadores, implementado como lista de adyacencia
 * (HashMap<idJugador, lista de vecinos>). Una arista A-B significa
 * "A y B ya jugaron juntos alguna vez" (o se calificaron bien mutuamente).
 *
 * Uso principal: cuando alguien entra a la cola de "partido aleatorio",
 * usamos BFS desde su nodo para encontrar jugadores cercanos en la red
 * social del sistema (amigos directos = distancia 1, amigos de amigos =
 * distancia 2) y darles prioridad sobre desconocidos totales.
 */
public class GrafoJugadores {

    private final Map<String, List<String>> adyacencia = new HashMap<>();

    public void agregarJugador(String idJugador) {
        adyacencia.putIfAbsent(idJugador, new ArrayList<>());
    }

    /** Crea una conexión bidireccional: "estos dos ya jugaron juntos". */
    public void agregarConexion(String idA, String idB) {
        agregarJugador(idA);
        agregarJugador(idB);
        if (!adyacencia.get(idA).contains(idB)) adyacencia.get(idA).add(idB);
        if (!adyacencia.get(idB).contains(idA)) adyacencia.get(idB).add(idA);
    }

    /**
     * BFS: devuelve los jugadores alcanzables desde idOrigen hasta una
     * distancia máxima (por ejemplo 2 = "amigos de mis amigos"),
     * sin incluir al propio jugador de origen.
     */
    public List<String> sugerirPorCercania(String idOrigen, int distanciaMaxima) {
        List<String> resultado = new ArrayList<>();
        if (!adyacencia.containsKey(idOrigen)) return resultado;

        Map<String, Integer> distancia = new HashMap<>();
        Queue<String> cola = new LinkedList<>(); // cola auxiliar estándar para el recorrido BFS
        distancia.put(idOrigen, 0);
        cola.add(idOrigen);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            int distActual = distancia.get(actual);
            if (distActual >= distanciaMaxima) continue;

            for (String vecino : adyacencia.get(actual)) {
                if (!distancia.containsKey(vecino)) {
                    distancia.put(vecino, distActual + 1);
                    resultado.add(vecino);
                    cola.add(vecino);
                }
            }
        }
        return resultado;
    }

    /** DFS recursivo: útil para detectar si un grupo completo está conectado entre sí. */
    public Set<String> dfsComponente(String idOrigen) {
        Set<String> visitados = new HashSet<>();
        dfsRec(idOrigen, visitados);
        return visitados;
    }

    private void dfsRec(String actual, Set<String> visitados) {
        if (visitados.contains(actual) || !adyacencia.containsKey(actual)) return;
        visitados.add(actual);
        for (String vecino : adyacencia.get(actual)) {
            dfsRec(vecino, visitados);
        }
    }

    public List<String> vecinosDirectos(String idJugador) {
        return adyacencia.getOrDefault(idJugador, new ArrayList<>());
    }
}
