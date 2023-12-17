package org.paymentprovider.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paymentprovider.dto.WebHookPayOutResponse;
import org.paymentprovider.dto.WebHookTransactionResponse;
import org.paymentprovider.entity.PayOut;
import org.paymentprovider.entity.Transaction;
import org.paymentprovider.exception.AccountException;
import org.paymentprovider.integration.payout.PayOutClient;
import org.paymentprovider.mapper.MapStructMapperImpl;
import org.paymentprovider.repository.PayOutRepository;
import org.paymentprovider.service.api.PayOutDao;
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
public class PayOutServiceImplTest {
  @Mock
  private PayOutDao payOutDao;
  @Spy
  private PayOutRepository payOutRepository;
  @Mock
  private PayOutClient payOutClient;
  @Mock
  private R2dbcEntityTemplate entityTemplate;
  @Spy
  private MapStructMapperImpl mapper;
  @InjectMocks
  private PayOutServiceImpl payOutService;

  private final String TEST_PAYOUT_FILE_NAME = "testentities/payout.json";

  @Test
  public void saveWithoutTransactionExceptionTest() {
    var payOut = JsonReader.read(TEST_PAYOUT_FILE_NAME, PayOut.class);
    var payOutDao = JsonReader.read(TEST_PAYOUT_FILE_NAME, PayOut.class);
    payOutDao.setMerchantId(1L);

    when(this.payOutDao.save(payOut)).thenReturn(Mono.just(payOutDao));
    when(payOutClient.notify(any())).thenReturn(Mono.just(WebHookPayOutResponse.builder().build()));

    StepVerifier.create(payOutService.save(payOut))
      .expectNextMatches(transactionSaved -> transactionSaved.equals(payOutDao))
      .verifyComplete();
  }

  @Test
  public void saveWithTransactionExceptionTest() {
    var transaction = JsonReader.read(TEST_PAYOUT_FILE_NAME, PayOut.class);
    var transactionFromDao = JsonReader.read(TEST_PAYOUT_FILE_NAME, PayOut.class);
    transactionFromDao.setMerchantId(1L);

    when(payOutDao.save(transaction)).thenReturn(Mono.just(transactionFromDao));
    when(payOutClient.notify(any())).thenThrow(new AccountException("account not found", "ACCOUNT_NOT_DOUND"));

    StepVerifier.create(payOutService.save(transaction))
      .expectErrorMatches(transactionSaved -> transactionSaved instanceof AccountException)
      .verify();
  }

  @Test
  public void getTransactionByIdTest() {
    var transaction = JsonReader.read(TEST_PAYOUT_FILE_NAME, PayOut.class);
    transaction.setId(1L);
    transaction.setMerchantId(1L);

    when(payOutRepository.findById(1L)).thenReturn(Mono.just(transaction));

    StepVerifier.create(payOutService.getPayOutById(1L))
      .expectNextMatches(transactionSaved -> transactionSaved.equals(transaction))
      .verifyComplete();
  }

  @Test
  public void getAllTransactionTest() {
    var payOut1 = JsonReader.read(TEST_PAYOUT_FILE_NAME, PayOut.class);
    var payOut2 = JsonReader.read(TEST_PAYOUT_FILE_NAME, PayOut.class);
    ReactiveSelect selectOperationMock = mock(ReactiveSelect.class);
    TerminatingSelect terminatingSelectMock = mock(TerminatingSelect.class);

    when(entityTemplate.select(PayOut.class))
      .thenReturn(selectOperationMock);
    when(entityTemplate.select(PayOut.class).matching(any()))
      .thenReturn(terminatingSelectMock);
    when(entityTemplate.select(PayOut.class).matching(any()).all())
      .thenReturn(Flux.just(payOut1, payOut2));


    StepVerifier.create(payOutService.getAllPayOut(null))
      .expectNext(payOut1, payOut2)
      .verifyComplete();
  }
}
