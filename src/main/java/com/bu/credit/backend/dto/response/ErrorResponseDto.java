package com.bu.credit.backend.dto.response;

import lombok.Data;

@Data
public class ErrorResponseDto {

  private String message;
  private String title;
  private String detail;

  public ErrorResponseDto(String title, String detail) {
    this.title = title;
    this.detail = detail;
  }
}
