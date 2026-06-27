

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    private static final SistemaReservas sistema = new SistemaReservas();
    private static final Matchmaking matchmaking = new Matchmaking();
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static void main(String[] args) {
        datosDePrueba();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opción: ");
            switch (opcion) {
                case 1:
                    registrarUsuario();
                    break;
                case 2:
                    verCanchas();
                    break;
                case 3:
                    solicitarPartidoPrivado();
                    break;
                case 4:
                    deshacerUltimaAccion();
                    break;
                case 5:
                    unirseAPartidoAleatorio();
                    break;
                case 6:
                    verEstadoFilaAleatoria();
                    break;
                case 7:
                    verSugerenciasSociales();
                    break;
                case 0:
                    System.out.println("¡Hasta la próxima!");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n=== SISTEMA DE PARTIDOS DE FÚTBOL ===");
        System.out.println("1. Registrar usuario");
        System.out.println("2. Ver canchas disponibles");
        System.out.println("3. Solicitar partido privado (rentar espacio)");
        System.out.println("4. Deshacer última acción");
        System.out.println("5. Unirme a la fila de partido aleatorio");
        System.out.println("6. Ver estado de la fila aleatoria / intentar formar partido");
        System.out.println("7. Ver sugerencias de jugadores conocidos");
        System.out.println("0. Salir");
    }

    private static void registrarUsuario() {
        System.out.print("ID del usuario: ");
        String id = sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        Usuario usuario = sistema.registrarUsuario(id, nombre);
        System.out.println("Usuario creado: " + usuario);
    }

    private static void verCanchas() {
        sistema.getCanchas().values().forEach(System.out::println);
    }

    private static void solicitarPartidoPrivado() {
        System.out.print("ID del usuario: ");
        String idUsuario = sc.nextLine();
        System.out.print("ID de la cancha: ");
        String idCancha = sc.nextLine();
        System.out.print("Fecha y hora de inicio (yyyy-MM-ddTHH:mm), ej. 2026-07-01T18:00: ");
        LocalDateTime inicio = LocalDateTime.parse(sc.nextLine(), FORMATO);
        int duracion = leerEntero("Duración en horas: ");
        boolean balon = leerSiNo("¿Incluir balón? (s/n): ");
        boolean hidratacion = leerSiNo("¿Incluir hidratación? (s/n): ");

        try {
            Partido partido = sistema.solicitarPartido(idUsuario, idCancha, inicio, duracion, balon, hidratacion);
            if (partido == null) {
                System.out.println("No hay disponibilidad en ese horario para esa cancha.");
            } else {
                System.out.println("¡Partido programado con éxito!");
                System.out.println(partido);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deshacerUltimaAccion() {
        System.out.println(sistema.deshacerUltimaAccion());
    }

    private static void unirseAPartidoAleatorio() {
        System.out.print("ID del usuario: ");
        String idUsuario = sc.nextLine();
        Usuario usuario = sistema.buscarUsuario(idUsuario);
        if (usuario == null) {
            System.out.println("Ese usuario no existe.");
            return;
        }
        matchmaking.unirseAFilaAleatoria(usuario);
        System.out.println(usuario.getNombre() + " entró a la fila de partido aleatorio. "
            + "En espera: " + matchmaking.jugadoresEnEspera());
    }

    private static void verEstadoFilaAleatoria() {
        System.out.println("Jugadores en espera: " + matchmaking.jugadoresEnEspera() + "/10");
        System.out.print("ID de cancha para intentar formar el partido: ");
        String idCancha = sc.nextLine();
        System.out.print("Fecha y hora de inicio (yyyy-MM-ddTHH:mm): ");
        LocalDateTime inicio = LocalDateTime.parse(sc.nextLine(), FORMATO);

        Partido partido = matchmaking.intentarFormarPartidoAleatorio(sistema, idCancha, inicio);
        if (partido == null) {
            System.out.println("Aún no hay suficientes jugadores, o no hay disponibilidad en la cancha.");
        } else {
            System.out.println("¡Partido aleatorio formado!");
            System.out.println(partido);
        }
    }

    private static void verSugerenciasSociales() {
        System.out.print("ID del usuario: ");
        String idUsuario = sc.nextLine();
        System.out.println("Jugadores conocidos (directos o de 2do grado): " + matchmaking.sugerirConocidos(idUsuario));
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {
            sc.nextLine();
            System.out.print("Ingresa un número válido: ");
        }
        int valor = sc.nextInt();
        sc.nextLine();
        return valor;
    }

    private static boolean leerSiNo(String mensaje) {
        System.out.print(mensaje);
        String respuesta = sc.nextLine().trim().toLowerCase();
        return respuesta.equals("s") || respuesta.equals("si");
    }

    private static void datosDePrueba() {
        sistema.registrarCancha("C1", "Cancha El Bosque", "Bogotá - Suba", 60000);
        sistema.registrarCancha("C2", "Cancha La 80", "Bogotá - Engativá", 50000);
    }
}
