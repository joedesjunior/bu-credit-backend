package com.bu.credit.backend.service;

import com.bu.credit.backend.dto.request.DebtRequestDto;
import com.bu.credit.backend.dto.response.DebtResponseDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface DebtService {
  Long saveDebt(DebtRequestDto debtRequestDto);

  List<DebtResponseDto> getDebtsByCreditorName(String creditorName);

  List<DebtResponseDto> getDebtsByStatus(String creditorName);

  DebtResponseDto getDebtById(Long id);
}
