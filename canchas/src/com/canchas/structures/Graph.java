package com.canchas.structures;

/**
 * Grafo Ponderado no dirigido para gestionar las distancias entre sedes de canchas.
 *
 * Implementación desde cero usando Matriz de Adyacencia y arreglos primitivos,
 * sin usar colecciones estándar de Java (ArrayList, HashMap, PriorityQueue, etc.).
 *
 * Algoritmo de Dijkstra para cálculo de caminos mínimos entre sedes.
 */
public class Graph {

    // ==========================================
    // ATRIBUTOS
    // ==========================================

    /** Nombres de los vértices (sedes). */
    private String[] vertices;

    /** Pesos de las aristas. 0.0 = sin conexión directa. */
    private double[][] matrizAdyacencia;

    /** Número de vértices registrados actualmente. */
    private int numVertices;

    /** Capacidad máxima de sedes que puede contener el grafo. */
    private int capacidadMax;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    /**
     * Crea un grafo vacío con capacidad parametrizable.
     * @param capacidadMax número máximo de sedes permitidas.
     */
    public Graph(int capacidadMax) {
        this.capacidadMax = capacidadMax;
        this.vertices = new String[capacidadMax];
        this.matrizAdyacencia = new double[capacidadMax][capacidadMax];
        this.numVertices = 0;

        for (int i = 0; i < capacidadMax; i++) {
            for (int j = 0; j < capacidadMax; j++) {
                matrizAdyacencia[i][j] = 0.0;
            }
        }
    }

    // ==========================================
    // MÉTODOS PÚBLICOS
    // ==========================================

    /**
     * Agrega una nueva sede como vértice en el grafo.
     * @param nombre nombre de la sede.
     * @return true si fue agregada, false si ya existía o se superó la capacidad.
     */
    public boolean agregarVertice(String nombre) {
        if (numVertices >= capacidadMax) {
            System.out.println("-> [GRAFO ERROR]: Se ha alcanzado la capacidad máxima de sedes (" + capacidadMax + ").");
            return false;
        }
        if (obtenerIndice(nombre) != -1) {
            System.out.println("-> [GRAFO AVISO]: La sede '" + nombre + "' ya existe en el grafo.");
            return false;
        }
        vertices[numVertices] = nombre;
        numVertices++;
        return true;
    }

    /**
     * Conecta dos sedes de forma bidireccional con una distancia dada en km.
     * @param origen  nombre de la sede de origen.
     * @param destino nombre de la sede de destino.
     * @param peso    distancia en km entre ambas sedes.
     * @return true si la arista fue creada, false si alguna sede no existe.
     */
    public boolean agregarArista(String origen, String destino, double peso) {
        int idxOrigen  = obtenerIndice(origen);
        int idxDestino = obtenerIndice(destino);

        if (idxOrigen == -1 || idxDestino == -1) {
            System.out.println("-> [GRAFO ERROR]: Una o ambas sedes no existen (" + origen + ", " + destino + ").");
            return false;
        }
        if (peso <= 0) {
            System.out.println("-> [GRAFO ERROR]: La distancia debe ser mayor a 0 km.");
            return false;
        }

        matrizAdyacencia[idxOrigen][idxDestino] = peso;
        matrizAdyacencia[idxDestino][idxOrigen] = peso; // Grafo no dirigido (bidireccional)
        return true;
    }

    /**
     * Retorna una copia del arreglo con los nombres de las sedes registradas.
     */
    public String[] getVertices() {
        String[] copia = new String[numVertices];
        System.arraycopy(vertices, 0, copia, 0, numVertices);
        return copia;
    }

    /**
     * Busca el índice de un vértice por su nombre (case-insensitive).
     * @return índice del vértice, o -1 si no existe.
     */
    public int obtenerIndice(String nombre) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Calcula la ruta más corta entre dos sedes usando el algoritmo de Dijkstra.
     * Utiliza arreglos primitivos como estructuras auxiliares (sin colecciones estándar).
     *
     * @param origen  nombre de la sede de partida.
     * @param destino nombre de la sede de llegada.
     * @return un {@link ResultadoRuta} con la distancia total y el camino como ListaEnlazada.
     */
    public ResultadoRuta calcularRutaMasCorta(String origen, String destino) {
        int idxOrigen  = obtenerIndice(origen);
        int idxDestino = obtenerIndice(destino);

        if (idxOrigen == -1 || idxDestino == -1) {
            System.out.println("-> [GRAFO ERROR]: Sede de origen o destino no encontrada.");
            return new ResultadoRuta(null, 0.0);
        }

        // Arreglos auxiliares del algoritmo
        double[]  distancias = new double[numVertices];  // Distancia mínima conocida desde el origen
        boolean[] visitado   = new boolean[numVertices]; // Marca si el nodo ya fue procesado
        int[]     anterior   = new int[numVertices];     // Nodo predecesor para reconstruir la ruta

        for (int i = 0; i < numVertices; i++) {
            distancias[i] = Double.MAX_VALUE;
            visitado[i]   = false;
            anterior[i]   = -1;
        }
        distancias[idxOrigen] = 0.0;

        // Iteración principal de Dijkstra: V iteraciones donde V = número de vértices
        for (int iter = 0; iter < numVertices; iter++) {

            // Paso 1: seleccionar el nodo no visitado con la menor distancia acumulada
            int u = -1;
            double minDist = Double.MAX_VALUE;
            for (int j = 0; j < numVertices; j++) {
                if (!visitado[j] && distancias[j] < minDist) {
                    minDist = distancias[j];
                    u = j;
                }
            }

            // Si no hay nodo alcanzable o ya llegamos al destino, detener
            if (u == -1 || u == idxDestino) break;

            visitado[u] = true;

            // Paso 2: relajar las aristas de los vecinos de u
            for (int v = 0; v < numVertices; v++) {
                double peso = matrizAdyacencia[u][v];
                if (peso > 0 && !visitado[v]) {
                    double nuevaDist = distancias[u] + peso;
                    if (nuevaDist < distancias[v]) {
                        distancias[v] = nuevaDist;
                        anterior[v]   = u;
                    }
                }
            }
        }

        // Si el destino sigue inalcanzable, no hay camino
        if (distancias[idxDestino] == Double.MAX_VALUE) {
            return new ResultadoRuta(null, 0.0);
        }

        // Reconstrucción del camino desde destino → origen, insertando al inicio de la lista
        ListaEnlazada<String> camino = new ListaEnlazada<>();
        for (int nodo = idxDestino; nodo != -1; nodo = anterior[nodo]) {
            camino.addFirst(vertices[nodo]);
        }

        return new ResultadoRuta(camino, distancias[idxDestino]);
    }

    /**
     * Imprime la matriz de adyacencia completa del grafo en consola.
     */
    public void imprimirGrafo() {
        System.out.println("\n===== MATRIZ DE ADYACENCIA - SEDES DE CANCHAS =====");
        System.out.printf("%-16s", "");
        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%-16s", vertices[i]);
        }
        System.out.println();

        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%-16s", vertices[i]);
            for (int j = 0; j < numVertices; j++) {
                if (matrizAdyacencia[i][j] == 0.0) {
                    System.out.printf("%-16s", "-");
                } else {
                    System.out.printf("%-16s", matrizAdyacencia[i][j] + " km");
                }
            }
            System.out.println();
        }
        System.out.println("====================================================");
        System.out.println("Sedes registradas: " + numVertices + " | Capacidad máxima: " + capacidadMax);
    }

    // ==========================================
    // CLASE INTERNA: RESULTADO DE DIJKSTRA
    // ==========================================

    /**
     * Encapsula el resultado del algoritmo de Dijkstra:
     * la ruta mínima como ListaEnlazada y la distancia total en km.
     */
    public static class ResultadoRuta {

        /** Camino minimo como lista enlazada de nombres de sedes (origen -> destino). */
        public ListaEnlazada<String> camino;

        /** Distancia total acumulada del camino mínimo en km. */
        public double distanciaTotal;

        public ResultadoRuta(ListaEnlazada<String> camino, double distanciaTotal) {
            this.camino = camino;
            this.distanciaTotal = distanciaTotal;
        }

        @Override
        public String toString() {
            if (camino == null || camino.isEmpty()) {
                return "No existe una ruta disponible entre las sedes seleccionadas.";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Distancia total : ").append(String.format("%.2f", distanciaTotal)).append(" km\n");
            sb.append("Ruta más corta  : ");

            Nodo<String> actual = camino.head;
            while (actual != null) {
                sb.append(actual.data);
                if (actual.next != null) sb.append(" -> ");
                actual = actual.next;
            }
            return sb.toString();
        }
    }
}
