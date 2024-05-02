package com.steamer.capas.configurations;

import com.mongodb.client.MongoClient;
import com.steamer.capas.domain.document.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    private final MongoClient mongoClient;

    public MongoConfig(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate template = new MongoTemplate(mongoClient, "steamdb");
        if (!template.collectionExists(User.class)) {
            template.createCollection(User.class);
        }
        return template;
    }
}
