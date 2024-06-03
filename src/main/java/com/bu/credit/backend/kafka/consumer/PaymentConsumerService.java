package com.bu.credit.backend.kafka.consumer;

import com.bu.credit.backend.dto.request.PaymentRequestDto;
import com.bu.credit.backend.service.PaymentService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Data
public class PaymentConsumerService {

  private static final Logger logger = LoggerFactory.getLogger(PaymentConsumerService.class);

  private final PaymentService paymentService;

  @Autowired
  public PaymentConsumerService(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @KafkaListener(
      topics = "payment-topic",
      groupId = "payment-group",
      containerFactory = "jsonContainerFactory")
  public void listenerPayment(@Payload PaymentRequestDto paymentRequestDto) {
    logger.info("Reading payment received: {}", paymentRequestDto);
    paymentService.savePayment(paymentRequestDto);
  }
}
