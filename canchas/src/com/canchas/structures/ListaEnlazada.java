package com.canchas.structures;

public class ListaEnlazada<T> {

    public Nodo<T> head;
    public Nodo<T> tail;
    public int size;


    public ListaEnlazada() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // verifica si la lista esta vacia.
    public boolean isEmpty() {
        return head == null;
    }

    // inserta al inicio de la lista
    public void addFirst(T data) {
        head = new Nodo<>(data, head);

        if (tail == null) {
            tail = head;
        }
        size++;
    }

    // inserta al final
    public void addLast(T data) {
        if (isEmpty()) {
            addFirst(data);
            return;
        }

        tail.next = new Nodo<>(data);
        tail = tail.next;
        size++;
    }

    // elimina de la primera posicion
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        T dataReturn = head.data;
        head = head.next;

        if (head == null) {
            tail = null;
        }
        size--;
        return dataReturn;
    }

    // busca el elemento en la lista.
    public T search(T target) {
        Nodo<T> actual = head;

        while (actual != null) {
            if (actual.data.equals(target)) {
                return actual.data;
            }
            actual = actual.next;
        }
        return null;
    }

    // Elimina el dato especifico
    public T remove(T target) {
        if (isEmpty()) {
            return null;
        }

        if (head.data.equals(target)) {
            return removeFirst();
        }

        Nodo<T> actual = head;

        while (actual.next != null && !actual.next.data.equals(target)) {
            actual = actual.next;
        }

        if (actual.next != null) {
            T dataReturn = actual.next.data;
            actual.next = actual.next.next;

            if (actual.next == null) {
                tail = actual;
            }
            size--;
            return dataReturn;
        }
        return null;
    }

    // Muestra toda la lista
    public void display() {
        Nodo<T> actual = head;
        while (actual != null) {
            System.out.print("[" + actual.data + "] -> ");
            actual = actual.next;
        }
        System.out.println("null");
    }
}
