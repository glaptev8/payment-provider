package org.paymentprovider.exception;

public class CredentialsNotPassedException extends ApiException {

  public CredentialsNotPassedException(String message, String errorCode) {
    super(message, errorCode);
  }
}
