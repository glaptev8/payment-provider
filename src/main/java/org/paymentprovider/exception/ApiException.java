package org.paymentprovider.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
  private String errorCode;

  public ApiException(String message, String errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}
