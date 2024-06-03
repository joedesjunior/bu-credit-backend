package com.bu.credit.backend.service;

import com.bu.credit.backend.dto.request.DebtRequestDto;
import com.bu.credit.backend.dto.response.DebtResponseDto;
import com.bu.credit.backend.exception.DebtNotFoundException;
import com.bu.credit.backend.exception.DebtServiceException;
import com.bu.credit.backend.exception.ResourceNotFoundException;
import com.bu.credit.backend.mapper.DebtMapper;
import com.bu.credit.backend.model.Debt;
import com.bu.credit.backend.repository.DebtRepository;
import com.bu.credit.backend.service.impl.DebtServiceImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class DebtServiceImplTest {

  @Mock private DebtRepository debtRepository;

  @Mock private DebtMapper debtMapper;

  @Mock private PaymentService paymentService;

  @InjectMocks private DebtServiceImpl debtService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testSaveDebt_Success() {
    var debtRequestDto = new DebtRequestDto();
    debtRequestDto.setTotalAmount(100.0);
    debtRequestDto.setCreditorName("John Doe");
    debtRequestDto.setDueDate(LocalDate.now());
    debtRequestDto.setNumberOfInstallments(3);

    Debt debt = new Debt();
    debt.setId(1L);

    Mockito.when(debtMapper.toEntity(debtRequestDto)).thenReturn(debt);
    Mockito.when(debtRepository.save(debt)).thenReturn(debt);

    Long savedDebtId = debtService.saveDebt(debtRequestDto);

    Assertions.assertEquals(1L, savedDebtId);
    Mockito.verify(debtRepository, Mockito.times(1)).save(debt);
    Mockito.verify(debtMapper, Mockito.times(1)).toEntity(debtRequestDto);
  }

  @Test
  void testSaveDebt_UnexpectedError() {
    DebtRequestDto debtRequestDto = new DebtRequestDto();
    debtRequestDto.setTotalAmount(100.0);
    debtRequestDto.setCreditorName("John Doe");
    debtRequestDto.setDueDate(LocalDate.now());
    debtRequestDto.setNumberOfInstallments(3);

    Mockito.when(debtMapper.toEntity(debtRequestDto)).thenReturn(new Debt());
    Mockito.when(debtRepository.save(ArgumentMatchers.any(Debt.class)))
        .thenThrow(RuntimeException.class);

    Assertions.assertThrows(DebtServiceException.class, () -> debtService.saveDebt(debtRequestDto));
    Mockito.verify(debtRepository, Mockito.times(1)).save(ArgumentMatchers.any(Debt.class));
    Mockito.verify(debtMapper, Mockito.times(1)).toEntity(debtRequestDto);
  }

  @Test
  void testGetDebtsByCreditorName_Success() {
    String creditorName = "John Doe";
    List<Debt> debts = new ArrayList<>();
    debts.add(new Debt());

    Mockito.when(debtRepository.findDebtByCreditorName(creditorName)).thenReturn(debts);
    Mockito.when(debtMapper.toDebtReponseList(debts)).thenReturn(new ArrayList<>());

    List<DebtResponseDto> debtResponseList = debtService.getDebtsByCreditorName(creditorName);

    Assertions.assertNotNull(debtResponseList);
    Mockito.verify(debtRepository, Mockito.times(1)).findDebtByCreditorName(creditorName);
    Mockito.verify(debtMapper, Mockito.times(1)).toDebtReponseList(debts);
  }

  @Test
  void testGetDebtsByCreditorName_NoDebtsFound() {
    String creditorName = "John Doe";

    Mockito.when(debtRepository.findDebtByCreditorName(creditorName)).thenReturn(new ArrayList<>());

    Assertions.assertThrows(
        ResourceNotFoundException.class, () -> debtService.getDebtsByCreditorName(creditorName));
    Mockito.verify(debtRepository, Mockito.times(1)).findDebtByCreditorName(creditorName);
    Mockito.verify(debtMapper, Mockito.never()).toDebtReponseList(ArgumentMatchers.anyList());
  }

  @Test
  void testGetDebtsByStatus_Success() {
    String status = "paid";
    List<Debt> debts = new ArrayList<>();
    debts.add(new Debt());

    Mockito.when(debtRepository.findDebtByStatus(status)).thenReturn(debts);
    Mockito.when(debtMapper.toDebtReponseList(debts)).thenReturn(new ArrayList<>());

    List<DebtResponseDto> debtResponseList = debtService.getDebtsByStatus(status);

    Assertions.assertNotNull(debtResponseList);
    Mockito.verify(debtRepository, Mockito.times(1)).findDebtByStatus(status);
    Mockito.verify(debtMapper, Mockito.times(1)).toDebtReponseList(debts);
  }

  @Test
  void testGetDebtsByStatus_NoDebtsFound() {
    String status = "paid";

    Mockito.when(debtRepository.findDebtByStatus(status)).thenReturn(new ArrayList<>());

    Assertions.assertThrows(
        ResourceNotFoundException.class, () -> debtService.getDebtsByStatus(status));
    Mockito.verify(debtRepository, Mockito.times(1)).findDebtByStatus(status);
    Mockito.verify(debtMapper, Mockito.never()).toDebtReponseList(ArgumentMatchers.anyList());
  }

  @Test
  void testGetDebtById_Success() {
    var id = 1L;
    var debt = new Debt();
    debt.setId(id);

    Mockito.when(debtRepository.findById(id)).thenReturn(Optional.of(debt));
    Mockito.when(debtMapper.toDebtReponse(debt)).thenReturn(new DebtResponseDto());
    Mockito.when(paymentService.getPaymentByDebtId(id)).thenReturn(new ArrayList<>());

    var debtResponseDto = debtService.getDebtById(id);

    Assertions.assertNotNull(debtResponseDto);
    Mockito.verify(debtRepository, Mockito.times(1)).findById(id);
    Mockito.verify(debtMapper, Mockito.times(1)).toDebtReponse(debt);
    Mockito.verify(paymentService, Mockito.times(1)).getPaymentByDebtId(id);
  }

  @Test
  void testGetDebtById_DebtNotFound() {
    Long id = 1L;

    Mockito.when(debtRepository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(DebtNotFoundException.class, () -> debtService.getDebtById(id));
    Mockito.verify(debtRepository, Mockito.times(1)).findById(id);
    Mockito.verify(debtMapper, Mockito.never()).toDebtReponse(ArgumentMatchers.any(Debt.class));
    Mockito.verify(paymentService, Mockito.never()).getPaymentByDebtId(ArgumentMatchers.anyLong());
  }
}
