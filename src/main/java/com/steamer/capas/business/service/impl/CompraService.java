package com.steamer.capas.business.service.impl;

import com.steamer.capas.business.service.UserService;
import com.steamer.capas.domain.document.Game;
import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.persistence.GameRepository;
import com.steamer.capas.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompraService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserService userService;
    public boolean addGameToCarrito(String username, String gameId){

        User user = userRepository.findByUserName(username);
        if (user != null) {
            List<Game> game = gameRepository.findByName(gameId);

            if (!game.isEmpty() && game.get(0)!=null && !user.getCarritoGames().contains(gameId) && !user.getOwnedGames().contains(gameId)) {
                user.getCarritoGames().add(game.get(0).getName());
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public List<Game> getCarritoGames(String username) {
        User user = userRepository.findByUserName(username);
        if (user != null) {
            List<String> gameIds = user.getCarritoGames();
            return gameRepository.findByNameIn(gameIds);
        }
        return null;
    }

    public void clearCarritoGames(String username) {
        User user = userRepository.findByUserName(username);
        if(user!=null){
            user.getCarritoGames().clear();
            userRepository.save(user);
        }
    }

    public boolean removeGameFromCarrito(String username, String gameId) {
        User user = userRepository.findByUserName(username);
        if (user!=null) {
            if (user.getCarritoGames().contains(gameId)) {
                user.getCarritoGames().remove(gameId);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public List<Game> getOwnedGames(String username) {
        User user = userRepository.findByUserName(username);
        if (user != null) {
            List<String> gameNames = user.getOwnedGames();
            return gameNames.stream()
                    .map(gameRepository::findByName)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public boolean purchaseGame(String username, double totalPrice) {
        User user = userRepository.findByUserName(username);
        if (user.getCartera() < totalPrice) {
            return false;
        }

        List<String> cartGames = user.getCarritoGames();
        user.getOwnedGames().addAll(cartGames);
        user.setCarritoGames(new ArrayList<>());
        user.setCartera((float) (user.getCartera() - totalPrice));

        userRepository.save(user);
        return true;
    }
}
