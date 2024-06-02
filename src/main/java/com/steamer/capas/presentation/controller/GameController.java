package com.steamer.capas.presentation.controller;

import com.steamer.capas.domain.document.Game;
import com.steamer.capas.persistence.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

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

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable String id) {
        gameRepository.deleteById(id);
    }
}
