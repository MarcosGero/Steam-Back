package com.steamer.capas.persistence;
import com.steamer.capas.domain.document.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
