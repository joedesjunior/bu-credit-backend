package com.bu.credit.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import com.bu.credit.backend.dto.request.DebtRequestDto;
import com.bu.credit.backend.dto.response.DebtResponseDto;
import com.bu.credit.backend.exception.DebtNotFoundException;
import com.bu.credit.backend.exception.DebtServiceException;
import com.bu.credit.backend.service.DebtService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class DebtControllerTest {

  private DebtController debtController;

  @Mock private DebtService debtService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    debtController = new DebtController(debtService);
  }

  @Test
  void testRegisterDebt_Success() throws DebtServiceException {
    var debtRequestDto = new DebtRequestDto();
    debtRequestDto.setTotalAmount(100.0);
    debtRequestDto.setCreditorName("John Doe");
    debtRequestDto.setDueDate(LocalDate.now());
    debtRequestDto.setNumberOfInstallments(3);

    when(debtService.saveDebt(debtRequestDto)).thenReturn(1L);

    var response = debtController.registerDebt(debtRequestDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Debt registered successfully with ID: 1", response.getBody());
    verify(debtService, times(1)).saveDebt(debtRequestDto);
  }

  @Test
  void testRegisterDebt_Exception() throws DebtServiceException {
    var debtRequestDto = new DebtRequestDto();
    debtRequestDto.setTotalAmount(100.0);
    debtRequestDto.setCreditorName("John Doe");
    debtRequestDto.setDueDate(LocalDate.now());
    debtRequestDto.setNumberOfInstallments(3);

    when(debtService.saveDebt(debtRequestDto))
        .thenThrow(new DebtServiceException("Error saving debt"));

    var response = debtController.registerDebt(debtRequestDto);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Error registering debt: Error saving debt", response.getBody());
    verify(debtService, times(1)).saveDebt(debtRequestDto);
  }

  @Test
  void testGetDebtById_Success() throws DebtNotFoundException {
    var id = 1L;
    var debtResponseDto = new DebtResponseDto();

    when(debtService.getDebtById(id)).thenReturn(debtResponseDto);

    var response = debtController.getDebtById(id);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(debtResponseDto, response.getBody());
    verify(debtService, times(1)).getDebtById(id);
  }

  @Test
  void testGetDebtById_DebtNotFoundException() throws DebtNotFoundException {
    Long id = 1L;

    when(debtService.getDebtById(id)).thenThrow(new DebtNotFoundException("Debt not found"));

    var response = debtController.getDebtById(id);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(debtService, times(1)).getDebtById(id);
  }

  @Test
  void testGetDebtById_Exception() throws DebtNotFoundException {
    Long id = 1L;

    when(debtService.getDebtById(id)).thenThrow(new RuntimeException("Internal server error"));

    var response = debtController.getDebtById(id);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
    verify(debtService, times(1)).getDebtById(id);
  }

  @Test
  void testGetDebtsByCreditorName_Success() {
    var creditorName = "John Doe";
    List<DebtResponseDto> debts = new ArrayList<>();

    when(debtService.getDebtsByCreditorName(creditorName)).thenReturn(debts);

    var response = debtController.getDebtsByCreditorName(creditorName);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(debts, response.getBody());
    verify(debtService, times(1)).getDebtsByCreditorName(creditorName);
  }

  @Test
  void testGetDebtsByStatus_InvalidStatus() {
    var status = "invalid";

    var response = debtController.getDebtsByStatus(status);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());
    verify(debtService, never()).getDebtsByStatus(status);
  }

  @Test
  void testGetDebtsByStatus_Success() {
    String status = "pay";
    List<DebtResponseDto> debts = new ArrayList<>();

    when(debtService.getDebtsByStatus(status)).thenReturn(debts);

    var response = debtController.getDebtsByStatus(status);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(debts, response.getBody());
    verify(debtService, times(1)).getDebtsByStatus(status);
  }
}
