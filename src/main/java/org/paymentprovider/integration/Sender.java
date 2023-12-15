package org.paymentprovider.integration;

import java.util.Map;

import org.springframework.http.HttpHeaders;

import reactor.core.publisher.Mono;

public interface Sender {
  <T, K> Mono<T> post(String url,
                      K requestEntity,
                      Map<String, String> params,
                      HttpHeaders headers,
                      Class<T> clazz);

  <K> Mono<K> get(String url,
                  Map<String, String> params,
                  HttpHeaders headers,
                  Class<K> clazz);
}
