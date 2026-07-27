package com.canchas.structures;

public class HashTable<K, V> { //Encadenamiento

    private ListaEnlazada<EntradaHash<K, V>>[] casillas;
    private int capacidad;

    public HashTable() {
        this.capacidad = 97;
        this.casillas = (ListaEnlazada<EntradaHash<K, V>>[]) new ListaEnlazada[capacidad];

        for (int i = 0; i < capacidad; i++) {
            casillas[i] = new ListaEnlazada<>();
        }
    }

    // Genera el indice de la key
    private int obtenerIndiceHash(K key) {
        return Math.abs(key.hashCode()) % capacidad;
    }

    // insertar o actualizar los valores de una key
    public void put(K key, V value) {
        int indice = obtenerIndiceHash(key);
        ListaEnlazada<EntradaHash<K, V>> listaColisiones = casillas[indice];

        EntradaHash<K, V> entradaBusqueda = new EntradaHash<>(key, null);
        EntradaHash<K, V> existente = listaColisiones.search(entradaBusqueda);

        if (existente != null) {
            existente.value = value;
        } else {
            listaColisiones.addFirst(new EntradaHash<>(key, value));
        }
    }

    // Busca la key y devuelve sus valores
    public V get(K key) {
        int indice = obtenerIndiceHash(key);
        ListaEnlazada<EntradaHash<K, V>> listaColisiones = casillas[indice];

        EntradaHash<K, V> entradaBusqueda = new EntradaHash<>(key, null);
        EntradaHash<K, V> encontrada = listaColisiones.search(entradaBusqueda);

        if (encontrada != null) {
            return encontrada.value;
        }
        return null;
    }

    // "elimina" la key y su valor
    public V remove(K key) {
        int indice = obtenerIndiceHash(key);
        ListaEnlazada<EntradaHash<K, V>> listaColisiones = casillas[indice];

        EntradaHash<K, V> entradaBusqueda = new EntradaHash<>(key, null);
        EntradaHash<K, V> eliminada = listaColisiones.remove(entradaBusqueda);

        if (eliminada != null) {
            return eliminada.value;
        }
        return null;
    }
}
