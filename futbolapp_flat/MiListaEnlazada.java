
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Lista simplemente enlazada genérica, implementada desde cero.
 * Sirve como base conceptual (no se usa directamente como pila/cola,
 * esas tienen su propia implementación más abajo, pero comparten la
 * misma idea de nodo + puntero "siguiente").
 *
 * Complejidades:
 *  - insertarFinal: O(n)  (recorre hasta el final)
 *  - insertarInicio: O(1)
 *  - eliminar(dato): O(n)
 *  - obtener(indice): O(n)
 */
public class MiListaEnlazada<T> implements Iterable<T> {

    // Nodo interno: dato + referencia al siguiente nodo
    private class Nodo {
        T dato;
        Nodo siguiente;
        Nodo(T dato) { this.dato = dato; }
    }

    private Nodo cabeza;
    private Nodo cola;
    private int tamano;

    public void insertarFinal(T dato) {
        Nodo nuevo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            cola = nuevo;
        }
        tamano++;
    }

    public void insertarInicio(T dato) {
        Nodo nuevo = new Nodo(dato);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        if (cola == null) cola = nuevo;
        tamano++;
    }

    public boolean eliminar(T dato) {
        if (cabeza == null) return false;

        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            if (cabeza == null) cola = null;
            tamano--;
            return true;
        }

        Nodo actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.equals(dato)) {
                actual.siguiente = actual.siguiente.siguiente;
                if (actual.siguiente == null) cola = actual;
                tamano--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public boolean contiene(T dato) {
        for (T t : this) {
            if (t.equals(dato)) return true;
        }
        return false;
    }

    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++) actual = actual.siguiente;
        return actual.dato;
    }

    public int tamano() { return tamano; }
    public boolean estaVacia() { return tamano == 0; }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo actual = cabeza;

            @Override
            public boolean hasNext() { return actual != null; }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T dato = actual.dato;
                actual = actual.siguiente;
                return dato;
            }
        };
    }
}
