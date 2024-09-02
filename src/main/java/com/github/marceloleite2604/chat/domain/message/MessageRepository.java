package com.github.marceloleite2604.chat.domain.message;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends ReactiveMongoRepository<Message, UUID> {
}
