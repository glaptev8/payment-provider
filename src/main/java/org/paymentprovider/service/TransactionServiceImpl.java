package org.paymentprovider.service;

import org.leantech.common.dto.TransactionStatusType;
import org.paymentprovider.dto.TransactionSearchFilter;
import org.paymentprovider.entity.Transaction;
import org.paymentprovider.integration.transaction.TransactionClient;
import org.paymentprovider.mapper.MapStructMapper;
import org.paymentprovider.repository.TransactionRepository;
import org.paymentprovider.service.api.TransactionDao;
import org.paymentprovider.service.api.TransactionService;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionDao transactionDao;
  private final TransactionRepository transactionRepository;
  private final R2dbcEntityTemplate entityTemplate;
  private final TransactionClient transactionClient;
  private final MapStructMapper mapper;

  @Override
  public Mono<Transaction> save(Transaction transaction) {
    log.info("saving transaction {}", transaction);
    transaction.setStatus(TransactionStatusType.APPROVED);
    return transactionDao.save(transaction)
      .doOnSuccess(transactionSaved -> log.info("transaction {} was saved", transactionSaved))
      .onErrorResume(throwable -> {
        log.error("error {}", throwable);
        transaction.setStatus(TransactionStatusType.FAILED);
        return transactionRepository.save(transaction)
          .flatMap(transactionSaved -> transactionClient.notify(mapper.transactionToRequest(transactionSaved))
                  .map(response -> transactionSaved))
          .then(Mono.error(throwable));
      })
      .flatMap(transactionSaved ->
                 transactionClient.notify(mapper.transactionToRequest(transactionSaved))
                   .map(response -> transactionSaved));
  }

  @Override
  public Mono<Transaction> getTransactionById(Long id) {
    return transactionRepository.findById(id);
  }

  @Override
  public Flux<Transaction> getAllTransaction(TransactionSearchFilter filter) {
    var criteria = transactionRepository.createTransactionFilter(filter);
    return entityTemplate
      .select(Transaction.class)
      .matching(criteria)
      .all();
  }
}
