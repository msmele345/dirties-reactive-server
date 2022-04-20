package com.mitchmele.dirtiesreactiveserver.config;


import com.mitchmele.dirtiesreactiveserver.repository.PottyEventRepository;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = PottyEventRepository.class)
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "Potty";
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }
}
