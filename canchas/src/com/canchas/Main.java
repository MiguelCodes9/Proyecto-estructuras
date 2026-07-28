package com.canchas;

import com.canchas.models.Field;
import com.canchas.models.User;
import com.canchas.services.AuthService;
import com.canchas.services.FieldService;
import com.canchas.services.ReservationService;
import com.canchas.structures.Graph;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static AuthService authService = new AuthService();
    private static FieldService fieldService = new FieldService();
    private static ReservationService reservationService = new ReservationService();
    private static Graph grafoSedes = new Graph(20);
    private static Scanner lector = new Scanner(System.in);
    
    private static int contadorReservas = 1;

    public static void main(String[] args) {
        cargarDatosDePrueba();

        boolean ejecucionActiva = true;
        System.out.println("\n===============================================================");
        System.out.println("   SISTEMA INTEGRADO DE RESERVA DE CANCHAS & RUTAS DE SEDES");
        System.out.println("===============================================================");

        while (ejecucionActiva) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Registrarse (Nuevo Jugador)");
            System.out.println("2. Iniciar Sesion");
            System.out.println("3. Ver Sedes de Canchas y Rutas (Grafo & Dijkstra)");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opcion: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    registrarJugador();
                    break;
                case 2:
                    iniciarSesion();
                    break;
                case 3:
                    menuGrafoRutas();
                    break;
                case 4:
                    System.out.println("\n¡Gracias por usar nuestro sistema de canchas! ¡Feliz dia!");
                    ejecucionActiva = false;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, selecciona entre 1 y 4.");
            }
        }
        lector.close();
    }

    // ==========================================
    // MENÚS Y SUBMENÚS
    // ==========================================

    private static void registrarJugador() {
        System.out.println("\n--- REGISTRO DE NUEVO JUGADOR ---");
        String id = leerTexto("Ingresa tu ID / Cédula");
        if (id == null) return;

        String username = leerTexto("Crea tu nombre de usuario (username)");
        if (username == null) return;

        String password = leerTexto("Crea tu contraseña");
        if (password == null) return;

        String name = leerTexto("Ingresa tu nombre completo");
        if (name == null) return;

        authService.register(id, username, password, name);
    }

    private static void iniciarSesion() {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        String username = leerTexto("Nombre de usuario");
        if (username == null) return;

        String password = leerTexto("Contraseña");
        if (password == null) return;

        // Validar si es administrador
        if (username.equalsIgnoreCase("admin") && password.equals("admin123")) {
            System.out.println("-> [BIENVENIDO]: Inicio de sesión exitoso como ADMINISTRADOR.");
            menuAdministrador();
            return;
        }

        User usuarioLogueado = authService.login(username, password);
        if (usuarioLogueado != null) {
            menuJugador(usuarioLogueado);
        }
    }

    private static void menuJugador(User usuario) {
        boolean sesionActiva = true;
        while (sesionActiva) {
            System.out.println("\n=== PANEL DE JUGADOR: " + usuario.getName().toUpperCase() + " ===");
            System.out.println("1. Ver Catálogo de Canchas (Listar por Precio/Capacidad)");
            System.out.println("2. Filtrar Canchas por Rango (Precio/Capacidad)");
            System.out.println("3. Solicitar Reserva de Cancha");
            System.out.println("4. Consultar Ruta y Distancia hacia una Sede");
            System.out.println("5. Ver mi Historial de Reservas (HashTable + ListaEnlazada)");
            System.out.println("6. Cerrar Sesión");
            System.out.print("Selecciona una opción: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    listarCatalogos();
                    break;
                case 2:
                    filtrarCatalogos();
                    break;
                case 3:
                    solicitarReserva(usuario);
                    break;
                case 4:
                    consultarRutaDijkstra(null);
                    break;
                case 5:
                    reservationService.displayUserHistory(usuario.getUsername());
                    break;
                case 6:
                    System.out.println("Cerrando sesión de " + usuario.getName() + "...");
                    sesionActiva = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void menuAdministrador() {
        boolean adminActivo = true;
        while (adminActivo) {
            System.out.println("\n=== PANEL DE ADMINISTRACIÓN ===");
            System.out.println("1. Registrar Nueva Cancha (Agregar al BST)");
            System.out.println("2. Ver Cola de Solicitudes de Reservas (FIFO - ListaEnlazada)");
            System.out.println("3. Procesar Siguiente Solicitud (Aprobar/Rechazar)");
            System.out.println("4. Ver Estructura del Árbol de Canchas");
            System.out.println("5. Volver al Menú Principal");
            System.out.print("Selecciona una opción: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    agregarCanchaAdmin();
                    break;
                case 2:
                    reservationService.displayPendingQueue();
                    break;
                case 3:
                    procesarReservaAdmin();
                    break;
                case 4:
                    fieldService.imprimirEstructuraArbol();
                    break;
                case 5:
                    adminActivo = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void menuGrafoRutas() {
        boolean grafoActivo = true;
        while (grafoActivo) {
            System.out.println("\n=== RUTAS Y SEDES DE CANCHAS ===");
            System.out.println("1. Mostrar Matriz de Distancias entre Sedes");
            System.out.println("2. Calcular Camino Más Corto entre Dos Sedes (Dijkstra)");
            System.out.println("3. Volver al Menú Principal");
            System.out.print("Selecciona una opción: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    grafoSedes.imprimirGrafo();
                    break;
                case 2:
                    consultarRutaDijkstra(null);
                    break;
                case 3:
                    grafoActivo = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // ==========================================
    // LÓGICA DE CANCHAS (BST)
    // ==========================================

    private static void listarCatalogos() {
        System.out.println("\n--- VER CATÁLOGO DE CANCHAS ---");
        System.out.println("1. Listar por capacidad (In-Order BST)");
        System.out.println("2. Listar por precio (In-Order BST)");
        System.out.println("0. Volver");
        System.out.print("Selecciona: ");
        int op = leerEntero();

        if (op == 1) {
            System.out.println("\n-- Canchas Ordenadas por Capacidad --");
            List<Field> canchas = fieldService.listarPorCapacidad();
            if (canchas.isEmpty()) {
                System.out.println("No hay canchas registradas.");
            } else {
                for (Field f : canchas) System.out.println(f);
            }
        } else if (op == 2) {
            System.out.println("\n-- Canchas Ordenadas por Precio --");
            List<Field> canchas = fieldService.listarPorPrecio();
            if (canchas.isEmpty()) {
                System.out.println("No hay canchas registradas.");
            } else {
                for (Field f : canchas) System.out.println(f);
            }
        }
    }

    private static void filtrarCatalogos() {
        System.out.println("\n--- FILTRAR CANCHAS ---");
        System.out.println("1. Filtrar por rango de capacidad (ej: entre 5 y 7 jugadores)");
        System.out.println("2. Filtrar por rango de precio");
        System.out.println("0. Volver");
        System.out.print("Selecciona: ");
        int op = leerEntero();

        if (op == 1) {
            System.out.print("Capacidad mínima: ");
            int min = leerEntero();
            System.out.print("Capacidad máxima: ");
            int max = leerEntero();
            List<Field> resultados = fieldService.buscarPorRangoCapacidad(min, max);
            System.out.println("\nCanchas encontradas:");
            if (resultados.isEmpty()) {
                System.out.println("  Ninguna cancha en ese rango.");
            } else {
                for (Field f : resultados) System.out.println("  " + f);
            }
        } else if (op == 2) {
            System.out.print("Precio mínimo: ");
            int min = leerEntero();
            System.out.print("Precio máximo: ");
            int max = leerEntero();
            List<Field> resultados = fieldService.buscarPorRangoPrecio(min, max);
            System.out.println("\nCanchas encontradas:");
            if (resultados.isEmpty()) {
                System.out.println("  Ninguna cancha en ese rango.");
            } else {
                for (Field f : resultados) System.out.println("  " + f);
            }
        }
    }

    private static void agregarCanchaAdmin() {
        System.out.println("\n--- REGISTRAR NUEVA CANCHA ---");

        // Usar leerTexto() para evitar problemas de buffer con Scanner
        String id = leerTexto("ID de la cancha (ej: C6)");
        if (id == null) return;

        String nombre = leerTexto("Nombre de la cancha");
        if (nombre == null) return;

        // Mostrar sedes del grafo para que el administrador asocie la cancha a una sede válida
        System.out.println("Sedes de juego disponibles:");
        String[] sedes = grafoSedes.getVertices();
        for (int i = 0; i < sedes.length; i++) {
            System.out.println((i + 1) + ". " + sedes[i]);
        }
        System.out.print("Selecciona el número de la sede / ubicación: ");
        int opSede = leerEntero();
        if (opSede < 1 || opSede > sedes.length) {
            System.out.println("-> [ERROR]: Selección de sede no válida. Proceso cancelado.");
            return;
        }
        String ubicacion = sedes[opSede - 1];

        System.out.print("Capacidad (5, 7 o 11 jugadores): ");
        int cap = leerEntero();
        if (cap != 5 && cap != 7 && cap != 11) {
            System.out.println("-> [ERROR]: Capacidad inválida. Solo se permiten 5, 7 u 11 jugadores.");
            return;
        }
        System.out.print("Precio por hora: ");
        int precio = leerEntero();
        if (precio <= 0) {
            System.out.println("-> [ERROR]: El precio debe ser mayor a 0.");
            return;
        }

        fieldService.agregarCancha(new Field(id, nombre, ubicacion, cap, precio));
    }

    // ==========================================
    // LÓGICA DE RESERVAS Y GRAFO (INTEGRACIÓN)
    // ==========================================

    private static void solicitarReserva(User usuario) {
        System.out.println("\n--- SOLICITAR RESERVACIÓN ---");
        List<Field> canchas = fieldService.listarPorCapacidad();
        if (canchas.isEmpty()) {
            System.out.println("-> [ERROR]: No hay canchas registradas en el catálogo. No se puede reservar.");
            return;
        }

        System.out.println("Canchas del catálogo actual:");
        for (int i = 0; i < canchas.size(); i++) {
            System.out.println((i + 1) + ". " + canchas.get(i));
        }
        System.out.print("Selecciona el número de la cancha que deseas reservar (0 para cancelar): ");
        int seleccion = leerEntero();
        if (seleccion == 0) return;
        if (seleccion < 1 || seleccion > canchas.size()) {
            System.out.println("-> [ERROR]: Selección no válida.");
            return;
        }

        Field canchaSeleccionada = canchas.get(seleccion - 1);
        System.out.println("Has seleccionado la cancha: " + canchaSeleccionada.getName() + " ubicada en " + canchaSeleccionada.getLocation());

        System.out.print("Ingresa la fecha de juego (ej: 2026-10-15): ");
        String fecha = lector.nextLine().trim();

        String idReserva = "RES-" + contadorReservas;
        contadorReservas++;

        // Enviar solicitud de reserva a la cola FIFO
        reservationService.requestReservation(idReserva, usuario, canchaSeleccionada.getName(), fecha);

        // Integración con Dijkstra: mostrar ruta desde la ubicación del jugador a la sede
        System.out.println("\n¿Deseas conocer la ruta más corta para llegar a esta cancha desde tu ubicación actual?");
        System.out.println("1. Sí, calcular ruta");
        System.out.println("2. No, terminar reserva");
        System.out.print("Selecciona (1-2): ");
        int opRuta = leerEntero();
        if (opRuta == 1) {
            consultarRutaDijkstra(canchaSeleccionada.getLocation());
        }
    }

    private static void procesarReservaAdmin() {
        System.out.println("\n--- PROCESANDO SIGUIENTE RESERVA ---");
        System.out.println("¿Deseas evaluar la solicitud de reserva al frente de la cola?");
        System.out.println("1. Sí, Aprobar");
        System.out.println("2. No, Rechazar");
        System.out.println("3. Cancelar y volver atrás");
        System.out.print("Selección (1-3): ");

        int decision = leerEntero();
        if (decision == 3) {
            System.out.println("-> [ADMIN]: Proceso cancelado.");
            return;
        }

        boolean aprobado = (decision == 1);
        reservationService.processNextRequest(aprobado);
    }

    private static void consultarRutaDijkstra(String sedeDestinoPredeterminada) {
        System.out.println("\n--- CÁLCULO DE RUTA MÁS CORTA (DIJKSTRA) ---");
        String[] sedes = grafoSedes.getVertices();

        String sedeOrigen = "";
        String sedeDestino = "";

        // Obtener sede origen
        System.out.println("Sedes de origen disponibles:");
        for (int i = 0; i < sedes.length; i++) {
            System.out.println((i + 1) + ". " + sedes[i]);
        }
        System.out.print("Selecciona tu sede de origen (número): ");
        int opOrigen = leerEntero();
        if (opOrigen < 1 || opOrigen > sedes.length) {
            System.out.println("-> [ERROR]: Origen no válido.");
            return;
        }
        sedeOrigen = sedes[opOrigen - 1];

        // Obtener sede destino
        if (sedeDestinoPredeterminada != null) {
            sedeDestino = sedeDestinoPredeterminada;
            System.out.println("Sede de destino predeterminada (Ubicación de la cancha): " + sedeDestino);
        } else {
            System.out.println("Sedes de destino disponibles:");
            for (int i = 0; i < sedes.length; i++) {
                System.out.println((i + 1) + ". " + sedes[i]);
            }
            System.out.print("Selecciona la sede de destino (número): ");
            int opDestino = leerEntero();
            if (opDestino < 1 || opDestino > sedes.length) {
                System.out.println("-> [ERROR]: Destino no válido.");
                return;
            }
            sedeDestino = sedes[opDestino - 1];
        }

        if (sedeOrigen.equalsIgnoreCase(sedeDestino)) {
            System.out.println("-> [Ruta]: Ya te encuentras en la sede '" + sedeOrigen + "'. Distancia: 0 km.");
            return;
        }

        // Ejecutar Dijkstra
        Graph.ResultadoRuta resultado = grafoSedes.calcularRutaMasCorta(sedeOrigen, sedeDestino);
        System.out.println("\n================ RESULTADO DE LA RUTA ================");
        System.out.println(resultado);
        System.out.println("======================================================");
    }

    // ==========================================
    // CARGA DE DATOS DE PRUEBA E INICIALIZACIÓN
    // ==========================================

    private static void cargarDatosDePrueba() {

        // ─────────────────────────────────────────────────────────────────
        // 1. SEDES PRINCIPALES (tienen canchas asociadas)
        // ─────────────────────────────────────────────────────────────────
        grafoSedes.agregarVertice("Suba");
        grafoSedes.agregarVertice("Engativa");
        grafoSedes.agregarVertice("Kennedy");
        grafoSedes.agregarVertice("Teusaquillo");
        grafoSedes.agregarVertice("Usaquen");

        // ─────────────────────────────────────────────────────────────────
        // 2. NODOS INTERMEDIOS (solo de paso, sin canchas)
        // ─────────────────────────────────────────────────────────────────
        grafoSedes.agregarVertice("Chapinero");
        grafoSedes.agregarVertice("Barrios Unidos");
        grafoSedes.agregarVertice("Fontibon");
        grafoSedes.agregarVertice("Santafe");
        grafoSedes.agregarVertice("La Candelaria");
        grafoSedes.agregarVertice("Los Martires");
        grafoSedes.agregarVertice("Puente Aranda");
        grafoSedes.agregarVertice("Antonio Narino");
        grafoSedes.agregarVertice("Tunjuelito");
        grafoSedes.agregarVertice("Rafael Uribe");
        grafoSedes.agregarVertice("San Cristobal");
        grafoSedes.agregarVertice("Usme");
        grafoSedes.agregarVertice("Ciudad Bolivar");
        grafoSedes.agregarVertice("Bosa");

        // ─────────────────────────────────────────────────────────────────
        // 3. CONEXIONES ENTRE SEDES PRINCIPALES (distancias originales)
        // ─────────────────────────────────────────────────────────────────
        grafoSedes.agregarArista("Suba",        "Engativa",    8.5);
        grafoSedes.agregarArista("Suba",        "Kennedy",    16.3);
        grafoSedes.agregarArista("Suba",        "Teusaquillo",13.5);
        grafoSedes.agregarArista("Suba",        "Usaquen",     6.8);
        grafoSedes.agregarArista("Engativa",    "Teusaquillo",10.2);
        grafoSedes.agregarArista("Engativa",    "Usaquen",    11.0);
        grafoSedes.agregarArista("Engativa",    "Kennedy",    12.9);
        grafoSedes.agregarArista("Teusaquillo", "Usaquen",    12.3);
        grafoSedes.agregarArista("Teusaquillo", "Kennedy",     8.4);
        grafoSedes.agregarArista("Usaquen",     "Kennedy",    19.5);

        // ─────────────────────────────────────────────────────────────────
        // 4. SEDES PRINCIPALES <-> NODOS INTERMEDIOS
        // ─────────────────────────────────────────────────────────────────
        // Suba
        grafoSedes.agregarArista("Suba",        "Barrios Unidos",  7.0);

        // Usaquen
        grafoSedes.agregarArista("Usaquen",     "Chapinero",       5.5);

        // Engativa
        grafoSedes.agregarArista("Engativa",    "Fontibon",         6.0);
        grafoSedes.agregarArista("Engativa",    "Barrios Unidos",   7.5);

        // Teusaquillo
        grafoSedes.agregarArista("Teusaquillo", "Barrios Unidos",   4.5);
        grafoSedes.agregarArista("Teusaquillo", "Chapinero",        5.0);
        grafoSedes.agregarArista("Teusaquillo", "Los Martires",     3.5);
        grafoSedes.agregarArista("Teusaquillo", "Puente Aranda",    4.0);

        // Kennedy
        grafoSedes.agregarArista("Kennedy",     "Bosa",             6.5);
        grafoSedes.agregarArista("Kennedy",     "Fontibon",         5.0);
        grafoSedes.agregarArista("Kennedy",     "Puente Aranda",    5.5);
        grafoSedes.agregarArista("Kennedy",     "Tunjuelito",       7.5);

        // ─────────────────────────────────────────────────────────────────
        // 5. CONEXIONES ENTRE NODOS INTERMEDIOS
        // ─────────────────────────────────────────────────────────────────
        // Zona Norte / Centro-Norte
        grafoSedes.agregarArista("Chapinero",      "Barrios Unidos",  4.0);
        grafoSedes.agregarArista("Chapinero",      "Santafe",         3.5);
        grafoSedes.agregarArista("Barrios Unidos", "Los Martires",    5.0);

        // Zona Occidente
        grafoSedes.agregarArista("Fontibon",       "Puente Aranda",   6.5);
        grafoSedes.agregarArista("Fontibon",       "Bosa",            9.0);

        // Zona Centro
        grafoSedes.agregarArista("Santafe",        "La Candelaria",   2.0);
        grafoSedes.agregarArista("Santafe",        "Los Martires",    3.0);
        grafoSedes.agregarArista("Santafe",        "Antonio Narino",  4.5);
        grafoSedes.agregarArista("Santafe",        "San Cristobal",   5.0);
        grafoSedes.agregarArista("La Candelaria",  "Los Martires",    2.5);
        grafoSedes.agregarArista("La Candelaria",  "Antonio Narino",  3.5);
        grafoSedes.agregarArista("Los Martires",   "Puente Aranda",   3.0);
        grafoSedes.agregarArista("Los Martires",   "Antonio Narino",  3.5);
        grafoSedes.agregarArista("Puente Aranda",  "Antonio Narino",  4.0);
        grafoSedes.agregarArista("Puente Aranda",  "Tunjuelito",      7.0);

        // Zona Sur
        grafoSedes.agregarArista("Antonio Narino", "Rafael Uribe",    4.5);
        grafoSedes.agregarArista("Antonio Narino", "Tunjuelito",      5.0);
        grafoSedes.agregarArista("Tunjuelito",     "Ciudad Bolivar",  5.5);
        grafoSedes.agregarArista("Tunjuelito",     "Rafael Uribe",    5.0);
        grafoSedes.agregarArista("Tunjuelito",     "Bosa",            8.0);
        grafoSedes.agregarArista("Rafael Uribe",   "San Cristobal",   5.5);
        grafoSedes.agregarArista("Rafael Uribe",   "Ciudad Bolivar",  7.0);
        grafoSedes.agregarArista("Rafael Uribe",   "Usme",            8.0);
        grafoSedes.agregarArista("San Cristobal",  "Usme",            7.0);
        grafoSedes.agregarArista("Usme",           "Ciudad Bolivar",  9.0);
        grafoSedes.agregarArista("Ciudad Bolivar", "Bosa",            6.5);

        // ─────────────────────────────────────────────────────────────────
        // 6. CANCHAS EN EL CATÁLOGO (BST - solo sedes principales)
        // ─────────────────────────────────────────────────────────────────
        fieldService.agregarCancha(new Field("C1", "El Bosque",  "Suba",        7, 60000));
        fieldService.agregarCancha(new Field("C2", "La 80",      "Engativa",    5, 45000));
        fieldService.agregarCancha(new Field("C3", "Estadio",    "Teusaquillo", 11, 90000));
        fieldService.agregarCancha(new Field("C4", "Los Pinos",  "Usaquen",     5, 40000));
        fieldService.agregarCancha(new Field("C5", "Sur Norte",  "Kennedy",     7, 55000));

        // ─────────────────────────────────────────────────────────────────
        // 7. USUARIOS INICIALES
        // ─────────────────────────────────────────────────────────────────
        authService.register("1001", "migue", "123", "Miguel Angel");
        authService.register("1002", "juli",  "456", "Juliana Gomez");
    }

    // ==========================================
    // AUXILIARES DE LECTURA
    // ==========================================

    private static String leerTexto(String campo) {
        System.out.print(campo + " (o escribe '0' para cancelar): ");
        String entrada = lector.nextLine().trim();
        if (entrada.equals("0") || entrada.equalsIgnoreCase("cancelar")) {
            System.out.println("-> Proceso cancelado.");
            return null;
        }
        return entrada;
    }

    private static int leerEntero() {
        while (!lector.hasNextInt()) {
            lector.nextLine();
            System.out.print("Ingresa un número válido: ");
        }
        int valor = lector.nextInt();
        lector.nextLine(); // Limpiar buffer
        return valor;
    }
}
