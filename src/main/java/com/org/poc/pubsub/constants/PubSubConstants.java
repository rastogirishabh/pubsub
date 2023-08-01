package com.org.poc.pubsub.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PubSubConstants {
  TOPIC("item-topic"),
  ORDERED_SUBSCRIPTION("item-topic-subscription"),
  UNORDERED_SUBSCRIPTION("");

  @Getter
  private final String value;
}
