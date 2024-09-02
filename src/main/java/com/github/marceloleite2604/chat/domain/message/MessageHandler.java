package com.github.marceloleite2604.chat.domain.message;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageHandler {

  private static final UriBuilderFactory URI_BUILDER_FACTORY = new DefaultUriBuilderFactory();

  private final MessageToDtoMapper messageToDtoMapper;

  private final MessageService messageService;

  public Mono<ServerResponse> create(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(MessageDto.class)
      .flatMap(messageToDtoMapper::mapFrom)
      .flatMap(messageService::save)
      .flatMap(messageToDtoMapper::mapTo)
      .flatMap(messageDto -> elaborateCreateResponse(messageDto, serverRequest));
  }

  public Mono<ServerResponse> update(ServerRequest serverRequest) {

    return serverRequest.bodyToMono(MessageDto.class)
      .flatMap(messageToDtoMapper::mapFrom)
      .flatMap(message ->
        retrieveMessageId(serverRequest)
          .flatMap(id -> messageService.update(id, message)))
      .flatMap(messageToDtoMapper::mapTo)
      .then(ServerResponse.ok()
        .build());
  }

  public Mono<ServerResponse> retrieve(ServerRequest serverRequest) {
    return retrieveMessageId(serverRequest)
      .flatMap(messageService::findById)
      .flatMap(messageToDtoMapper::mapTo)
      .flatMap(messageDto -> ServerResponse.ok()
        .bodyValue(messageDto))
      .switchIfEmpty(ServerResponse.notFound()
        .build());
  }

  public Mono<ServerResponse> retrieveAll(ServerRequest serverRequest) {

    final var messageDtoFlux = messageService.findAll()
      .flatMap(messageToDtoMapper::mapTo);

    return ServerResponse.ok()
      .body(messageDtoFlux, MessageDto.class);
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    return retrieveMessageId(serverRequest)
      .flatMap(messageService::delete)
      .then(ServerResponse.ok()
        .build())
      .switchIfEmpty(ServerResponse.notFound()
        .build());
  }

  private Mono<UUID> retrieveMessageId(ServerRequest serverRequest) {
    return Mono.just(UUID.fromString(serverRequest.pathVariable("id")));
  }

  private Mono<ServerResponse> elaborateCreateResponse(MessageDto messageDto, ServerRequest serverRequest) {
    final var messageLocationUri = createMessageLocationUri(messageDto, serverRequest);
    return ServerResponse.created(messageLocationUri)
      .build();
  }

  private URI createMessageLocationUri(MessageDto messageDto, ServerRequest serverRequest) {

    return URI_BUILDER_FACTORY.uriString(serverRequest.uri().toASCIIString())
      .pathSegment(Paths.ID)
      .build(Map.of("id", messageDto.getId()));
  }

  @UtilityClass
  public static final class Parameters {
    private static final String ID = "id";
  }

  @UtilityClass
  public static final class Paths {
    public static final String MESSAGES = "messages";

    public static final String ID = "{" + Parameters.ID + "}";
  }
}
