package org.paymentprovider.integration.transaction;

import org.paymentprovider.dto.WebHookTransactionRequest;
import org.paymentprovider.dto.WebHookTransactionResponse;
import org.paymentprovider.integration.Client;

import reactor.core.publisher.Mono;

public interface TransactionClient extends Client {
  Mono<WebHookTransactionResponse> notify(WebHookTransactionRequest request);
}
