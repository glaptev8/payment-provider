package org.paymentprovider.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paymentprovider.dto.WebHookTransactionResponse;
import org.paymentprovider.entity.Transaction;
import org.paymentprovider.exception.AccountException;
import org.paymentprovider.integration.transaction.TransactionClient;
import org.paymentprovider.mapper.MapStructMapperImpl;
import org.paymentprovider.repository.TransactionRepository;
import org.paymentprovider.service.api.TransactionDao;
import org.paymentprovider.util.JsonReader;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation.ReactiveSelect;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation.TerminatingSelect;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

  @Mock
  private TransactionDao transactionDao;
  @Spy
  private TransactionRepository transactionRepository;
  @Mock
  private TransactionClient transactionClient;
  @Mock
  private R2dbcEntityTemplate entityTemplate;
  @Spy
  MapStructMapperImpl mapper;
  @InjectMocks
  TransactionServiceImpl transactionService;

  private final String TEST_TRANSACTION_FILE_NAME = "testentities/transaction.json";

  @Test
  public void saveWithoutTransactionExceptionTest() {
    var transaction = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    var transactionFromDao = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    transactionFromDao.setMerchantId(1L);

    when(transactionDao.save(transaction)).thenReturn(Mono.just(transactionFromDao));
    when(transactionClient.notify(any())).thenReturn(Mono.just(WebHookTransactionResponse.builder().build()));

    StepVerifier.create(transactionService.save(transaction))
      .expectNextMatches(transactionSaved -> transactionSaved.equals(transactionFromDao))
      .verifyComplete();
  }

  @Test
  public void saveWithTransactionExceptionTest() {
    var transaction = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    var transactionFromDao = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    transactionFromDao.setMerchantId(1L);

    when(transactionDao.save(transaction)).thenReturn(Mono.just(transactionFromDao));
    when(transactionClient.notify(any())).thenThrow(new AccountException("account not found", "ACCOUNT_NOT_DOUND"));

    StepVerifier.create(transactionService.save(transaction))
      .expectErrorMatches(transactionSaved -> transactionSaved instanceof AccountException)
      .verify();
  }

  @Test
  public void getTransactionByIdTest() {
    var transaction = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    transaction.setId(1L);
    transaction.setMerchantId(1L);

    when(transactionRepository.findById(1L)).thenReturn(Mono.just(transaction));

    StepVerifier.create(transactionService.getTransactionById(1L))
      .expectNextMatches(transactionSaved -> transactionSaved.equals(transaction))
      .verifyComplete();
  }

  @Test
  public void getAllTransactionTest() {
    var transaction1 = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    var transaction2 = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    ReactiveSelect selectOperationMock = mock(ReactiveSelect.class);
    TerminatingSelect terminatingSelectMock = mock(TerminatingSelect.class);

    when(entityTemplate.select(Transaction.class))
      .thenReturn(selectOperationMock);
    when(entityTemplate.select(Transaction.class).matching(any()))
      .thenReturn(terminatingSelectMock);
    when(entityTemplate.select(Transaction.class).matching(any()).all())
      .thenReturn(Flux.just(transaction1, transaction2));


    StepVerifier.create(transactionService.getAllTransaction(null))
      .expectNext(transaction1, transaction2)
      .verifyComplete();
  }
}
