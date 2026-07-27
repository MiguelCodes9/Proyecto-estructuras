package com.canchas;

import com.canchas.models.Field;
import com.canchas.services.FieldService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static FieldService fieldService = new FieldService();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        cargarDatosDePrueba();

        int opcion;
        do {
            System.out.println("\n====== SISTEMA DE CANCHAS (BST) ======");
            System.out.println("1. Listar por capacidad (In-Order)");
            System.out.println("2. Listar por precio    (In-Order)");
            System.out.println("3. Buscar por capacidad exacta");
            System.out.println("4. Filtrar por rango de capacidad");
            System.out.println("5. Filtrar por rango de precio");
            System.out.println("6. Ver estructura del arbol");
            System.out.println("7. Recorrido Pre-Order");
            System.out.println("8. Agregar cancha");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    listarPorCapacidad();
                    break;
                case 2:
                    listarPorPrecio();
                    break;
                case 3:
                    buscarPorCapacidad();
                    break;
                case 4:
                    filtrarPorRangoCapacidad();
                    break;
                case 5:
                    filtrarPorRangoPrecio();
                    break;
                case 6:
                    fieldService.imprimirEstructuraArbol();
                    break;
                case 7:
                    preOrder();
                    break;
                case 8:
                    agregarCancha();
                    break;
                case 0:
                    System.out.println("Hasta luego!");
                    break;
                default:
                    System.out.println("Opcion no valida.");
                    break;
            }
        } while (opcion != 0);
    }

    private static void listarPorCapacidad() {
        System.out.println("\n-- In-Order por capacidad --");
        List<Field> canchas = fieldService.listarPorCapacidad();
        if (canchas.isEmpty()) { System.out.println("Sin canchas registradas."); return; }
        for (Field f : canchas) System.out.println(f);
    }

    private static void listarPorPrecio() {
        System.out.println("\n-- In-Order por precio --");
        for (Field f : fieldService.listarPorPrecio()) System.out.println(f);
    }

    private static void buscarPorCapacidad() {
        System.out.print("Capacidad (5, 7 o 11): ");
        int cap = leerEntero();
        Field resultado = fieldService.buscarPorCapacidad(cap);
        System.out.println(resultado != null ? resultado : "No encontrada.");
    }

    private static void filtrarPorRangoCapacidad() {
        System.out.print("Capacidad minima: "); int min = leerEntero();
        System.out.print("Capacidad maxima: "); int max = leerEntero();
        List<Field> resultados = fieldService.buscarPorRangoCapacidad(min, max);
        System.out.println("Canchas entre " + min + " y " + max + " jugadores:");
        if (resultados.isEmpty()) { System.out.println("  Ninguna."); return; }
        for (Field f : resultados) System.out.println("  " + f);
    }

    private static void filtrarPorRangoPrecio() {
        System.out.print("Precio minimo: "); int min = leerEntero();
        System.out.print("Precio maximo: "); int max = leerEntero();
        List<Field> resultados = fieldService.buscarPorRangoPrecio(min, max);
        System.out.println("Canchas entre $" + min + " y $" + max + ":");
        if (resultados.isEmpty()) { System.out.println("  Ninguna."); return; }
        for (Field f : resultados) System.out.println("  " + f);
    }

    private static void preOrder() {
        System.out.println("\n-- Pre-Order --");
        for (Field f : fieldService.listarPreOrder()) System.out.println(f);
    }

    private static void agregarCancha() {
        System.out.print("ID: ");        String id       = sc.nextLine();
        System.out.print("Nombre: ");    String nombre   = sc.nextLine();
        System.out.print("Ubicacion: "); String ubicacion = sc.nextLine();
        System.out.print("Capacidad (5, 7 o 11): "); int cap    = leerEntero();
        System.out.print("Precio/hora: ");            int precio = leerEntero();
        fieldService.agregarCancha(new Field(id, nombre, ubicacion, cap, precio));
    }

    private static void cargarDatosDePrueba() {
        fieldService.agregarCancha(new Field("C1", "El Bosque",  "Suba",        7,  60000));
        fieldService.agregarCancha(new Field("C2", "La 80",      "Engativa",    5,  45000));
        fieldService.agregarCancha(new Field("C3", "Estadio",    "Teusaquillo", 11, 90000));
        fieldService.agregarCancha(new Field("C4", "Los Pinos",  "Usaquen",     5,  40000));
        fieldService.agregarCancha(new Field("C5", "Sur Norte",  "Kennedy",     7,  55000));
    }

    private static int leerEntero() {
        while (!sc.hasNextInt()) { sc.nextLine(); System.out.print("Numero valido: "); }
        int valor = sc.nextInt();
        sc.nextLine();
        return valor;
    }
}
