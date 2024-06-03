package com.bu.credit.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum StatusDebt {
  PAY("pay"),
  PENDING("pending");

  private final String name;

  public static boolean isValid(String status) {
    return Arrays.stream(StatusDebt.values()).anyMatch(s -> s.getName().equalsIgnoreCase(status));
  }
}
