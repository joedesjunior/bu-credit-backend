package com.bu.credit.backend.exception;

public class DebtNotFoundException extends RuntimeException {
  public DebtNotFoundException(String message) {
    super(message);
  }
}
