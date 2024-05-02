package com.steamer.capas.business.service;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderService {

    private final Argon2 argon2 = Argon2Factory.create();

    public String hashPassword(String password) {
        // Parámetros ejemplo: 1 iteración, 65536 bytes de memoria, 1 hilo paralelo
        return argon2.hash(1, 65536, 1, password.toCharArray());
    }

    public void cleanup(String password) {
        argon2.wipeArray(password.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return argon2.verify(encodedPassword, rawPassword.toCharArray());
    }

}

