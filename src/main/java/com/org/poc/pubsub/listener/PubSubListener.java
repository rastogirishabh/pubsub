package com.org.poc.pubsub.listener;

import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage;
import com.org.poc.pubsub.constants.PubSubConstants;
import com.org.poc.pubsub.dto.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PubSubListener {
  @Bean
  public Subscriber itemListener(PubSubTemplate pubSubTemplate) {

    return pubSubTemplate.subscribeAndConvert(getSubscription(), this::handleMessage, Item.class);
  }

  public void handleMessage(ConvertedBasicAcknowledgeablePubsubMessage<Item> message) {
    Item payload = message.getPayload();
    log.info(
        "Item received with item ID : [{}] and event description : [{}] with ordering key [{}]",
        payload.getItemNumber(),
        payload.getItemDescription(),
        message.getPubsubMessage().getOrderingKey());

    message.ack();
  }

  public String getSubscription() {
    return PubSubConstants.ORDERED_SUBSCRIPTION.getValue();
  }
}
