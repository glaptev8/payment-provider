package org.paymentprovider.integration.transaction;

import org.paymentprovider.dto.WebHookTransactionRequest;
import org.paymentprovider.dto.WebHookTransactionResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Profile({"dev || test || mock"})
public class TransactionClientImplMock implements TransactionClient {

  @Override
  public Mono<WebHookTransactionResponse> notify(WebHookTransactionRequest request) {
    log.info("requesting webhook transaction {}", request);
    return Mono.just(new WebHookTransactionResponse());
  }
}
