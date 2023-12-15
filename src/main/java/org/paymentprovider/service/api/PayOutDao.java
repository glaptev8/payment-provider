package org.paymentprovider.service.api;

import org.paymentprovider.entity.PayOut;
import org.paymentprovider.entity.Transaction;

import reactor.core.publisher.Mono;

public interface PayOutDao {
  Mono<PayOut> save(PayOut transaction);
}
