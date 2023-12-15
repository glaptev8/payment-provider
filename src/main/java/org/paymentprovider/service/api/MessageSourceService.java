package org.paymentprovider.service.api;

public interface MessageSourceService {
  String getMessage(String sourceKey, Object ... objects);
}
