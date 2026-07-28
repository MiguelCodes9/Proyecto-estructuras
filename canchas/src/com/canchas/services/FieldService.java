package com.canchas.services;

import com.canchas.models.Field;
import com.canchas.structures.BinarySearchTree;
import com.canchas.structures.ListaEnlazada;
import com.canchas.structures.Nodo;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de gestión del catálogo de canchas.
 *
 * Utiliza dos Árboles Binarios de Búsqueda (BST):
 *   - arbolPorCapacidad: ordenado por cantidad de jugadores (5, 7 u 11).
 *   - arbolPorPrecio:    ordenado por precio por hora.
 *
 * Cada nodo del árbol almacena una ListaEnlazada<Field> para soportar
 * múltiples canchas con la misma capacidad o precio sin pérdida de datos.
 */
public class FieldService {

    private BinarySearchTree<ListaEnlazada<Field>> arbolPorCapacidad;
    private BinarySearchTree<ListaEnlazada<Field>> arbolPorPrecio;

    public FieldService() {
        arbolPorCapacidad = new BinarySearchTree<ListaEnlazada<Field>>();
        arbolPorPrecio    = new BinarySearchTree<ListaEnlazada<Field>>();
    }

    // ==========================================
    // OPERACIONES PRINCIPALES
    // ==========================================

    /**
     * Registra una cancha en ambos árboles (capacidad y precio).
     * Si ya existe un nodo con la misma clave, agrega la cancha a su lista.
     */
    public void agregarCancha(Field cancha) {
        // --- Árbol por capacidad ---
        int keyCapacidad = cancha.getCapacity();
        ListaEnlazada<Field> listaCapacidad = arbolPorCapacidad.buscar(keyCapacidad);
        if (listaCapacidad == null) {
            listaCapacidad = new ListaEnlazada<Field>();
            arbolPorCapacidad.insertar(keyCapacidad, listaCapacidad);
        }
        listaCapacidad.addLast(cancha);

        // --- Árbol por precio ---
        int keyPrecio = (int) cancha.getPricePerHour();
        ListaEnlazada<Field> listaPrecio = arbolPorPrecio.buscar(keyPrecio);
        if (listaPrecio == null) {
            listaPrecio = new ListaEnlazada<Field>();
            arbolPorPrecio.insertar(keyPrecio, listaPrecio);
        }
        listaPrecio.addLast(cancha);

        System.out.println("Cancha registrada: " + cancha);
    }

    /**
     * Elimina una cancha específica de ambos árboles.
     * Si la lista en ese nodo queda vacía, elimina el nodo del árbol.
     */
    public void eliminarCancha(Field cancha) {
        // --- Árbol por capacidad ---
        int keyCapacidad = cancha.getCapacity();
        ListaEnlazada<Field> listaCapacidad = arbolPorCapacidad.buscar(keyCapacidad);
        if (listaCapacidad != null) {
            listaCapacidad.remove(cancha);
            if (listaCapacidad.isEmpty()) {
                arbolPorCapacidad.eliminar(keyCapacidad);
            }
        }

        // --- Árbol por precio ---
        int keyPrecio = (int) cancha.getPricePerHour();
        ListaEnlazada<Field> listaPrecio = arbolPorPrecio.buscar(keyPrecio);
        if (listaPrecio != null) {
            listaPrecio.remove(cancha);
            if (listaPrecio.isEmpty()) {
                arbolPorPrecio.eliminar(keyPrecio);
            }
        }

        System.out.println("Cancha eliminada: " + cancha.getName());
    }

    // ==========================================
    // BÚSQUEDAS
    // ==========================================

    /**
     * Busca la primera cancha que coincida exactamente con la capacidad dada.
     * @return la primera Field encontrada, o null si no existe.
     */
    public Field buscarPorCapacidad(int capacidad) {
        ListaEnlazada<Field> lista = arbolPorCapacidad.buscar(capacidad);
        if (lista == null || lista.isEmpty()) return null;
        return lista.head.data;
    }

    /** Retorna todas las canchas cuya capacidad esté dentro del rango [min, max]. */
    public List<Field> buscarPorRangoCapacidad(int min, int max) {
        List<ListaEnlazada<Field>> listas = arbolPorCapacidad.buscarPorRango(min, max);
        return aplanarListas(listas);
    }

    /** Retorna todas las canchas cuyo precio por hora esté dentro del rango [precioMin, precioMax]. */
    public List<Field> buscarPorRangoPrecio(int precioMin, int precioMax) {
        List<ListaEnlazada<Field>> listas = arbolPorPrecio.buscarPorRango(precioMin, precioMax);
        return aplanarListas(listas);
    }

    // ==========================================
    // RECORRIDOS
    // ==========================================

    /** Recorrido In-Order del árbol de capacidades (orden ascendente). */
    public List<Field> listarPorCapacidad() {
        List<ListaEnlazada<Field>> listas = arbolPorCapacidad.recorridoInOrder();
        return aplanarListas(listas);
    }

    /** Recorrido In-Order del árbol de precios (orden ascendente). */
    public List<Field> listarPorPrecio() {
        List<ListaEnlazada<Field>> listas = arbolPorPrecio.recorridoInOrder();
        return aplanarListas(listas);
    }

    /** Recorrido Pre-Order del árbol de capacidades. */
    public List<Field> listarPreOrder() {
        List<ListaEnlazada<Field>> listas = arbolPorCapacidad.recorridoPreOrder();
        return aplanarListas(listas);
    }

    // ==========================================
    // VISUALIZACIÓN
    // ==========================================

    /**
     * Imprime la estructura del árbol de capacidades mostrando todas las
     * canchas en orden ascendente de capacidad, junto con la altura y
     * el número de nodos del árbol.
     */
    public void imprimirEstructuraArbol() {
        System.out.println("\n===== ESTRUCTURA DEL BST DE CANCHAS =====");
        System.out.println("--- Recorrido In-Order (por capacidad) ---");
        List<Field> canchas = listarPorCapacidad();
        if (canchas.isEmpty()) {
            System.out.println("  (sin canchas registradas)");
        } else {
            for (Field f : canchas) System.out.println("  " + f);
        }
        System.out.println("Altura del arbol : " + arbolPorCapacidad.altura());
        System.out.println("Nodos unicos     : " + arbolPorCapacidad.tamano());
        System.out.println("Total canchas    : " + canchas.size());
        System.out.println("==========================================");
    }

    // ==========================================
    // AUXILIAR
    // ==========================================

    /**
     * Aplana una lista de ListaEnlazada<Field> en una sola List<Field>.
     * Recorre cada ListaEnlazada y extrae sus elementos en orden de inserción.
     */
    private List<Field> aplanarListas(List<ListaEnlazada<Field>> listas) {
        List<Field> resultado = new ArrayList<Field>();
        for (ListaEnlazada<Field> lista : listas) {
            Nodo<Field> actual = lista.head;
            while (actual != null) {
                resultado.add(actual.data);
                actual = actual.next;
            }
        }
        return resultado;
    }
}
