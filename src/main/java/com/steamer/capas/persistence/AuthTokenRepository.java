package com.steamer.capas.persistence;

import com.steamer.capas.domain.document.AuthToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthTokenRepository extends MongoRepository<AuthToken, String> {
    boolean existsByToken(String token);
    void deleteByToken(String token);
}
