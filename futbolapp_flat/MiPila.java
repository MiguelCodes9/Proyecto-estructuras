
/**
 * Pila (LIFO) implementada con nodos enlazados propios.
 * Uso en el sistema: historial de acciones para poder "deshacer"
 * la última reserva, el último jugador agregado, etc.
 *
 * push, pop, peek -> O(1)
 */
public class MiPila<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;
        Nodo(T dato) { this.dato = dato; }
    }

    private Nodo tope;
    private int tamano;

    public void push(T dato) {
        Nodo nuevo = new Nodo(dato);
        nuevo.siguiente = tope;
        tope = nuevo;
        tamano++;
    }

    public T pop() {
        if (estaVacia()) throw new IllegalStateException("La pila está vacía");
        T dato = tope.dato;
        tope = tope.siguiente;
        tamano--;
        return dato;
    }

    public T peek() {
        if (estaVacia()) throw new IllegalStateException("La pila está vacía");
        return tope.dato;
    }

    public boolean estaVacia() { return tope == null; }
    public int tamano() { return tamano; }
}
