
import java.util.HashMap;
import java.util.Map;

/**
 * Union-Find (Conjuntos Disjuntos) con compresión de camino y unión por rango.
 *
 * Uso en el sistema: agrupar jugadores en "comunidades" según con quién
 * ya han jugado (cada vez que dos jugadores comparten un partido, se
 * unen sus conjuntos). Esto permite, por ejemplo, saber rápidamente si
 * dos jugadores ya pertenecen al mismo círculo social sin recorrer
 * todo el grafo, en casi O(1) amortizado.
 */
public class UnionFind {

    private final Map<String, String> padre = new HashMap<>();
    private final Map<String, Integer> rango = new HashMap<>();

    /** Registra un jugador nuevo como su propio conjunto. */
    public void agregar(String idJugador) {
        padre.putIfAbsent(idJugador, idJugador);
        rango.putIfAbsent(idJugador, 0);
    }

    /** Encuentra el representante (raíz) del conjunto de un jugador, con compresión de camino. */
    public String encontrar(String idJugador) {
        agregar(idJugador);
        String p = padre.get(idJugador);
        if (!p.equals(idJugador)) {
            p = encontrar(p);          // recursión hacia la raíz
            padre.put(idJugador, p);   // compresión de camino: apunta directo a la raíz
        }
        return p;
    }

    /** Une los conjuntos de dos jugadores (por ejemplo, jugaron juntos en un partido). */
    public void unir(String idA, String idB) {
        String raizA = encontrar(idA);
        String raizB = encontrar(idB);
        if (raizA.equals(raizB)) return;

        int rangoA = rango.get(raizA);
        int rangoB = rango.get(raizB);

        if (rangoA < rangoB) {
            padre.put(raizA, raizB);
        } else if (rangoA > rangoB) {
            padre.put(raizB, raizA);
        } else {
            padre.put(raizB, raizA);
            rango.put(raizA, rangoA + 1);
        }
    }

    /** ¿Pertenecen al mismo grupo / comunidad de jugadores? */
    public boolean mismoConjunto(String idA, String idB) {
        return encontrar(idA).equals(encontrar(idB));
    }
}
