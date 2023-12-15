package org.paymentprovider.integration.payout;

import org.paymentprovider.dto.WebHookPayOutRequest;
import org.paymentprovider.dto.WebHookPayOutResponse;
import org.paymentprovider.dto.WebHookTransactionRequest;
import org.paymentprovider.dto.WebHookTransactionResponse;
import org.paymentprovider.integration.Client;

import reactor.core.publisher.Mono;

public interface PayOutClient extends Client {
  Mono<WebHookPayOutResponse> notify(WebHookPayOutRequest request);
}
