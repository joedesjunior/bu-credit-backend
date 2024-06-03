package com.bu.credit.backend.kafka.config;

import java.util.HashMap;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@RequiredArgsConstructor
@Configuration
@Data
public class KafkaAdminConfig {

  @Value("${kafka.producer.topic-name}")
  private String topicName;

  private final KafkaProperties properties;

  @Bean
  public KafkaAdmin kafkaAdmin() {
    var configs = new HashMap<String, Object>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
    return new KafkaAdmin(configs);
  }

  @Bean
  public KafkaAdmin.NewTopics newTopics() {
    return new KafkaAdmin.NewTopics(TopicBuilder.name(topicName).partitions(2).build());
  }
}
