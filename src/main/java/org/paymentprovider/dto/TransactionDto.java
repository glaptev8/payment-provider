package org.paymentprovider.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
  @JsonProperty("transactionId")
  private Long id;
  private String paymentMethod;
  private BigDecimal amount;
  private CurrencyType currency;
  private Long externalTransactionId;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
  private LocalDateTime createdAt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
  private LocalDateTime updatedAt;
  private String language;
  private String notificationUrl;
  private Customer customer;
  private TransactionStatusType status;
  private CardData cardData;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Customer {
    private String firstName;
    private String lastName;
    private String country;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CardData {
    private String cardNumber;
    private String cvv;
    @JsonFormat(pattern = "yy/MM")
    private LocalDate expDate;
  }
}
