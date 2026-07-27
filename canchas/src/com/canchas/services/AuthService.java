package com.canchas.services;

import com.canchas.models.User;
import com.canchas.structures.HashTable;

public class AuthService {
    private HashTable<String, User> userTable;

    public AuthService() {
        this.userTable = new HashTable<>();
    }

    // registrar un usuario nuevo
    public boolean register(String id, String username, String password, String name) {
        if (userTable.get(username) != null) {
            System.out.println("-> [ERROR]: El nombre de usuario '" + username + "' ya esta registrado.");
            return false;
        }

        User newUser = new User(id, username, password, name);
        userTable.put(username, newUser);
        System.out.println("-> [EXITO]: Usuario '" + name + "' registrado correctamente.");
        return true;
    }

    // inciciar seccion
    public User login(String username, String password) {
        User user = userTable.get(username);

        if (user == null || !user.getPassword().equals(password)) {
            System.out.println("-> [ERROR]: Credenciales incorrectas.");
            return null;
        }

        System.out.println("-> [BIENVENIDO]: Inicio de sesion exitoso. Hola, " + user.getName() + ".");
        return user;
    }
}