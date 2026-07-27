package com.canchas.structures;

public class EntradaHash<K, V> {
    public K key;
    public V value;

    public EntradaHash(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public boolean equals(Object obj) {
        if (obj instanceof EntradaHash) {
            EntradaHash otra = (EntradaHash) obj;
            return this.key.equals(otra.key);
        }
        return false;
    }
}