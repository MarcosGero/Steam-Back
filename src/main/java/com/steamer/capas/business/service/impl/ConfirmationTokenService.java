package com.steamer.capas.business.service.impl;

import com.steamer.capas.domain.document.ConfirmationToken;
import com.steamer.capas.persistence.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    public int updateConfirmedAt(String token, LocalDateTime confirmedAt) {
        Query query = new Query(Criteria.where("token").is(token));
        Update update = new Update().set("confirmedAt", confirmedAt);
        var result = mongoTemplate.updateFirst(query, update, ConfirmationToken.class);
        return result.getModifiedCount() == 1 ? 1 : 0; // Simula el comportamiento del método original
    }
}
