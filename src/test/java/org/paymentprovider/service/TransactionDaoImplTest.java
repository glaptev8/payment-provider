package org.paymentprovider.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paymentprovider.entity.Balance;
import org.paymentprovider.entity.Transaction;
import org.paymentprovider.exception.AccountException;
import org.paymentprovider.repository.BalanceRepository;
import org.paymentprovider.repository.TransactionRepository;
import org.paymentprovider.service.api.MessageSourceService;
import org.paymentprovider.util.JsonReader;
import org.springframework.transaction.reactive.TransactionalOperator;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionDaoImplTest {
  @Mock
  TransactionRepository transactionRepository;

  @Mock
  BalanceRepository balanceRepository;

  @Mock
  TransactionalOperator transactionalOperator;

  @Mock
  MessageSourceService messageSource;

  @InjectMocks
  TransactionDaoImpl transactionDao;

  private final String TEST_TRANSACTION_FILE_NAME = "testentities/transaction.json";
  private final String TEST_BALANCE_FILE_NAME = "testentities/balance.json";

  @Test
  public void saveTest() {
    Transaction transaction = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    Transaction transactionSaved = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    Balance balance = JsonReader.read(TEST_BALANCE_FILE_NAME, Balance.class);
    Balance balanceSaved = JsonReader.read(TEST_BALANCE_FILE_NAME, Balance.class);
    transaction.setMerchantId(1L);
    transactionSaved.setId(1L);
    balance.setMerchantId(1L);
    balanceSaved.setId(1L);

    when(balanceRepository.findByCurrencyAndMerchantId(balance.getCurrency(), balance.getMerchantId())).thenReturn(Mono.just(balance));
    when(transactionalOperator.transactional(any(Mono.class))).thenReturn(Mono.just(transactionSaved));

    StepVerifier.create(transactionDao.save(transaction))
      .expectNext(transaction)
      .verifyComplete();
  }

  @Test
  public void saveAccountNotFoundTest() {
    Transaction transaction = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    Transaction transactionSaved = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    Balance balance = JsonReader.read(TEST_BALANCE_FILE_NAME, Balance.class);
    Balance balanceSaved = JsonReader.read(TEST_BALANCE_FILE_NAME, Balance.class);
    transaction.setMerchantId(1L);
    transactionSaved.setId(1L);
    balance.setMerchantId(1L);
    balanceSaved.setId(1L);

    when(balanceRepository.findByCurrencyAndMerchantId(balance.getCurrency(), balance.getMerchantId())).thenReturn(Mono.empty());
    when(transactionalOperator.transactional(any(Mono.class))).thenReturn(Mono.error(new AccountException("", "")));
    StepVerifier.create(transactionDao.save(transaction))
      .expectError(AccountException.class)
      .verify();
  }
}
