package com.steamer.capas.configurations;
import com.steamer.capas.business.service.impl.PasswordEncoderService;
import com.steamer.capas.domain.document.Game;
import com.steamer.capas.domain.document.Role;
import com.steamer.capas.domain.document.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class DbSeeder implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;
    private final PasswordEncoderService passwordEncoderService;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public DbSeeder(MongoTemplate mongoTemplate, PasswordEncoderService passwordEncoderService) {
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoderService = passwordEncoderService;
    }

    private static final Map<String, String> MIME_TYPES = new HashMap<>();
    static {
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("webp", "image/webp");
    }

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Game.class);
        // Create users
        User user1 = new User("username1", "user1@example.com", "Country1",
                passwordEncoderService.hashPassword("password1"));
        user1.setRole(Role.USER);
        user1.setAccountEnabled(true);
        user1.setImage(storeImage("static/image1.png"));

        User user2 = new User("username2", "user2@example.com", "Country2",
                passwordEncoderService.hashPassword("password2"));
        user2.setRole(Role.USER);
        user2.setAccountEnabled(true);
        user2.setImage(storeImage("static/image2.webp"));
        // Create games
        Game game1 = new Game(
                "Ghost of Tsushima: VERSION DEL DIRECTOR",
                "Recorre el camino de Jin Sakai y descubre la experiencia completa de Ghost of Tsushima en esta Versión del Director por primera vez para PC." ,
                "Se avecina una tormenta. Vive la experiencia completa de Ghost of Tsushima: Versión del Director para PC. Forja tu propio camino y descubre las maravillas de este juego de aventura y acción de mundo abierto, a cargo de Sucker Punch Productions, Nixxes Software y PlayStation Studios",
                LocalDate.of(2024, 5, 16),
                "Sucker Punch Productions",
                49.99, Arrays.asList("Action", "Adventure", "Open World"),
                Arrays.asList(
                        "ghost_video1.webm",
                        "ghost_1.jpg",
                        "ghost_2.jpg",
                        "ghost_3.jpg",
                        "ghost_4.jpg"

                ),
                "ghost_1.jpg");

        Game game2 = new Game(
                "EA SPORTS FC 24",
                "UEFA EURO 2024™\n" +
                        "Todo el mundo está incluido, tú también. Lidera a tu país contra los mejores y compite por la gloria internacional en el Juego de todos para conquistar el trono europeo.",
                "EA SPORTS FC™ 24 te da la bienvenida al Juego de Todos: la experiencia futbolística más realista hasta el momento con HyperMotionV, los Estilos de juego optimizados por Opta y un motor Frostbite™ mejorado.",
                LocalDate.of(2023, 8, 29),
                "EA Canada & EA Romania",
                69.99,
                Arrays.asList("Futbol", "Sport", "Multiplayer"),
                Arrays.asList(
                        "fc_video_1.webm",
                        "fc_video_2.webm",
                        "fc_1.jpg",
                        "fc_2.jpg",
                        "fc_3.jpg"
                ),
                "fc_thumbnail.png");

        Game game3 = new Game(
                "Elden Ring",
                "Elden Ring is an action role-playing game developed by FromSoftware and published by Bandai Namco Entertainment.",
                "In the game, players take on the role of a Tarnished, a character exiled from the Lands Between, who must traverse the realm to restore the Elden Ring and become the Elden Lord.",
                LocalDate.of(2022, 2, 25),
                "FromSoftware",
                59.99,
                Arrays.asList("RPG", "Fantasy", "Open World"),
                Arrays.asList(
                        "eldenring_video1.webm",
                        "eldenring_gameplay1.jpg",
                        "eldenring_gameplay2.jpg",
                        "eldenring_gameplay3.jpg",
                        "eldenring_gameplay4.jpg"
                ),
                "eldenring_thumbnail.png");

        // Insert initial data
        mongoTemplate.insert(user1, "users");
        mongoTemplate.insert(user2, "users");
        mongoTemplate.insert(game1, "games");
        mongoTemplate.insert(game2, "games");
        mongoTemplate.insert(game3, "games");
    }

    private String storeImage(String imagePath) {
        try {
            ClassPathResource resource = new ClassPathResource(imagePath);
            InputStream inputStream = resource.getInputStream();
            String filename = resource.getFilename();
            String extension = getExtension(filename);
            String contentType = MIME_TYPES.getOrDefault(extension, "application/octet-stream");
            return gridFsTemplate.store(inputStream, filename, contentType).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex + 1).toLowerCase();
    }
}