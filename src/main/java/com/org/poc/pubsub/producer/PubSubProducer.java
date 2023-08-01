package com.org.poc.pubsub.producer;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.org.poc.pubsub.constants.PubSubConstants;
import com.org.poc.pubsub.dto.Item;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PubSubProducer {

  private final PubSubTemplate pubSubTemplate;

  public void publish(List<Item> items, boolean orderingEnabled) {
    if (orderingEnabled) {
      publishWithOrdering(items);
    } else {
      publishWithoutOrdering(items);
    }
  }

  public String topic() {
    return PubSubConstants.TOPIC.getValue();
  }

  private void publishWithOrdering(List<Item> items) {
    items.forEach(
        item -> {
          log.info(
              "Publishing item (with ordering key) : [{}] with event description : [{}] ",
              item.getItemNumber(),
              item.getItemDescription());

          pubSubTemplate.publish(
              topic(), item, Collections.singletonMap(GcpPubSubHeaders.ORDERING_KEY, "1234"));
        });
  }

  private void publishWithoutOrdering(List<Item> items) {
    items.forEach(
        item -> {
          log.info(
              "Publishing item (without ordering key) : [{}] with event description : [{}] ",
              item.getItemNumber(),
              item.getItemDescription());
          pubSubTemplate.publish(topic(), item);
        });
  }
}
