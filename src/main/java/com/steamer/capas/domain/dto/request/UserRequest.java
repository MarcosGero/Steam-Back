package com.steamer.capas.domain.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    private String userName;       // Nombre de usuario, requerido para la creación y opcional para la actualización
    private String email;          // Correo electrónico, requerido para la creación y opcional para la actualización
    private String password;       // Contraseña, requerido para la creación y opcional para la actualización
    private String country;        // País, podría ser opcional dependiendo de los requisitos del negocio
    private String profileVisibility;  // Visibilidad del perfil, opcional
    private String preferredLanguage;  // Idioma preferido, opcional

    // Otros campos que podrían ser relevantes dependiendo del flujo de la aplicación
    private String avatarUrl;          // URL para la imagen de perfil, opcional
    private Boolean enableNotifications;  // Si el usuario desea recibir notificaciones, opcional
}
