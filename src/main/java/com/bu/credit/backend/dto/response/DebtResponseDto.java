package com.bu.credit.backend.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class DebtResponseDto {
  private Double totalAmount;

  private String creditorName;

  private String status;

  private LocalDate dueDate;

  private Integer numberOfInstallments;

  private List<PaymentResponseDto> paymentHistory;
}
