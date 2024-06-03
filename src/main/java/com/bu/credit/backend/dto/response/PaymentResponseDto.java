package com.bu.credit.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PaymentResponseDto {

  private int installmentNumber;
  private BigDecimal amountPaid;
  private LocalDate paymentDate;
}
