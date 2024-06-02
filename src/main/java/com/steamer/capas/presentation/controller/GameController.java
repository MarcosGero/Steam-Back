package com.steamer.capas.presentation.controller;

import com.steamer.capas.domain.document.Game;
import com.steamer.capas.persistence.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;
    private static final String UPLOAD_DIR = "uploads/";
    @GetMapping
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }
    @GetMapping("/name/{name}")
    public List<Game> getGamesByName(@PathVariable String name) {
        return gameRepository.findByName(name);
    }
    @GetMapping("/{id}")
    public Optional<Game> getGameById(@PathVariable String id) {
        return gameRepository.findById(id);
    }

    @PostMapping
    public Game createGame(@RequestBody Game game) {
        return gameRepository.save(game);
    }
    @PutMapping("/{id}")
    public Game updateGame(@PathVariable String id, @RequestBody Game updatedGame) {
        return gameRepository.findById(id)
                .map(game -> {
                    game.setName(updatedGame.getName());
                    game.setDescription(updatedGame.getDescription());
                    game.setLaunchDate(updatedGame.getLaunchDate());
                    game.setDeveloper(updatedGame.getDeveloper());
                    game.setPrice(updatedGame.getPrice());
                    game.setCategories(updatedGame.getCategories());
                    return gameRepository.save(game);
                })
                .orElseGet(() -> {
                    updatedGame.setId(id);
                    return gameRepository.save(updatedGame);
                });
    }
    @PostMapping("/{id}/uploadImage")
    public String uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isEmpty()) {
            return "Game not found";
        }
        Game game = optionalGame.get();

        // Crear el directorio si no existe
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Guardar la imagen
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, file.getBytes());

        // Construir la URL de la imagen
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();

        // Añadir la URL de la imagen al juego
        game.getImageUrl().add(imageUrl);
        gameRepository.save(game);

        return imageUrl;
    }
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("No se puede leer el archivo: " + filename);
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable String id) {
        gameRepository.deleteById(id);
    }
}
