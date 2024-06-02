package com.steamer.capas.domain.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "games")
@Getter
@Setter

public class Game {
    @Id
    private String id;
    private String name;
    private String description;
    private String details;
    private LocalDate launchDate;
    private String developer;
    private double price;
    private List<String> categories;
    private List<String>  imageUrl; // Campo para la URL de la imagen
    private String  thumbnail; // Campo para la URL de la imagen

    public Game(String name, String description, String details, LocalDate launchDate, String developer, double price, List<String> categories, List<String> imageUrl, String thumbnail) {
        this.name = name;
        this.description = description;
        this.details = details;
        this.launchDate = launchDate;
        this.developer = developer;
        this.price = price;
        this.categories = categories;
        this.imageUrl = imageUrl;
        this.thumbnail = thumbnail;
    }
}
