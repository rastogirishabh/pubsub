package com.org.poc.pubsub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedExecutorProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.v1.stub.PublisherStubSettings;
import com.google.cloud.spring.autoconfigure.pubsub.GcpPubSubProperties;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.core.publisher.PubSubPublisherTemplate;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.DefaultPublisherFactory;
import com.google.cloud.spring.pubsub.support.DefaultSubscriberFactory;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import com.google.cloud.spring.pubsub.support.converter.PubSubMessageConverter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PubSubConfig {

  @Value("${project-id}")
  private String projectId;

  @Value("${credentials-file-path}")
  private String credentialsFilePath;

  @Bean
  public CredentialsProvider credentialsProvider() {
    return () ->
        ServiceAccountCredentials.fromStream(Files.newInputStream(Paths.get(credentialsFilePath)))
            .createScoped(PublisherStubSettings.getDefaultServiceScopes());
  }

  @Bean
  public PubSubMessageConverter pubSubMessageConverter(ObjectMapper objectMapper) {
    return new JacksonPubSubMessageConverter(objectMapper);
  }

  @Bean
  public PubSubPublisherTemplate pubSubPublisherTemplate(
      CredentialsProvider credentialsProvider, PubSubMessageConverter pubSubMessageConverter) {

    DefaultPublisherFactory factory = new DefaultPublisherFactory(() -> projectId);
    factory.setEnableMessageOrdering(true);
    factory.setCredentialsProvider(credentialsProvider);
    factory.setEndpoint("us-east1-pubsub.googleapis.com:443");

    PubSubPublisherTemplate template = new PubSubPublisherTemplate(factory);
    template.setMessageConverter(pubSubMessageConverter);
    return template;
  }

  @Bean
  public PubSubSubscriberTemplate subscriberTemplate(
      CredentialsProvider credentialsProvider, PubSubMessageConverter pubSubMessageConverter) {

    GcpPubSubProperties gcpPubSubProperties = new GcpPubSubProperties();
    gcpPubSubProperties.setKeepAliveIntervalMinutes(1);
    gcpPubSubProperties.initialize(projectId);

    DefaultSubscriberFactory factory =
        new DefaultSubscriberFactory(() -> projectId, gcpPubSubProperties);
    factory.setCredentialsProvider(credentialsProvider);
    factory.setExecutorProvider(FixedExecutorProvider.create(new ScheduledThreadPoolExecutor(1)));

    PubSubSubscriberTemplate template = new PubSubSubscriberTemplate(factory);
    template.setMessageConverter(pubSubMessageConverter);
    return template;
  }

  @Bean
  public PubSubTemplate pubSubTemplateBean(
      PubSubPublisherTemplate pubSubPublisherTemplate,
      PubSubSubscriberTemplate subscriberTemplate) {
    return new PubSubTemplate(pubSubPublisherTemplate, subscriberTemplate);
  }
}
