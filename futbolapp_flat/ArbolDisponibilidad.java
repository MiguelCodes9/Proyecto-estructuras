
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Árbol Binario de Búsqueda (BST) ordenado por la fecha/hora de inicio
 * de cada reserva. Cada cancha tiene SU propio árbol de disponibilidad.
 *
 * ¿Por qué un árbol y no una lista? Porque para verificar si un horario
 * está libre o para insertar una nueva reserva ordenada por tiempo,
 * un BST balanceado en promedio da O(log n), mientras que una lista
 * recorrida completa da O(n). Aquí no balanceamos automáticamente
 * (no es AVL/Rojo-Negro) para mantenerlo simple, pero la idea queda clara.
 *
 * Para detectar choques de horario, no basta con comparar el instante
 * de inicio: hay que comparar RANGOS (inicio, fin). Por eso al verificar
 * disponibilidad hacemos un recorrido in-order acotado, comparando
 * solapamiento de intervalos contra cada reserva existente.
 */
public class ArbolDisponibilidad {

    private class NodoArbol {
        Reserva reserva;
        NodoArbol izquierdo, derecho;
        NodoArbol(Reserva reserva) { this.reserva = reserva; }
    }

    private NodoArbol raiz;

    /** Inserta una reserva ya confirmada en el árbol, ordenada por su hora de inicio. */
    public void insertar(Reserva reserva) {
        raiz = insertarRec(raiz, reserva);
    }

    private NodoArbol insertarRec(NodoArbol nodo, Reserva reserva) {
        if (nodo == null) return new NodoArbol(reserva);

        if (reserva.getInicio().isBefore(nodo.reserva.getInicio())) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, reserva);
        } else {
            nodo.derecho = insertarRec(nodo.derecho, reserva);
        }
        return nodo;
    }

    /**
     * Verifica si el rango [inicio, fin) está libre, es decir que no
     * se solape con ninguna reserva existente en el árbol.
     */
    public boolean hayDisponibilidad(LocalDateTime inicio, LocalDateTime fin) {
        return hayDisponibilidadRec(raiz, inicio, fin);
    }

    private boolean hayDisponibilidadRec(NodoArbol nodo, LocalDateTime inicio, LocalDateTime fin) {
        if (nodo == null) return true;

        boolean seSolapan = inicio.isBefore(nodo.reserva.getFin()) && nodo.reserva.getInicio().isBefore(fin);
        if (seSolapan) return false;

        // Poda: si el rango pedido termina antes de que empiece este nodo,
        // solo hace falta mirar el subárbol izquierdo (reservas más tempranas).
        if (fin.isBefore(nodo.reserva.getInicio()) || fin.isEqual(nodo.reserva.getInicio())) {
            return hayDisponibilidadRec(nodo.izquierdo, inicio, fin);
        }
        // Si el rango pedido empieza después de que termina este nodo,
        // solo hace falta mirar el subárbol derecho (reservas más tardías).
        if (inicio.isAfter(nodo.reserva.getFin()) || inicio.isEqual(nodo.reserva.getFin())) {
            return hayDisponibilidadRec(nodo.derecho, inicio, fin);
        }
        // Caso límite: revisamos ambos lados por seguridad.
        return hayDisponibilidadRec(nodo.izquierdo, inicio, fin) && hayDisponibilidadRec(nodo.derecho, inicio, fin);
    }

    /** Elimina una reserva del árbol (por ejemplo, al cancelar/deshacer). */
    public void eliminar(Reserva reserva) {
        raiz = eliminarRec(raiz, reserva);
    }

    private NodoArbol eliminarRec(NodoArbol nodo, Reserva reserva) {
        if (nodo == null) return null;

        if (reserva.getInicio().isBefore(nodo.reserva.getInicio())) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, reserva);
        } else if (reserva.getInicio().isAfter(nodo.reserva.getInicio())) {
            nodo.derecho = eliminarRec(nodo.derecho, reserva);
        } else if (!reserva.getId().equals(nodo.reserva.getId())) {
            // Mismo instante de inicio pero distinta reserva (caso raro): buscamos en ambos lados.
            nodo.izquierdo = eliminarRec(nodo.izquierdo, reserva);
            nodo.derecho = eliminarRec(nodo.derecho, reserva);
        } else {
            // Nodo encontrado: aplicamos el borrado clásico de BST.
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;

            NodoArbol sucesor = nodo.derecho;
            while (sucesor.izquierdo != null) sucesor = sucesor.izquierdo;
            nodo.reserva = sucesor.reserva;
            nodo.derecho = eliminarRec(nodo.derecho, sucesor.reserva);
        }
        return nodo;
    }

    /** Devuelve todas las reservas ordenadas cronológicamente (recorrido in-order). */
    public List<Reserva> listarReservas() {
        List<Reserva> resultado = new ArrayList<>();
        inOrder(raiz, resultado);
        return resultado;
    }

    private void inOrder(NodoArbol nodo, List<Reserva> resultado) {
        if (nodo == null) return;
        inOrder(nodo.izquierdo, resultado);
        resultado.add(nodo.reserva);
        inOrder(nodo.derecho, resultado);
    }
}
