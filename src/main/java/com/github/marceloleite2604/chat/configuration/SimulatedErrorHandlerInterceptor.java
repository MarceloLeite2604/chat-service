package com.github.marceloleite2604.chat.configuration;

import com.github.marceloleite2604.chat.properties.ChatProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimulatedErrorHandlerInterceptor implements HandlerInterceptor {

  private static final Random RANDOM = new SecureRandom();

  private final ChatProperties chatProperties;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      if (chatProperties.getResponseErrorRatio()
            .compareTo(BigDecimal.valueOf(RANDOM.nextDouble())) > 0) {
      sendSimulatedError(response);
      return false;
    }

    return true;
  }

  private void sendSimulatedError(HttpServletResponse response) throws IOException {
    response.sendError(500, "Simulated error");
  }
}
