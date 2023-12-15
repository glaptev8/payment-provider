package org.paymentprovider.service;

import java.util.Locale;

import org.paymentprovider.service.api.MessageSourceService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageSourceServiceImpl implements MessageSourceService {

  private final MessageSource messageSource;

  @Override
  public String getMessage(String sourceKey, Object... objects) {
    return messageSource.getMessage(sourceKey, objects, Locale.getDefault());
  }
}
