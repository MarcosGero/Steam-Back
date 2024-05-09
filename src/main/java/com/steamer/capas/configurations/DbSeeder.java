package com.steamer.capas.configurations;
import com.steamer.capas.business.service.impl.PasswordEncoderService;
import com.steamer.capas.domain.document.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbSeeder implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;
    private final PasswordEncoderService passwordEncoderService;

    public DbSeeder(MongoTemplate mongoTemplate, PasswordEncoderService passwordEncoderService) {
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoderService = passwordEncoderService;
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User("username1", "user1@example.com", "Country1",
                passwordEncoderService.hashPassword("password1"));
        User user2 = new User( "username2", "user2@example.com", "Country2",
                passwordEncoderService.hashPassword("password2"));

        // Clear existing data
        mongoTemplate.dropCollection(User.class);

        // Insert initial data
        mongoTemplate.insert(user1, "users");
        mongoTemplate.insert(user2, "users");
    }
}