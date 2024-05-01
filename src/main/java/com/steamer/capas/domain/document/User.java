package com.steamer.capas.domain.document;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {
    @Id
    private Long id;
    @Indexed(unique = true)
    private String userName;
    private String mail;
    private String country;
}
