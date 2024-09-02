package com.github.marceloleite2604.chat.configuration;

import com.github.marceloleite2604.chat.domain.message.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Configuration
public class RouterConfiguration {

  @Bean
  public RouterFunction<ServerResponse> createMessageRouter(MessageHandler messageHandler) {
    return RouterFunctions.route()
      .path(MessageHandler.Paths.MESSAGES,
        messageRouteBuilder -> messageRouteBuilder
          .nest(
            path(MessageHandler.Paths.ID),
            messageIdRouteBuilder -> messageIdRouteBuilder
              .POST(messageHandler::update)
              .DELETE(messageHandler::delete)
              .GET(messageHandler::retrieve))
      )
      .GET(messageHandler::retrieveAll)
      .PUT(messageHandler::create)
      .build();
  }
}
