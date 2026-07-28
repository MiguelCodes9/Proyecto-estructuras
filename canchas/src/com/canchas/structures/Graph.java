package com.canchas.structures;

/**
 * Grafo ponderado 
 * Algoritmo de dijkstra para cálculo de caminos mínimos entre sedes
 */
public class Graph {

    // ATRIBUTOS
    /* nombres de los vértices */
    private String[] vertices;

    /** pesos de las aristas. 0.0 = sin conexión directa. */
    private double[][] matrizAdyacencia;

    /** numero de vertices registrados actualmente. */
    private int numVertices;

    /** capacidad máxima de sedes que puede contener el grafo. */
    private int capacidadMax;

    // CONSTRUCTOR

    /**
     * creamos un grafo vacío con capacidad parametrizable.
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
    // MÉTODOS PÚBLICOS
    /**
     * agrega una nueva sede como vértice en el grafo
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
        matrizAdyacencia[idxDestino][idxOrigen] = peso;
        return true;
    }

    /**
     * retorna una copia del arreglo con los nombres de las sedes registradas
     */
    public String[] getVertices() {
        String[] copia = new String[numVertices];
        System.arraycopy(vertices, 0, copia, 0, numVertices);
        return copia;
    }

    /**
     * busca el índice de un vértice por su nombre
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
     * calcula la ruta más corta entre dos sedes usando el algoritmo de Dijkstra
     * @param origen  nombre de la sede de partida
     * @param destino nombre de la sede de llegada
     * @return un {@link ResultadoRuta} con la distancia total y el camino como ListaEnlazada
     */
    public ResultadoRuta calcularRutaMasCorta(String origen, String destino) {
        int idxOrigen  = obtenerIndice(origen);
        int idxDestino = obtenerIndice(destino);

        if (idxOrigen == -1 || idxDestino == -1) {
            System.out.println("-> [GRAFO ERROR]: Sede de origen o destino no encontrada.");
            return new ResultadoRuta(null, 0.0);
        }

        double[]  distancias = new double[numVertices];  
        boolean[] visitado   = new boolean[numVertices]; 
        int[]     anterior   = new int[numVertices];     

        for (int i = 0; i < numVertices; i++) {
            distancias[i] = Double.MAX_VALUE;
            visitado[i]   = false;
            anterior[i]   = -1;
        }
        distancias[idxOrigen] = 0.0;

        for (int iter = 0; iter < numVertices; iter++) {

            int u = -1;
            double minDist = Double.MAX_VALUE;
            for (int j = 0; j < numVertices; j++) {
                if (!visitado[j] && distancias[j] < minDist) {
                    minDist = distancias[j];
                    u = j;
                }
            }

            if (u == -1 || u == idxDestino) break;

            visitado[u] = true;

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

        // si el destino sigue inalcanzable, no hay camino
        if (distancias[idxDestino] == Double.MAX_VALUE) {
            return new ResultadoRuta(null, 0.0);
        }

        ListaEnlazada<String> camino = new ListaEnlazada<>();
        for (int nodo = idxDestino; nodo != -1; nodo = anterior[nodo]) {
            camino.addFirst(vertices[nodo]);
        }

        return new ResultadoRuta(camino, distancias[idxDestino]);
    }

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

    public static class ResultadoRuta {

        public ListaEnlazada<String> camino;


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
