package com.canchas.structures;

public class Nodo<T> {
    public T data;
    public Nodo<T> next;

    public Nodo(T data) {
        this.data = data;
        this.next = null;
    }

    public Nodo(T data, Nodo<T> next) {
        this.data = data;
        this.next = next;
    }
}
