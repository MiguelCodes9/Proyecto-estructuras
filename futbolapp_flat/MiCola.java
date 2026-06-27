
/**
 * Cola (FIFO) implementada con nodos enlazados propios, con
 * puntero a frente y a final para que encolar/decolar sea O(1).
 *
 * Uso en el sistema: fila de espera de jugadores que quieren
 * un "partido aleatorio" (orden de llegada).
 */
public class MiCola<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;
        Nodo(T dato) { this.dato = dato; }
    }

    private Nodo frente;
    private Nodo final_;
    private int tamano;

    public void encolar(T dato) {
        Nodo nuevo = new Nodo(dato);
        if (frente == null) {
            frente = nuevo;
            final_ = nuevo;
        } else {
            final_.siguiente = nuevo;
            final_ = nuevo;
        }
        tamano++;
    }

    public T decolar() {
        if (estaVacia()) throw new IllegalStateException("La cola está vacía");
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) final_ = null;
        tamano--;
        return dato;
    }

    public T verFrente() {
        if (estaVacia()) throw new IllegalStateException("La cola está vacía");
        return frente.dato;
    }

    public boolean estaVacia() { return frente == null; }
    public int tamano() { return tamano; }
}
