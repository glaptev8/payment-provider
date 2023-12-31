package org.paymentprovider.integration.payout;

import org.paymentprovider.dto.WebHookPayOutRequest;
import org.paymentprovider.dto.WebHookPayOutResponse;
import org.paymentprovider.dto.WebHookTransactionRequest;
import org.paymentprovider.dto.WebHookTransactionResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Profile({"dev || test || mock"})
public class PayOutClientImplMock implements PayOutClient {
  @Override
  public Mono<WebHookPayOutResponse> notify(WebHookPayOutRequest request) {
    log.info("requesting webhook payout {}", request);
    return Mono.just(new WebHookPayOutResponse());
  }
}
