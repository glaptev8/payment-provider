package org.paymentprovider.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.paymentprovider.config.TestContainerConfig;
import org.paymentprovider.dto.TransactionDto;
import org.paymentprovider.dto.TransactionStatusType;
import org.paymentprovider.entity.Merchant;
import org.paymentprovider.entity.Transaction;
import org.paymentprovider.mapper.MapStructMapper;
import org.paymentprovider.repository.MerchantRepository;
import org.paymentprovider.repository.TransactionRepository;
import org.paymentprovider.util.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest extends TestContainerConfig {
  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private MerchantRepository merchantRepository;
  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private MapStructMapper mapper;

  private final String TEST_TRANSACTION_FILE_NAME = "testentities/transaction.json";

  @Test
  public void transactionListTest() {
    createTestMerchantAndTransactions();
    var transactions = webTestClient.get().uri("/api/v1/payments/transaction/list")
      .header("Authorization", "Basic dGVzdDpzZWNyZXQ=")
      .exchange()
      .expectStatus()
      .isOk()
      .returnResult(TransactionDto.class)
      .getResponseBody()
      .collectList()
      .block();
    cleanDb();

    Assertions.assertEquals(3, transactions.size());
  }

  @Test
  public void transactionListWithDateTest() {
    createTestMerchantAndTransactions();
    var transactions = webTestClient.get().uri("/api/v1/payments/transaction/list?start_date=1233540122&end_date=1328148122")
      .header("Authorization", "Basic dGVzdDpzZWNyZXQ=")
      .exchange()
      .expectStatus()
      .isOk()
      .returnResult(TransactionDto.class)
      .getResponseBody()
      .collectList()
      .block();
    cleanDb();

    Assertions.assertEquals(1, transactions.size());
  }

  @Test
  public void transactionDetailTest() {
    createTestMerchantAndTransactions();
    var transactionsFromDb = transactionRepository.findAll().collectList().block();

    var transaction = webTestClient.get().uri("/api/v1/payments/transaction/" + transactionsFromDb.get(0).getId() + "/details")
      .header("Authorization", "Basic dGVzdDpzZWNyZXQ=")
      .exchange()
      .expectStatus()
      .isOk()
      .returnResult(TransactionDto.class)
      .getResponseBody()
      .blockFirst();

    cleanDb();

    Assertions.assertEquals(transaction, mapper.transactionToDto(transactionsFromDb.get(0)));
  }

  private void cleanDb() {
    transactionRepository.deleteAll().block();
    merchantRepository.deleteAll().block();
  }
  private void createTestMerchantAndTransactions() {
    var transaction = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    var transaction2 = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    var transaction3 = JsonReader.read(TEST_TRANSACTION_FILE_NAME, Transaction.class);
    var merchant = Merchant.builder()
      .merchantName("test")
      .secret("secret")
      .build();
    var savedMerchant = merchantRepository.save(merchant).block();
    transaction.setMerchantId(savedMerchant.getId());
    transaction.setStatus(TransactionStatusType.APPROVED);
    transaction.setCreatedAt(LocalDateTime.of(2012, 2, 1, 1, 1));
    transaction2.setMerchantId(savedMerchant.getId());
    transaction2.setStatus(TransactionStatusType.APPROVED);
    transaction3.setMerchantId(savedMerchant.getId());
    transaction3.setStatus(TransactionStatusType.APPROVED);
    transactionRepository.saveAll(List.of(transaction, transaction2, transaction3)).collectList().block();
  }
}
