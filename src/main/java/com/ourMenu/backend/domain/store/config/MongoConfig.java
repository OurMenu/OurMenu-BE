package com.ourMenu.backend.domain.store.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.ourMenu")
public class MongoConfig extends AbstractMongoClientConfiguration{

    @Value("${spring.data.mongodb.url}")
    private String url;
    @Override
    protected String getDatabaseName() {
        return "ourmenu"; // 데이터베이스 이름 설정
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(url); // MongoDB 연결 URI 설정
    }

}