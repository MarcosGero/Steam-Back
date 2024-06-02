package com.steamer.capas.persistence;


import com.steamer.capas.domain.document.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findByName(String name);
}