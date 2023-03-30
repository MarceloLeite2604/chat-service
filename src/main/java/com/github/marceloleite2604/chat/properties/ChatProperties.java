package com.github.marceloleite2604.chat.properties;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@ConfigurationProperties(PropertiesPath.BASE_PATH)
@RequiredArgsConstructor
@Validated
@Getter
public class ChatProperties {

  @NotNull
  @DecimalMax(value = "1.0", message = "Value cannot be greater than 1.0.")
  @DecimalMin(value = "0.0", message = "Value cannot be lower than 0.")
  private final BigDecimal responseErrorRatio;
}
