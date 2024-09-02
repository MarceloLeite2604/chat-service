package com.github.marceloleite2604.chat.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(PropertiesPath.MONGO)
@RequiredArgsConstructor
@Validated
@Getter
public class MongoProperties {

  @NotBlank(message = "Schema cannot be null, empty of blank.")
  private final String schema;

  @NotBlank(message = "Host cannot be null, empty or blank.")
  private final String host;

  @Range(min = 1, max = 65535, message = "Port must be between 1 and 65535.")
  private final int port;

  @NotBlank(message = "Username cannot be null, empty or blank.")
  private final String username;

  @NotBlank(message = "Password cannot be null, empty or blank.")
  private final String password;

  @NotBlank(message = "Database cannot be null, empty or blank.")
  private final String database;

  private final String authSource;
}
