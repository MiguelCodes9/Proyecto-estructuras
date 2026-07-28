package com.canchas.structures;

/**
 * Representa un Grafo Ponderado no dirigido para gestionar las distancias entre las sedes de las canchas.
 * Utiliza una Matriz de Adyacencia y arreglos primitivos para evitar el uso de colecciones estándar de Java.
 */
public class Graph {

    private String[] vertices;
    private double[][] matrizAdyacencia;
    private int numVertices;
    private int capacidadMax;

    public Graph(int capacidadMax) {
        this.capacidadMax = capacidadMax;
        this.vertices = new String[capacidadMax];
        this.matrizAdyacencia = new double[capacidadMax][capacidadMax];
        this.numVertices = 0;

        // Inicializar matriz con 0 (sin conexión directa)
        for (int i = 0; i < capacidadMax; i++) {
            for (int j = 0; j < capacidadMax; j++) {
                matrizAdyacencia[i][j] = 0.0;
            }
        }
    }

    /**
     * Agrega una nueva sede como vértice en el grafo.
     */
    public boolean agregarVertice(String nombre) {
        if (numVertices >= capacidadMax) {
            System.out.println("-> [GRAFO ERROR]: Se ha alcanzado la capacidad máxima de sedes.");
            return false;
        }
        if (obtenerIndice(nombre) != -1) {
            // Ya existe
            return false;
        }
        vertices[numVertices] = nombre;
        numVertices++;
        return true;
    }

    /**
     * Conecta dos sedes de forma bidireccional indicando la distancia en km.
     */
    public boolean agregarArista(String origen, String destino, double peso) {
        int indiceOrigen = obtenerIndice(origen);
        int indiceDestino = obtenerIndice(destino);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            System.out.println("-> [GRAFO ERROR]: Una o ambas sedes no existen en el grafo (" + origen + ", " + destino + ").");
            return false;
        }

        matrizAdyacencia[indiceOrigen][indiceDestino] = peso;
        matrizAdyacencia[indiceDestino][indiceOrigen] = peso; // Bidireccional
        return true;
    }

    /**
     * Busca el índice correspondiente de un vértice por su nombre.
     * Retorna -1 si no se encuentra.
     */
    public int obtenerIndice(String nombre) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    public String[] getVertices() {
        // Retorna una copia de los vértices existentes hasta el momento
        String[] verticesExistentes = new String[numVertices];
        System.arraycopy(vertices, 0, verticesExistentes, 0, numVertices);
        return verticesExistentes;
    }

    /**
     * Estructura interna para retornar el resultado de Dijkstra.
     */
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
                return "No existe una ruta disponible.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Distancia total: ").append(String.format("%.2f", distanciaTotal)).append(" km\nRuta: ");
            Nodo<String> actual = camino.head;
            while (actual != null) {
                sb.append(actual.data);
                if (actual.next != null) {
                    sb.append(" -> ");
                }
                actual = actual.next;
            }
            return sb.toString();
        }
    }

    /**
     * Algoritmo de Dijkstra para calcular la ruta más corta entre dos sedes.
     */
    public ResultadoRuta calcularRutaMasCorta(String origen, String destino) {
        int idxOrigen = obtenerIndice(origen);
        int idxDestino = obtenerIndice(destino);

        if (idxOrigen == -1 || idxDestino == -1) {
            return new ResultadoRuta(null, 0.0);
        }

        double[] distancias = new double[numVertices];
        boolean[] visitado = new boolean[numVertices];
        int[] anterior = new int[numVertices];

        // Inicializar arreglos auxiliares
        for (int i = 0; i < numVertices; i++) {
            distancias[i] = Double.MAX_VALUE;
            visitado[i] = false;
            anterior[i] = -1;
        }

        // Nodo inicial
        distancias[idxOrigen] = 0.0;

        for (int i = 0; i < numVertices; i++) {
            // 1. Encontrar el nodo no visitado con distancia mínima
            int u = -1;
            double minDist = Double.MAX_VALUE;
            for (int j = 0; j < numVertices; j++) {
                if (!visitado[j] && distancias[j] < minDist) {
                    minDist = distancias[j];
                    u = j;
                }
            }

            // Si todos los alcanzables fueron visitados
            if (u == -1 || distancias[u] == Double.MAX_VALUE) {
                break;
            }

            visitado[u] = true;

            // Si llegamos al destino, podemos detener el cálculo antes si solo queremos este destino
            if (u == idxDestino) {
                break;
            }

            // 2. Actualizar las distancias de los vecinos de u
            for (int v = 0; v < numVertices; v++) {
                double pesoArista = matrizAdyacencia[u][v];
                if (pesoArista > 0 && !visitado[v]) {
                    double nuevaDist = distancias[u] + pesoArista;
                    if (nuevaDist < distancias[v]) {
                        distancias[v] = nuevaDist;
                        anterior[v] = u;
                    }
                }
            }
        }

        // Si la distancia al destino sigue siendo Double.MAX_VALUE, no hay conexión
        if (distancias[idxDestino] == Double.MAX_VALUE) {
            return new ResultadoRuta(null, 0.0);
        }

        // Reconstrucción del camino usando la ListaEnlazada del proyecto
        ListaEnlazada<String> camino = new ListaEnlazada<>();
        int actual = idxDestino;
        while (actual != -1) {
            camino.addFirst(vertices[actual]); // Inserta al inicio para conservar el orden origen -> destino
            actual = anterior[actual];
        }

        return new ResultadoRuta(camino, distancias[idxDestino]);
    }

    /**
     * Imprime la matriz de adyacencia del grafo para depuración.
     */
    public void imprimirGrafo() {
        System.out.println("\n===== MATRIZ DE ADYACENCIA DEL GRAFO DE SEDES =====");
        System.out.print(String.format("%-15s", ""));
        for (int i = 0; i < numVertices; i++) {
            System.out.print(String.format("%-15s", vertices[i]));
        }
        System.out.println();

        for (int i = 0; i < numVertices; i++) {
            System.out.print(String.format("%-15s", vertices[i]));
            for (int j = 0; j < numVertices; j++) {
                if (matrizAdyacencia[i][j] == 0) {
                    System.out.print(String.format("%-15s", "0.0"));
                } else {
                    System.out.print(String.format("%-15.1f", matrizAdyacencia[i][j]));
                }
            }
            System.out.println();
        }
        System.out.println("====================================================");
    }

    /**
     * Método Main de prueba para comprobar el funcionamiento local.
     */
    public static void main(String[] args) {
        Graph g = new Graph(10);

        // Registrar las 5 sedes acordadas
        g.agregarVertice("Suba");
        g.agregarVertice("Engativa");
        g.agregarVertice("Kennedy");
        g.agregarVertice("Teusaquillo");
        g.agregarVertice("Usaquen");

        // Registrar las aristas dadas
        g.agregarArista("Suba", "Engativa", 8.5);
        g.agregarArista("Suba", "Kennedy", 16.3);
        g.agregarArista("Suba", "Teusaquillo", 13.5);
        g.agregarArista("Suba", "Usaquen", 6.8);
        g.agregarArista("Engativa", "Teusaquillo", 10.2);
        g.agregarArista("Engativa", "Usaquen", 11.0);
        g.agregarArista("Engativa", "Kennedy", 12.9);
        g.agregarArista("Teusaquillo", "Usaquen", 12.3);
        g.agregarArista("Teusaquillo", "Kennedy", 8.4);
        g.agregarArista("Usaquen", "Kennedy", 19.5);

        // Imprimir el grafo
        g.imprimirGrafo();

        // Probar Dijkstra
        System.out.println("\n--- PRUEBA DE DIJKSTRA ---");
        String desde = "Usaquen";
        String hasta = "Kennedy";
        ResultadoRuta resultado = g.calcularRutaMasCorta(desde, hasta);
        System.out.println("Buscando ruta más corta desde " + desde + " hasta " + hasta + ":");
        System.out.println(resultado);
    }
}
