package org.paymentprovider.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.paymentprovider.dto.WebHookPayOutRequest.Customer;

import lombok.Data;

@Data
public class WebHookTransactionRequest {
  private Long providerTransactionId;
  private String paymentMethod;
  private BigDecimal amount;
  private CurrencyType currency;
  private String type = "transaction";
  private Long externalTransactionId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Customer customer;
  private CardData cardData;
  private String language;
  private TransactionStatusType status;
  private String message;

  @Data
  public static class Customer {
    private String firstName;
    private String lastName;
  }

  @Data
  public static class CardData {
    private String cardNumber;
  }
}
