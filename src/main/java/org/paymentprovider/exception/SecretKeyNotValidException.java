package org.paymentprovider.exception;

public class SecretKeyNotValidException extends ApiException {

  public SecretKeyNotValidException(String message, String errorCode) {
    super(message, errorCode);
  }
}
