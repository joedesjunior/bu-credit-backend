package com.bu.credit.backend.service.impl;

import com.bu.credit.backend.dto.request.PaymentRequestDto;
import com.bu.credit.backend.dto.response.PaymentResponseDto;
import com.bu.credit.backend.enums.StatusDebt;
import com.bu.credit.backend.mapper.PaymentMapper;
import com.bu.credit.backend.model.Debt;
import com.bu.credit.backend.model.Payment;
import com.bu.credit.backend.repository.DebtRepository;
import com.bu.credit.backend.repository.PaymentRepository;
import com.bu.credit.backend.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final PaymentMapper paymentMapper;
  private final DebtRepository debtRepository;

  @Autowired
  public PaymentServiceImpl(
      PaymentRepository paymentRepository,
      PaymentMapper paymentMapper,
      DebtRepository debtRepository) {
    this.paymentRepository = paymentRepository;
    this.paymentMapper = paymentMapper;
    this.debtRepository = debtRepository;
  }

  @Override
  public List<PaymentResponseDto> getPaymentByDebtId(Long debitId) {
    var payment = paymentRepository.findByDebtId(debitId);
    return paymentMapper.toPaymentReponse(payment);
  }

  @Override
  public void savePayment(PaymentRequestDto paymentRequestDto) {
    log.info("Starting savePayment with PaymentRequestDto: {}", paymentRequestDto);

    try {
      if (paymentRequestDto.getDebtId() == null) {
        log.error("DebtId is missing in PaymentRequestDto");
        throw new IllegalArgumentException("DebtId is required");
      }

      var debt =
          debtRepository
              .findById(paymentRequestDto.getDebtId())
              .orElseThrow(
                  () -> {
                    log.error("Debt not found with id {}", paymentRequestDto.getDebtId());
                    return new EntityNotFoundException(
                        "Debt not found with id " + paymentRequestDto.getDebtId());
                  });

      var payment = paymentMapper.toEntity(paymentRequestDto);
      payment.setDebt(debt);

      paymentRepository.save(payment);
      log.info("Payment saved successfully with id: {}", payment.getId());

      updatedDebt(debt, payment);
    } catch (IllegalArgumentException | EntityNotFoundException e) {
      log.error("Error in savePayment: {}", e.getMessage(), e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error in savePayment: {}", e.getMessage(), e);
      throw new RuntimeException("Unexpected error occurred while saving payment", e);
    }
  }

  private void updatedDebt(Debt debt, Payment payment) {
    updateStatus(debt.getTotalAmount(), payment.getAmountPaid(), debt);
    updateAmount(debt.getTotalAmount(), payment.getAmountPaid(), debt, payment);
    updateInstallment(debt.getNumberOfInstallments(), payment.getInstallmentNumber(), debt);

    debtRepository.save(debt);
    log.info("Debt updated to: {}", debt);
  }

  private void updateInstallment(Integer numberOfInstallments, int installmentNumber, Debt debt) {
    if (installmentNumber != 0) {
      debt.setNumberOfInstallments(numberOfInstallments - installmentNumber);
    }
  }

  public void updateAmount(Double totalAmount, BigDecimal amountPaid, Debt debt, Payment payment) {
    if (amountPaid == null) {
      throw new IllegalArgumentException("amountPaid cannot be null");
    }

    if (totalAmount != 0 && payment.getPaymentDate().isAfter(debt.getDueDate())) {
      totalAmount += totalAmount * 0.10;
      log.info("Interest applied. New totalAmount: {}", totalAmount);
    }

    var totalAmountBigDecimal = BigDecimal.valueOf(totalAmount);
    var updatedAmount = totalAmountBigDecimal.subtract(amountPaid);

    if (updatedAmount.compareTo(BigDecimal.ZERO) < 0) {
      updatedAmount = BigDecimal.ZERO;
    }

    debt.setTotalAmount(updatedAmount.doubleValue());
    log.info("Debt amount updated to: {}", debt.getTotalAmount());
  }

  public void updateStatus(Double totalAmount, BigDecimal amountPaid, Debt debt) {
    double remainingAmount = totalAmount - amountPaid.doubleValue();
    if (remainingAmount <= 0 && totalAmount <= 0) {
      debt.setStatus(StatusDebt.PAY.getName());
      log.info("Debt status updated to: {}", debt.getStatus());
    }
  }
}
