package org.paymentprovider.integration.transaction;


import java.time.Duration;
import java.util.HashMap;

import org.paymentprovider.config.IntegrationConfig;
import org.paymentprovider.dto.WebHookTransactionRequest;
import org.paymentprovider.dto.WebHookTransactionResponse;
import org.paymentprovider.integration.Sender;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!dev && !test && !mock")
public class TransactionClientImpl implements TransactionClient {

  private final Sender sender;
  private final IntegrationConfig integrationConfig;

  @Override
  public Mono<WebHookTransactionResponse> notify(WebHookTransactionRequest request) {
    log.info("requesting webhook transaction {}", request);
    var headers = new HttpHeaders();

    integrationConfig
      .getTransactionProperty()
      .getHeaders()
      .forEach(header -> headers.add(header.getKey(), header.getValue()));

    return sender.post(integrationConfig.getTransactionProperty().getBaseUrl(),
                       request,
                       new HashMap<>(),
                       headers,
                       WebHookTransactionResponse.class)
      .retryWhen(Retry.backoff(3, Duration.ofMinutes(1)));
  }
}
