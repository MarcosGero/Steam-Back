package com.steamer.capas.configurations;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.steamer.capas.domain.document.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    // Define the MongoClient bean
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://root:pass12345@localhost:27017/admin");
    }

    // Define the MongoTemplate using the MongoClient bean
    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, getDatabaseName());
    }

    @Override
    public boolean autoIndexCreation() {
        return true;
    }
    @Override
    protected String getDatabaseName() {
        return "steamdb";
    }
}
