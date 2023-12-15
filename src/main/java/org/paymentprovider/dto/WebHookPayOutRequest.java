package org.paymentprovider.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WebHookPayOutRequest {
  private String paymentMethod;
  private BigDecimal amount;
  private CurrencyType currency;
  private Long externalTransactionId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String type = "payOut";
  private Customer customer;
  private CardData cardData;
  private String language;
  private PayOutStatusType status;
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
