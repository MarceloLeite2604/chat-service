package com.github.marceloleite2604.chat.domain.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;

  public Mono<Message> save(Message message) {

    final var messageToBePersisted = message.toBuilder()
      .id(UUID.randomUUID())
      .time(LocalDateTime.now())
      .build();

    return messageRepository.save(messageToBePersisted);
  }

  public Mono<Message> findById(UUID id) {
    return messageRepository.findById(id);
  }

  public Flux<Message> findAll() {
    return messageRepository.findAll();
  }

  public Mono<Message> update(UUID id, Message updatedMessage) {
    return messageRepository.findById(id)
      .map(persistedMessage -> merge(persistedMessage, updatedMessage))
      .flatMap(messageRepository::save);
  }

  private Message merge(Message targetMessage, Message incomingMessage) {
    return targetMessage.toBuilder()
      .content(incomingMessage.getContent())
      .time(LocalDateTime.now())
      .build();
  }

  public Mono<Void> delete(UUID id) {
    return messageRepository.deleteById(id);
  }
}
