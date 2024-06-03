package com.bu.credit.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "debt")
public class Debt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "total_amount", nullable = false)
  private Double totalAmount;

  @Column(name = "creditor_name", nullable = false)
  private String creditorName;

  @Column(name = "due_date", nullable = false)
  private LocalDate dueDate;

  @Column(name = "number_of_installments", nullable = false)
  private Integer numberOfInstallments;

  @Column(name = "status", nullable = false)
  private String status;
}
