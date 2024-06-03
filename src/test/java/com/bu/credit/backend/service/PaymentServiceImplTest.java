package com.bu.credit.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bu.credit.backend.dto.request.PaymentRequestDto;
import com.bu.credit.backend.mapper.PaymentMapper;
import com.bu.credit.backend.model.Debt;
import com.bu.credit.backend.model.Payment;
import com.bu.credit.backend.repository.DebtRepository;
import com.bu.credit.backend.repository.PaymentRepository;
import com.bu.credit.backend.service.impl.PaymentServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PaymentServiceImplTest {

  @Mock private PaymentRepository paymentRepository;

  @Mock private PaymentMapper paymentMapper;

  @Mock private DebtRepository debtRepository;

  @InjectMocks private PaymentServiceImpl paymentService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testGetPaymentByDebtId() {
    var debtId = 1L;
    List<Payment> payments = new ArrayList<>();
    var payment = new Payment();
    payments.add(payment);

    when(paymentRepository.findByDebtId(debtId)).thenReturn(payments);
    when(paymentMapper.toPaymentReponse(payments)).thenReturn(new ArrayList<>());

    var result = paymentService.getPaymentByDebtId(debtId);

    assertNotNull(result);
    assertEquals(0, result.size());

    verify(paymentRepository, times(1)).findByDebtId(debtId);
    verify(paymentMapper, times(1)).toPaymentReponse(payments);
  }

  @Test
  void testSavePayment() {
    var paymentRequestDto = new PaymentRequestDto();
    paymentRequestDto.setDebtId(1L);
    paymentRequestDto.setInstallmentNumber(1);
    paymentRequestDto.setAmountPaid(BigDecimal.TEN);
    paymentRequestDto.setPaymentDate(LocalDate.now());

    var debt = new Debt();
    debt.setId(1L);
    debt.setTotalAmount(100.0);
    debt.setDueDate(LocalDate.now());
    debt.setNumberOfInstallments(2);
    debt.setStatus("PENDING");

    Payment payment = new Payment();
    payment.setId(1L);
    payment.setAmountPaid(BigDecimal.valueOf(50.0));
    payment.setPaymentDate(LocalDate.now());

    when(debtRepository.findById(paymentRequestDto.getDebtId())).thenReturn(Optional.of(debt));
    when(paymentMapper.toEntity(paymentRequestDto)).thenReturn(payment);

    paymentService.savePayment(paymentRequestDto);

    verify(debtRepository, times(1)).findById(paymentRequestDto.getDebtId());
    verify(paymentMapper, times(1)).toEntity(paymentRequestDto);
    verify(paymentRepository, times(1)).save(payment);
    verify(debtRepository, times(1)).save(debt);
  }
}
