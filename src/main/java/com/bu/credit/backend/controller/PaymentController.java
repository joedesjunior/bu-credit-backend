package com.bu.credit.backend.controller;

import com.bu.credit.backend.dto.request.PaymentRequestDto;
import com.bu.credit.backend.dto.response.ErrorResponseDto;
import com.bu.credit.backend.kafka.producer.PaymentProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

  private final PaymentProducerService paymentProducerService;

  @Autowired
  public PaymentController(PaymentProducerService paymentProducerService) {
    this.paymentProducerService = paymentProducerService;
  }

  @Operation(summary = "Register a new payment")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Payment registered successfully"),
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
  public ResponseEntity<HttpStatus> registerPayment(@Valid @RequestBody PaymentRequestDto paymentRequestDto) {
    paymentProducerService.sendPayment(paymentRequestDto);
    return ResponseEntity.ok(HttpStatus.OK);
  }
}
