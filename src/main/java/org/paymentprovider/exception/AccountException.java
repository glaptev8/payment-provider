package org.paymentprovider.exception;

public class AccountException extends ApiException {

  public AccountException(String message, String errorCode) {
    super(message, errorCode);
  }
}
