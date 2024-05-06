package com.steamer.capas.domain.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
    private String username;          // Correo electrónico, requerido para la creación y opcional para la actualización
    private String password;       // Contraseña, requerido para la creación y opcional para la actualización
}
