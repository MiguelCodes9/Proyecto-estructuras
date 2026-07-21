package com.canchas.structures;

import java.util.ArrayList;
import java.util.List;

// BST generico: izq < raiz < der | busqueda/insercion O(log n) promedio
public class BinarySearchTree<T> {

    private TreeNode<T> raiz;

    public BinarySearchTree() {
        this.raiz = null;
    }

    // --- INSERCION ---

    public void insertar(int key, T data) {
        raiz = insertarRec(raiz, key, data);
    }

    private TreeNode<T> insertarRec(TreeNode<T> nodo, int key, T data) {
        if (nodo == null) return new TreeNode<T>(key, data);

        if (key < nodo.key) {
            nodo.left = insertarRec(nodo.left, key, data);  // menor → izquierda
        } else if (key > nodo.key) {
            nodo.right = insertarRec(nodo.right, key, data); // mayor → derecha
        } else {
            nodo.data = data; // clave duplicada: actualizar
        }
        return nodo;
    }

    // --- BUSQUEDA EXACTA ---

    public T buscar(int key) {
        TreeNode<T> nodo = buscarRec(raiz, key);
        return (nodo != null) ? nodo.data : null;
    }

    private TreeNode<T> buscarRec(TreeNode<T> nodo, int key) {
        if (nodo == null || nodo.key == key) return nodo;
        if (key < nodo.key) return buscarRec(nodo.left, key);
        return buscarRec(nodo.right, key);
    }

    // --- BUSQUEDA POR RANGO [keyMin, keyMax] ---

    public List<T> buscarPorRango(int keyMin, int keyMax) {
        List<T> resultado = new ArrayList<T>();
        buscarPorRangoRec(raiz, keyMin, keyMax, resultado);
        return resultado;
    }

    private void buscarPorRangoRec(TreeNode<T> nodo, int keyMin, int keyMax, List<T> resultado) {
        if (nodo == null) return;
        if (nodo.key > keyMin) buscarPorRangoRec(nodo.left, keyMin, keyMax, resultado);   // poda izq
        if (nodo.key >= keyMin && nodo.key <= keyMax) resultado.add(nodo.data);           // en rango
        if (nodo.key < keyMax) buscarPorRangoRec(nodo.right, keyMin, keyMax, resultado);  // poda der
    }

    // --- ELIMINACION ---

    public void eliminar(int key) {
        raiz = eliminarRec(raiz, key);
    }

    private TreeNode<T> eliminarRec(TreeNode<T> nodo, int key) {
        if (nodo == null) return null;

        if (key < nodo.key) {
            nodo.left = eliminarRec(nodo.left, key);
        } else if (key > nodo.key) {
            nodo.right = eliminarRec(nodo.right, key);
        } else {
            if (nodo.left == null)  return nodo.right; // sin hijo izq
            if (nodo.right == null) return nodo.left;  // sin hijo der

            // dos hijos → sucesor in-order (minimo del subarbol derecho)
            TreeNode<T> sucesor = minimoNodo(nodo.right);
            nodo.key  = sucesor.key;
            nodo.data = sucesor.data;
            nodo.right = eliminarRec(nodo.right, sucesor.key);
        }
        return nodo;
    }

    private TreeNode<T> minimoNodo(TreeNode<T> nodo) {
        while (nodo.left != null) nodo = nodo.left;
        return nodo;
    }

    // --- RECORRIDOS ---

    // In-Order: izq → raiz → der | resultado ascendente
    public List<T> recorridoInOrder() {
        List<T> resultado = new ArrayList<T>();
        inOrderRec(raiz, resultado);
        return resultado;
    }

    private void inOrderRec(TreeNode<T> nodo, List<T> resultado) {
        if (nodo == null) return;
        inOrderRec(nodo.left, resultado);
        resultado.add(nodo.data);
        inOrderRec(nodo.right, resultado);
    }

    // Pre-Order: raiz → izq → der | refleja estructura del arbol
    public List<T> recorridoPreOrder() {
        List<T> resultado = new ArrayList<T>();
        preOrderRec(raiz, resultado);
        return resultado;
    }

    private void preOrderRec(TreeNode<T> nodo, List<T> resultado) {
        if (nodo == null) return;
        resultado.add(nodo.data);
        preOrderRec(nodo.left, resultado);
        preOrderRec(nodo.right, resultado);
    }

    // --- UTILIDADES ---

    public boolean estaVacio() { return raiz == null; }

    public int tamano() { return tamanoRec(raiz); }

    private int tamanoRec(TreeNode<T> nodo) {
        if (nodo == null) return 0;
        return 1 + tamanoRec(nodo.left) + tamanoRec(nodo.right);
    }

    public int altura() { return alturaRec(raiz); }

    private int alturaRec(TreeNode<T> nodo) {
        if (nodo == null) return 0;
        return 1 + Math.max(alturaRec(nodo.left), alturaRec(nodo.right));
    }

    // imprime el arbol rotado 90° (derecha arriba, izquierda abajo)
    public void imprimirArbol() { imprimirArbolRec(raiz, 0); }

    private void imprimirArbolRec(TreeNode<T> nodo, int nivel) {
        if (nodo == null) return;
        imprimirArbolRec(nodo.right, nivel + 1);
        for (int i = 0; i < nivel; i++) System.out.print("    ");
        System.out.println("[" + nodo.key + "] " + nodo.data);
        imprimirArbolRec(nodo.left, nivel + 1);
    }
}
