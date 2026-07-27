package com.canchas.services;

import com.canchas.models.Reservation;
import com.canchas.models.User;
import com.canchas.structures.HashTable;
import com.canchas.structures.ListaEnlazada;
import com.canchas.structures.Nodo;

public class ReservationService {
    private ListaEnlazada<Reservation> pendingQueue;
    private HashTable<String, ListaEnlazada<Reservation>> usersHistories;

    public ReservationService() {
        this.pendingQueue = new ListaEnlazada<>();
        this.usersHistories = new HashTable<>();
    }

    // Para solicitar la reserva de cancha y poder ver el historial
    public void requestReservation(String id, User user, String fieldName, String date) {
        Reservation newReservation = new Reservation(id, user, fieldName, date);

        pendingQueue.addLast(newReservation);

        ListaEnlazada<Reservation> userHistory = usersHistories.get(user.getUsername());

        if (userHistory == null) {
            userHistory = new ListaEnlazada<>();
            usersHistories.put(user.getUsername(), userHistory);
        }

        userHistory.addFirst(newReservation);

        System.out.println("-> [SOLICITUD]: Reserva creada. En espera de aprobacion por el administrador.");
    }

    // Permite gestionar las reservas por orden de llegada
    public void processNextRequest(boolean approve) {
        Reservation request = pendingQueue.removeFirst();

        if (request == null) {
            System.out.println("-> [INFO]: No hay solicitudes de reserva pendientes por procesar.");
            return;
        }

        if (approve) {
            request.setStatus("APROBADA");
            System.out.println("-> [APROBADA]: La reserva #" + request.getId() + " de " + request.getUser().getName() + " fue aprobada.");
        } else {
            request.setStatus("RECHAZADA");
            System.out.println("-> [RECHAZADA]: La reserva #" + request.getId() + " de " + request.getUser().getName() + " fue rechazada.");
        }
    }

    // mostrar historial del usuario
    public void displayUserHistory(String username) {
        ListaEnlazada<Reservation> history = usersHistories.get(username);

        if (history == null || history.isEmpty()) {
            System.out.println("-> [HISTORIAL]: No tienes ninguna reserva registrada.");
            return;
        }

        System.out.println("\n===== TU HISTORIAL DE RESERVAS (Mas reciente primero) =====");
        Nodo<Reservation> actual = history.head;

        // Recorremos la lista de forma clásica y sencilla
        while (actual != null) {
            System.out.println("- " + actual.data.toString());
            actual = actual.next;
        }
        System.out.println("===========================================================");
    }

    // Mostrar la lista de solicitudes
    public void displayPendingQueue() {
        if (pendingQueue.isEmpty()) {
            System.out.println("-> [COLA]: No hay solicitudes pendientes.");
            return;
        }

        System.out.println("\n===== COLA DE ESPERA GLOBAL (Administrador) =====");
        Nodo<Reservation> actual = pendingQueue.head;
        int posicion = 1;

        while (actual != null) {
            System.out.println(posicion + ". " + actual.data.toString());
            actual = actual.next;
            posicion++;
        }
        System.out.println("================================================");
    }
}
