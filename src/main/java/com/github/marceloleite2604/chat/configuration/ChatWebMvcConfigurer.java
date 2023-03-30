package com.github.marceloleite2604.chat.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChatWebMvcConfigurer implements WebMvcConfigurer {

  private final Set<HandlerInterceptor> handlerInterceptors;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    if (!CollectionUtils.isEmpty(handlerInterceptors)) {
      handlerInterceptors.forEach(registry::addInterceptor);
    }
  }
}
