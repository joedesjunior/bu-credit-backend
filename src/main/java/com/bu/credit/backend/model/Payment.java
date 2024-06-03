package com.bu.credit.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "payment")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "installmentNumber", nullable = false)
  private int installmentNumber;

  @Column(name = "amountPaid", nullable = false)
  private BigDecimal amountPaid;

  @Column(name = "paymentDate", nullable = false)
  private LocalDate paymentDate;

  @ManyToOne
  @JoinColumn(name = "debtId", nullable = false)
  private Debt debt;
}
