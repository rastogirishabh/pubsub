package com.org.poc.pubsub.controller;

import com.org.poc.pubsub.dto.Item;
import com.org.poc.pubsub.producer.PubSubProducer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/pub-sub/publish")
@RequiredArgsConstructor
public class PubSubController {

  private final PubSubProducer producer;

  @PostMapping("/items")
  public String publishItems(
      @RequestBody List<Item> items, @RequestParam("orderingEnabled") boolean orderingEnabled) {

    producer.publish(items, orderingEnabled);

    return "Items Published! Check PubSub Console";
  }


}
