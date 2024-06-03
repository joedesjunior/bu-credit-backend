package com.bu.credit.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDate;

@Data
@Validated
public class DebtRequestDto {

  @NotNull(message = "Total amount is mandatory")
  private Double totalAmount;

  @NotBlank(message = "Creditor name is mandatory")
  private String creditorName;

  @NotNull(message = "Due date is mandatory")
  private LocalDate dueDate;

  @NotNull(message = "Number of installments is mandatory")
  private Integer numberOfInstallments;
}
