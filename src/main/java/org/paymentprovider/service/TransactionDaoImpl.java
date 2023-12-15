package org.paymentprovider.service;

import org.paymentprovider.entity.Transaction;
import org.paymentprovider.exception.AccountException;
import org.paymentprovider.repository.BalanceRepository;
import org.paymentprovider.repository.TransactionRepository;
import org.paymentprovider.service.api.MessageSourceService;
import org.paymentprovider.service.api.TransactionDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionDaoImpl implements TransactionDao {

  private final TransactionRepository transactionRepository;
  private final BalanceRepository balanceRepository;
  private final MessageSourceService messageSource;
  private final TransactionalOperator transactionalOperator;

  @Override
  public Mono<Transaction> save(Transaction transaction) {
    return transactionalOperator.transactional(
      balanceRepository
        .findByCurrencyAndMerchantId(transaction.getCurrency(), transaction.getMerchantId())
        .switchIfEmpty(Mono.defer(() -> {
          log.error(messageSource.getMessage("account.not.found.id.currency", transaction.getMerchantId(), transaction.getCurrency()));
          return Mono.error(new AccountException(messageSource.getMessage("account.not.found.id.currency", transaction.getMerchantId(), transaction.getCurrency()),
                                                 messageSource.getMessage("account.not.found.code")));
        }))
        .flatMap(balance -> {
          balance.setBalance(balance.getBalance().add(transaction.getAmount()));
          return balanceRepository
            .save(balance)
            .then(transactionRepository.save(transaction));
        })
    ).then(Mono.just(transaction));
  }
}
