package com.bu.credit.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class PaymentRequestDto implements Serializable {
  @NotNull(message = "idDebt is mandatory")
  private Long debtId;

  @NotNull(message = "installmentNumber is mandatory")
  private int installmentNumber;

  @NotNull(message = "amountPaid is mandatory")
  private BigDecimal amountPaid;

  @NotNull(message = "paymentDate is mandatory")
  private LocalDate paymentDate;
}
