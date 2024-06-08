package com.steamer.capas.domain.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpRequest {
    private String userName;
    private String email;
    private String password;
    private String country;
    private String profileVisibility;
    private String preferredLanguage;


    private String avatarUrl;
    private Boolean enableNotifications;
}
