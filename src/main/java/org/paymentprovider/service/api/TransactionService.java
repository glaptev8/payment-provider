package org.paymentprovider.service.api;

import org.paymentprovider.dto.TransactionSearchFilter;
import org.paymentprovider.entity.Transaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
  Mono<Transaction> save(Transaction transaction);

  Mono<Transaction> getTransactionById(Long id);

  Flux<Transaction> getAllTransaction(TransactionSearchFilter filter);
}
