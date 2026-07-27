package com.canchas;

import com.canchas.models.User;
import com.canchas.services.AuthService;
import com.canchas.services.ReservationService;
import java.util.Scanner;

public class Prueba {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        ReservationService reservationService = new ReservationService();

        Scanner lector = new Scanner(System.in);
        int contadorReservas = 1;

        authService.register("1001", "migue", "123", "Miguel Angel");
        authService.register("1002", "juli", "456", "Juliana Gomez");

        boolean ejecucionActiva = true;

        System.out.println("\n ========= SISTEMA DE RESERVAS DE CANCHAS DE FUTBOL ========== ");

        while (ejecucionActiva) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Registrarse (Nuevo Jugador)");
            System.out.println("2. Iniciar Sesion");
            System.out.println("3. Panel de Administrador");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opcion: ");

            int opcionPrincipal = leerOpcionEntera(lector);

            switch (opcionPrincipal) {
                case 1:
                    System.out.println("\n--- REGISTRO DE NUEVO JUGADOR ---");

                    String id = leerTextoConCancelacion(lector, "Ingresa tu ID / Cedula");
                    if (id == null) {
                        System.out.println("-> [REGISTRO]: Proceso cancelado por el usuario.");
                        break;
                    }

                    String username = leerTextoConCancelacion(lector, "Crea tu nombre de usuario (username)");
                    if (username == null) {
                        System.out.println("-> [REGISTRO]: Proceso cancelado por el usuario.");
                        break;
                    }

                    String password = leerTextoConCancelacion(lector, "Crea tu contraseña");
                    if (password == null) {
                        System.out.println("-> [REGISTRO]: Proceso cancelado por el usuario.");
                        break;
                    }

                    String nombreCompleto = leerTextoConCancelacion(lector, "Ingresa tu nombre completo");
                    if (nombreCompleto == null) {
                        System.out.println("-> [REGISTRO]: Proceso cancelado por el usuario.");
                        break;
                    }

                    authService.register(id, username, password, nombreCompleto);
                    break;

                case 2:
                    System.out.println("\n--- INICIO DE SESION ---");

                    String loginUser = leerTextoConCancelacion(lector, "Nombre de usuario");
                    if (loginUser == null) {
                        System.out.println("-> [LOGIN]: Proceso cancelado por el usuario.");
                        break;
                    }

                    String loginPass = leerTextoConCancelacion(lector, "Contraseña");
                    if (loginPass == null) {
                        System.out.println("-> [LOGIN]: Proceso cancelado por el usuario.");
                        break;
                    }

                    User usuarioLogueado = authService.login(loginUser, loginPass);

                    if (usuarioLogueado != null) {
                        menuUsuario(usuarioLogueado, reservationService, lector, contadorReservas);
                        contadorReservas += 10;
                    }
                    break;

                case 3:
                    menuAdministrador(reservationService, lector);
                    break;

                case 4:
                    System.out.println("\nGracias por usar el sistema de canchas. ¡Feliz dia!");
                    ejecucionActiva = false;
                    break;

                default:
                    System.out.println("Opcion no valida. Por favor ingresa un numero (1-4).");
            }
        }
        lector.close();
    }

    private static void menuUsuario(User usuario, ReservationService resService, Scanner lector, int idBase) {
        boolean sesionActiva = true;
        int localIdCounter = idBase;

        while (sesionActiva) {
            System.out.println("\n=== PANEL DE JUGADOR: " + usuario.getName().toUpperCase() + " ===");
            System.out.println("1. Solicitar Reserva de Cancha");
            System.out.println("2. Ver mi Historial de Reservas");
            System.out.println("3. Cerrar Sesion");
            System.out.print("Selecciona una opcion: ");

            int opcionUsuario = leerOpcionEntera(lector);

            switch (opcionUsuario) {
                case 1:
                    System.out.println("\n--- SOLICITAR RESERVACION ---");
                    System.out.println("Selecciona la cancha deseada:");
                    System.out.println("1. Cancha Sintetica 5 (F5)");
                    System.out.println("2. Cancha F7 Principal (F7)");
                    System.out.println("3. Cancha de Pasto Profesional (F11)");
                    System.out.println("0. Cancelar y volver atras");
                    System.out.print("Ingresa el numero de la cancha (0-3): ");

                    int opcionCancha = leerOpcionEntera(lector);

                    if (opcionCancha == 0) {
                        System.out.println("-> [RESERVA]: Proceso cancelado por el usuario.");
                        break;
                    }

                    String canchaSeleccionada = "";
                    if (opcionCancha == 1) {
                        canchaSeleccionada = "Cancha Sintetica 5 (F5)";
                    } else if (opcionCancha == 2) {
                        canchaSeleccionada = "Cancha F7 Principal (F7)";
                    } else if (opcionCancha == 3) {
                        canchaSeleccionada = "Cancha de Pasto Profesional (F11)";
                    } else {
                        System.out.println("-> [ERROR]: Opcion de cancha invalida. Proceso cancelado.");
                        break;
                    }

                    String fecha = leerTextoConCancelacion(lector, "Ingresa la fecha (ej. 2026-10-15)");
                    if (fecha == null) {
                        System.out.println("-> [RESERVA]: Proceso cancelado por el usuario.");
                        break;
                    }

                    String idReserva = "RES-" + localIdCounter;
                    localIdCounter++;

                    resService.requestReservation(idReserva, usuario, canchaSeleccionada, fecha);
                    break;

                case 2:
                    resService.displayUserHistory(usuario.getUsername());
                    break;

                case 3:
                    System.out.println("Cerrando sesion de " + usuario.getName());
                    sesionActiva = false;
                    break;

                default:
                    System.out.println("Opcion no valida. Por favor ingresa un numero (1-3).");
            }
        }
    }

    private static void menuAdministrador(ReservationService resService, Scanner lector) {
        boolean adminActivo = true;

        while (adminActivo) {
            System.out.println("\n=== PANEL DE ADMINISTRACION ===");
            System.out.println("1. Ver Cola de Solicitudes Pendientes (FIFO - Cola)");
            System.out.println("2. Procesar Siguiente Solicitud (Aprobar/Rechazar)");
            System.out.println("3. Volver al Menu Principal");
            System.out.print("Selecciona una opcion: ");

            int opcionAdmin = leerOpcionEntera(lector);

            switch (opcionAdmin) {
                case 1:
                    resService.displayPendingQueue();
                    break;

                case 2:
                    System.out.println("\n--- PROCESANDO SIGUIENTE RESERVA ---");
                    System.out.println("¿Deseas evaluar la solicitud de reserva al frente?");
                    System.out.println("1. Si, Aprobar");
                    System.out.println("2. No, Rechazar");
                    System.out.println("3. Cancelar y volver atras");
                    System.out.print("Seleccion (1-3): ");

                    int decision = leerOpcionEntera(lector);

                    if (decision == 3) {
                        System.out.println("-> [ADMIN]: Proceso cancelado.");
                        break;
                    }

                    boolean aprobado = (decision == 1);

                    resService.processNextRequest(aprobado);
                    break;

                case 3:
                    adminActivo = false;
                    break;

                default:
                    System.out.println("Opcion no valida. Por favor ingresa un numero (1-3).");
            }
        }
    }

    private static int leerOpcionEntera(Scanner lector) {
        try {
            int opcion = lector.nextInt();
            lector.nextLine();
            return opcion;
        } catch (Exception e) {
            lector.nextLine();
            return -1;
        }
    }

    private static String leerTextoConCancelacion(Scanner lector, String campo) {
        System.out.print(campo + " (o escribe '0' para cancelar): ");
        String entrada = lector.nextLine().trim();

        if (entrada.equals("0") || entrada.equalsIgnoreCase("cancelar")) {
            return null;
        }
        return entrada;
    }
}
