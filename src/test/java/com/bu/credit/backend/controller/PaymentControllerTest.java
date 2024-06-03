package com.bu.credit.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.bu.credit.backend.dto.request.PaymentRequestDto;
import com.bu.credit.backend.kafka.producer.PaymentProducerService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class PaymentControllerTest {

  private PaymentController paymentController;

  @Mock private PaymentProducerService paymentProducerService;

  private Validator validator;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    paymentController = new PaymentController(paymentProducerService);

    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @DisplayName(
      "Testa o caso em que uma requisição de pagamento válida é feita e espera-se que a resposta seja `HttpStatus.OK`. "
          + "Além disso, verifica se o método `sendPayment` do `paymentProducerService` foi chamado corretamente.")
  void registerPayment_ValidPaymentRequest_ReturnsOk() {
    var paymentRequestDto = new PaymentRequestDto();
    paymentRequestDto.setDebtId(1L);
    paymentRequestDto.setInstallmentNumber(1);
    paymentRequestDto.setAmountPaid(BigDecimal.valueOf(100));
    paymentRequestDto.setPaymentDate(LocalDate.now());

    var response = paymentController.registerPayment(paymentRequestDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(paymentProducerService).sendPayment(paymentRequestDto);
  }

  @Test
  @DisplayName(
      "Testa a validação do objeto `PaymentRequestDto` utilizando o `Validator`. "
          + "Verifica se existem 4 violações de validação esperadas.")
  void registerPayment_InvalidPaymentRequest_ValidationErrors() {
    var paymentRequestDto = new PaymentRequestDto();
    paymentRequestDto.setDebtId(null);
    paymentRequestDto.setAmountPaid(null);
    paymentRequestDto.setPaymentDate(null);

    var violations = validator.validate(paymentRequestDto);
    assertEquals(3, violations.size());
  }
}
