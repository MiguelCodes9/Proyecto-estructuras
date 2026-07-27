package com.canchas.services;

import com.canchas.models.Field;
import com.canchas.structures.BinarySearchTree;

import java.util.List;

// Dos BST: uno ordenado por capacidad, otro por precio
public class FieldService {

    private BinarySearchTree<Field> arbolPorCapacidad;
    private BinarySearchTree<Field> arbolPorPrecio;

    public FieldService() {
        arbolPorCapacidad = new BinarySearchTree<Field>();
        arbolPorPrecio    = new BinarySearchTree<Field>();
    }

    // inserta en ambos arboles simultaneamente
    public void agregarCancha(Field cancha) {
        arbolPorCapacidad.insertar(cancha.getCapacity(), cancha);
        arbolPorPrecio.insertar((int) cancha.getPricePerHour(), cancha);
        System.out.println("Cancha registrada: " + cancha);
    }

    // elimina de ambos arboles
    public void eliminarCancha(Field cancha) {
        arbolPorCapacidad.eliminar(cancha.getCapacity());
        arbolPorPrecio.eliminar((int) cancha.getPricePerHour());
        System.out.println("Cancha eliminada: " + cancha.getName());
    }

    // busqueda exacta por capacidad (5, 7 o 11)
    public Field buscarPorCapacidad(int capacidad) {
        return arbolPorCapacidad.buscar(capacidad);
    }

    // rango de capacidad: ej. [5, 7] → futbol 5 y futbol 7
    public List<Field> buscarPorRangoCapacidad(int min, int max) {
        return arbolPorCapacidad.buscarPorRango(min, max);
    }

    // rango de precio: ej. [40000, 60000]
    public List<Field> buscarPorRangoPrecio(int precioMin, int precioMax) {
        return arbolPorPrecio.buscarPorRango(precioMin, precioMax);
    }

    // In-Order ascendente por capacidad
    public List<Field> listarPorCapacidad() {
        return arbolPorCapacidad.recorridoInOrder();
    }

    // In-Order ascendente por precio
    public List<Field> listarPorPrecio() {
        return arbolPorPrecio.recorridoInOrder();
    }

    // Pre-Order del arbol de capacidades
    public List<Field> listarPreOrder() {
        return arbolPorCapacidad.recorridoPreOrder();
    }

    public void imprimirEstructuraArbol() {
        System.out.println("\n--- BST por capacidad ---");
        arbolPorCapacidad.imprimirArbol();
        System.out.println("Altura: " + arbolPorCapacidad.altura());
        System.out.println("Total : " + arbolPorCapacidad.tamano());
    }
}
