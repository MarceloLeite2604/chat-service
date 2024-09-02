package com.github.marceloleite2604.chat.configuration;

import com.github.marceloleite2604.chat.properties.MongoProperties;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.ReactiveMongoClientFactoryBean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
public class MongoConfiguration {

  @Bean
  public MongoClient createMongoClient(MongoProperties mongoProperties) {

    final var connectionString = createConnectionString(mongoProperties);


    final var mongoClientSettings = MongoClientSettings.builder()
      .applyConnectionString(connectionString)
      .uuidRepresentation(UuidRepresentation.STANDARD)
      .build();

    return MongoClients.create(mongoClientSettings);
  }

  private ConnectionString createConnectionString(MongoProperties mongoProperties) {
    final var connectionString = "%s://%s:%s@%s:%d/%s?authSource=%s".formatted(
      mongoProperties.getSchema(),
      mongoProperties.getUsername(),
      mongoProperties.getPassword(),
      mongoProperties.getHost(),
      mongoProperties.getPort(),
      mongoProperties.getDatabase(),
      mongoProperties.getAuthSource()
    );
    return new ConnectionString(connectionString);
  }
}
