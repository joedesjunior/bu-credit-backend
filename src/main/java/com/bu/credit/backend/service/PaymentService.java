package com.bu.credit.backend.service;

import com.bu.credit.backend.dto.request.PaymentRequestDto;
import com.bu.credit.backend.dto.response.PaymentResponseDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    List<PaymentResponseDto> getPaymentByDebtId(Long id);
    void savePayment(PaymentRequestDto paymentRequestDto);
}
