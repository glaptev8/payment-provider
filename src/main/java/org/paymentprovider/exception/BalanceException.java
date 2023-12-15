package org.paymentprovider.exception;

public class BalanceException extends ApiException {

  public BalanceException(String message, String errorCode) {
    super(message, errorCode);
  }
}
