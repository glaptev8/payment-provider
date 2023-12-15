package org.paymentprovider.integration.payout;


import java.util.HashMap;

import org.paymentprovider.config.IntegrationConfig;
import org.paymentprovider.dto.WebHookPayOutRequest;
import org.paymentprovider.dto.WebHookPayOutResponse;
import org.paymentprovider.dto.WebHookTransactionRequest;
import org.paymentprovider.dto.WebHookTransactionResponse;
import org.paymentprovider.integration.Sender;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Profile("!dev")
@RequiredArgsConstructor
public class PayOutClientImpl implements PayOutClient {

  private final Sender sender;
  private final IntegrationConfig integrationConfig;

  @Override
  public Mono<WebHookPayOutResponse> notify(WebHookPayOutRequest request) {
    var headers = new HttpHeaders();

    integrationConfig
      .getTransactionProperty()
      .getHeaders()
      .forEach(header -> headers.add(header.getKey(), header.getValue()));

    return sender.post(integrationConfig.getPayOutProperty().getBaseUrl(),
                       request,
                       new HashMap<>(),
                       headers,
                       WebHookPayOutResponse.class);
  }
}
