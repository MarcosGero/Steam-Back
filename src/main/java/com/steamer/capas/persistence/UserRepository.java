package com.steamer.capas.persistence;

import com.steamer.capas.domain.document.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    public User findByEmail(String mail);
    public User findByUserName(String username);
    public boolean existsByUserName(String userName);
    public void deleteByUserName(String userName);
    public int enableUser(String mail);
}
