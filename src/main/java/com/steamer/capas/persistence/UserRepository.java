package com.steamer.capas.persistence;

<<<<<<< Updated upstream
public interface UserRepository {
=======
import com.steamer.capas.domain.document.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    User findByUserName(String username);

    User findByEmail(String email);
>>>>>>> Stashed changes
}
