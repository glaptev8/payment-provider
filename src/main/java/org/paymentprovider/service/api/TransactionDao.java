package org.paymentprovider.service.api;

import org.paymentprovider.entity.Transaction;

import reactor.core.publisher.Mono;

public interface TransactionDao {
  Mono<Transaction> save(Transaction transaction);
}
