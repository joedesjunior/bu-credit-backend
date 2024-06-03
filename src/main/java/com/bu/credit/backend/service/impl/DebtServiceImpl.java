package com.bu.credit.backend.service.impl;

import com.bu.credit.backend.dto.request.DebtRequestDto;
import com.bu.credit.backend.dto.response.DebtResponseDto;
import com.bu.credit.backend.exception.DebtNotFoundException;
import com.bu.credit.backend.exception.DebtServiceException;
import com.bu.credit.backend.exception.ResourceNotFoundException;
import com.bu.credit.backend.mapper.DebtMapper;
import com.bu.credit.backend.repository.DebtRepository;
import com.bu.credit.backend.service.DebtService;
import com.bu.credit.backend.service.PaymentService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class DebtServiceImpl implements DebtService {

  private static final Logger logger = LoggerFactory.getLogger(DebtServiceImpl.class);

  private final DebtRepository debtRepository;
  private final DebtMapper debtMapper;
  private final PaymentService paymentService;

  @Autowired
  public DebtServiceImpl(DebtRepository debtRepository, DebtMapper debtMapper, PaymentService paymentService) {
    this.debtRepository = debtRepository;
    this.debtMapper = debtMapper;
    this.paymentService = paymentService;
  }

  @Override
  public Long saveDebt(DebtRequestDto debtRequestDto) {
    try {
      var debt = debtRepository.save(debtMapper.toEntity(debtRequestDto));
      logger.info("Debt registered successfully with ID: {}", debt.getId());
      return debt.getId();
    } catch (DataAccessException e) {
      logger.error("Database error occurred while registering debt", e);
      throw new DebtServiceException("Database error occurred while registering debt", e);
    } catch (Exception e) {
      logger.error("Unexpected error occurred while registering debt", e);
      throw new DebtServiceException("Unexpected error occurred while registering debt", e);
    }
  }

  @Override
  public List<DebtResponseDto> getDebtsByCreditorName(String creditorName) {
    try {
      var debts = debtRepository.findDebtByCreditorName(creditorName);
      if (debts.isEmpty())
        throw new ResourceNotFoundException(
            "No debts found for the given creditor name: " + creditorName);
      return debtMapper.toDebtReponseList(debts);
    } catch (DataAccessException e) {
      logger.error("Database error occurred while get debt by name", e);
      throw new DebtServiceException("Database error occurred while get debt by name", e);
    }
  }

  @Override
  public List<DebtResponseDto> getDebtsByStatus(String status) {
    try {
      var debts = debtRepository.findDebtByStatus(status);
      if (debts.isEmpty())
        throw new ResourceNotFoundException("No debts found for the given status: " + status);
      return debtMapper.toDebtReponseList(debts);
    } catch (DataAccessException e) {
      logger.error("Database error occurred while get debt by status", e);
      throw new DebtServiceException("Database error occurred while get debt by status", e);
    }
  }

  @Override
  public DebtResponseDto getDebtById(Long id) {
    try {
      var debt =
          debtRepository
              .findById(id)
              .orElseThrow(() -> new DebtNotFoundException("Debt not found for ID: " + id));

      var current = debtMapper.toDebtReponse(debt);
      var paymentHistory = paymentService.getPaymentByDebtId(debt.getId());
      current.setPaymentHistory(paymentHistory);

      return current;
    } catch (DataAccessException e) {
      logger.error("Database error occurred while get debt by id", e);
      throw new DebtServiceException("Database error occurred while get debt by id", e);
    }
  }
}
