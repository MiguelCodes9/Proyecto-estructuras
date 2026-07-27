package com.canchas.models;

public class Reservation {
    private String id;
    private User user;
    private String fieldName;
    private String date;
    private String status;

    public Reservation(String id, User user, String fieldName, String date) {
        this.id = id;
        this.user = user;
        this.fieldName = fieldName;
        this.date = date;
        this.status = "PENDIENTE";
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return "Reserva #" + id + " | Cancha: " + fieldName + " | Fecha: " + date + " | Estado: " + status;
    }
}