package com.bu.credit.backend.exception;

public class DebtServiceException extends RuntimeException {

  public DebtServiceException(String message) {
    super(message);
  }

  public DebtServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
