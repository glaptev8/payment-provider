package org.paymentprovider.exception;

public class MerchantNotFoundException extends ApiException {

  public MerchantNotFoundException(String message, String errorCode) {
    super(message, errorCode);
  }
}
