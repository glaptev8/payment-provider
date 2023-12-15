package org.paymentprovider.service.api;

import org.paymentprovider.dto.PayOutSearchFilter;
import org.paymentprovider.entity.PayOut;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PayOutService {
  Mono<PayOut> save(PayOut transaction);

  Mono<PayOut> getPayOutById(Long id);

  Flux<PayOut> getAllPayOut(PayOutSearchFilter filter);
}
