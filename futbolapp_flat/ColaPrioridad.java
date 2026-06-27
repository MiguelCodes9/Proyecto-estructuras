
import java.util.Comparator;

/**
 * Cola de prioridad implementada como min-heap binario sobre un arreglo.
 * Se usa en el Matchmaking para sacar primero al jugador con mayor
 * prioridad (por ejemplo: el que tiene ELO más cercano al promedio
 * de la fila, o el que lleva más tiempo esperando).
 *
 * insertar: O(log n)
 * extraerMinimo: O(log n)
 * verMinimo: O(1)
 */
public class ColaPrioridad<T> {

    private Object[] datos;
    private int tamano;
    private final Comparator<T> comparador;

    public ColaPrioridad(Comparator<T> comparador) {
        this.comparador = comparador;
        this.datos = new Object[16];
        this.tamano = 0;
    }

    public void insertar(T elemento) {
        if (tamano == datos.length) crecer();
        datos[tamano] = elemento;
        flotar(tamano);
        tamano++;
    }

    public T extraerMinimo() {
        if (estaVacia()) throw new IllegalStateException("La cola de prioridad está vacía");
        @SuppressWarnings("unchecked")
        T minimo = (T) datos[0];
        tamano--;
        datos[0] = datos[tamano];
        datos[tamano] = null;
        hundir(0);
        return minimo;
    }

    @SuppressWarnings("unchecked")
    public T verMinimo() {
        if (estaVacia()) throw new IllegalStateException("La cola de prioridad está vacía");
        return (T) datos[0];
    }

    public boolean estaVacia() { return tamano == 0; }
    public int tamano() { return tamano; }

    // --- mecánica interna del heap ---

    @SuppressWarnings("unchecked")
    private void flotar(int i) {
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (comparador.compare((T) datos[i], (T) datos[padre]) < 0) {
                intercambiar(i, padre);
                i = padre;
            } else break;
        }
    }

    @SuppressWarnings("unchecked")
    private void hundir(int i) {
        while (true) {
            int izq = 2 * i + 1;
            int der = 2 * i + 2;
            int menor = i;

            if (izq < tamano && comparador.compare((T) datos[izq], (T) datos[menor]) < 0) menor = izq;
            if (der < tamano && comparador.compare((T) datos[der], (T) datos[menor]) < 0) menor = der;

            if (menor == i) break;
            intercambiar(i, menor);
            i = menor;
        }
    }

    private void intercambiar(int i, int j) {
        Object tmp = datos[i];
        datos[i] = datos[j];
        datos[j] = tmp;
    }

    private void crecer() {
        Object[] nuevo = new Object[datos.length * 2];
        System.arraycopy(datos, 0, nuevo, 0, datos.length);
        datos = nuevo;
    }
}
