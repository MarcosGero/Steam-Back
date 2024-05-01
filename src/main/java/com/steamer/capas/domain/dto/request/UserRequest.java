package com.steamer.capas.domain.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    private String userName;
    private String mail;
    private String country;
    // password (?)
}
