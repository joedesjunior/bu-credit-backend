package com.bu.credit.backend.kafka.producer;

import com.bu.credit.backend.dto.request.PaymentRequestDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.io.Serializable;

@Service
@RequiredArgsConstructor
@Data
public class PaymentProducerService {

  private static final Logger logger = LoggerFactory.getLogger(PaymentProducerService.class);

  @Value("${kafka.producer.topic-name}")
  private String topicName;

  private final KafkaTemplate<String, Serializable> kafkaTemplate;

  public void sendPayment(PaymentRequestDto paymentDto) {
    logger.info("Sending message... Body: {}", paymentDto);
    kafkaTemplate.send(topicName, paymentDto);
  }
}
