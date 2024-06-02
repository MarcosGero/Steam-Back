package com.steamer.capas.configurations;
import com.steamer.capas.business.service.impl.PasswordEncoderService;
import com.steamer.capas.domain.document.Role;
import com.steamer.capas.domain.document.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class DbSeeder implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;
    private final PasswordEncoderService passwordEncoderService;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public DbSeeder(MongoTemplate mongoTemplate, PasswordEncoderService passwordEncoderService) {
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoderService = passwordEncoderService;
    }

    private static final Map<String, String> MIME_TYPES = new HashMap<>();
    static {
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("webp", "image/webp");
    }

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        mongoTemplate.dropCollection(User.class);

        // Create users
        User user1 = new User("username1", "user1@example.com", "Country1",
                passwordEncoderService.hashPassword("password1"));
        user1.setRole(Role.USER);
        user1.setAccountEnabled(true);
        user1.setImage(storeImage("static/image1.png"));

        User user2 = new User("username2", "user2@example.com", "Country2",
                passwordEncoderService.hashPassword("password2"));
        user2.setRole(Role.USER);
        user2.setAccountEnabled(true);
        user2.setImage(storeImage("static/image2.webp"));

        // Insert initial data
        mongoTemplate.insert(user1, "users");
        mongoTemplate.insert(user2, "users");
    }

    private String storeImage(String imagePath) {
        try {
            ClassPathResource resource = new ClassPathResource(imagePath);
            InputStream inputStream = resource.getInputStream();
            String filename = resource.getFilename();
            String extension = getExtension(filename);
            String contentType = MIME_TYPES.getOrDefault(extension, "application/octet-stream");
            return gridFsTemplate.store(inputStream, filename, contentType).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex + 1).toLowerCase();
    }
}