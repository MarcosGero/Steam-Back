package com.steamer.capas.domain.dto.request;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class LoginRequest {
    private String email;
    private String password;


}