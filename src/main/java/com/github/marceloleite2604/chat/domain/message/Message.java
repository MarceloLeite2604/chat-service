package com.github.marceloleite2604.chat.domain.message;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Document("messages")
@NoArgsConstructor
public class Message {

  @Id
  private UUID id;

  private LocalDateTime time;

  private String user;

  private String content;
}
