package com.imperial.utilidad;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encriptacion {
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private Encriptacion(){
        throw new UnsupportedOperationException("Es una clase de utileria y no debe ser instanciada");
    }
    
    /**
     * Genera un hash BCrypt de una contraseña en texto plano.
     * @param password El texto plano a encriptar.
     * @return El hash de la contraseña (ej. "$2a$10$...")
     */
    public static String hashPassword(String password) {
        return encoder.encode(password);
    }

    /**
     * Compara una contraseña en texto plano con un hash guardado.
     * @param plainPassword La contraseña que ingresó el usuario.
     * @param hashedPassword El hash que está guardado en la base de datos.
     * @return true si las contraseñas coinciden, false en otro caso.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}