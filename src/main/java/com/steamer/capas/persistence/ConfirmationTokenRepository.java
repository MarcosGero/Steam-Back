package com.steamer.capas.persistence;

import com.steamer.capas.domain.document.ConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

    // Metodo updateConfirmedAt (String token, LocalDateTime confirmedAt)
}
