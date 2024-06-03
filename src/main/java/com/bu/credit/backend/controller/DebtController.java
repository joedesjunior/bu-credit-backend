package com.bu.credit.backend.controller;

import com.bu.credit.backend.dto.request.DebtRequestDto;
import com.bu.credit.backend.dto.response.DebtResponseDto;
import com.bu.credit.backend.dto.response.ErrorResponseDto;
import com.bu.credit.backend.enums.StatusDebt;
import com.bu.credit.backend.exception.DebtNotFoundException;
import com.bu.credit.backend.exception.DebtServiceException;
import com.bu.credit.backend.service.DebtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/debt")
public class DebtController {

  private static final Logger logger = LoggerFactory.getLogger(DebtController.class);

  private final DebtService debtService;

  @Autowired
  public DebtController(DebtService debtService) {
    this.debtService = debtService;
  }

  @Operation(summary = "Register a new debt")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Debt registered successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
      })
  @PostMapping
  public ResponseEntity<String> registerDebt(@Valid @RequestBody DebtRequestDto debtRequestDto) {
    try {
      var debt = debtService.saveDebt(debtRequestDto);
      return ResponseEntity.ok("Debt registered successfully with ID: " + debt);
    } catch (DebtServiceException e) {
      logger.error("Error registering debt", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error registering debt: " + e.getMessage());
    }
  }

  @Operation(summary = "Get debt by id")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Debts retrieved successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid id",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "No debts found for the given id",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
      })
  @GetMapping("/{id}")
  public ResponseEntity<DebtResponseDto> getDebtById(@NotNull @PathVariable Long id) {
    try {
      var debt = debtService.getDebtById(id);
      return ResponseEntity.ok(debt);
    } catch (DebtNotFoundException e) {
      return ResponseEntity.status(404).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(null);
    }
  }

  @Operation(summary = "Get debts by creditor name")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Debts retrieved successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "No debts found for the given creditor name",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
      })
  @GetMapping("/name")
  public ResponseEntity<List<DebtResponseDto>> getDebtsByCreditorName(
      @NotBlank @RequestParam String creditorName) {
    var debts = debtService.getDebtsByCreditorName(creditorName);
    return ResponseEntity.ok(debts);
  }

  @Operation(summary = "Get debts by status")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Debts retrieved successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid status",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "No debts found for the given status",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
      })
  @GetMapping("/status")
  public ResponseEntity<List<DebtResponseDto>> getDebtsByStatus(
      @NotBlank @RequestParam String status) {
    if (!StatusDebt.isValid(status)) return ResponseEntity.badRequest().body(null);
    var debts = debtService.getDebtsByStatus(status);
    return ResponseEntity.ok(debts);
  }
}
