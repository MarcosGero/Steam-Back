package com.steamer.capas.persistence;

import com.steamer.capas.domain.document.ConfirmationToken;
import com.steamer.capas.domain.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

    /*@Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    */int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);
}
