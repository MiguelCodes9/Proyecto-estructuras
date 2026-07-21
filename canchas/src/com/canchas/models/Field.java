package com.canchas.models;

// Cancha: capacity → key en BST por capacidad | pricePerHour → key en BST por precio
public class Field {

    private String id;
    private String name;
    private String location;
    private int capacity;        // 5, 7 o 11 jugadores
    private double pricePerHour;

    public Field(String id, String name, String location, int capacity, double pricePerHour) {
        this.id           = id;
        this.name         = name;
        this.location     = location;
        this.capacity     = capacity;
        this.pricePerHour = pricePerHour;
    }

    public String getId()           { return id; }
    public String getName()         { return name; }
    public String getLocation()     { return location; }
    public int getCapacity()        { return capacity; }
    public double getPricePerHour() { return pricePerHour; }

    @Override
    public String toString() {
        return String.format(
            "Cancha [%s] %s | Ubicacion: %s | Futbol %d | Precio: $%.0f/hora",
            id, name, location, capacity, pricePerHour
        );
    }
}
