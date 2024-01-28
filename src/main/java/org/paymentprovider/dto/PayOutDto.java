package org.paymentprovider.dto;

import java.math.BigDecimal;

import org.leantech.common.dto.PayOutStatusType;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayOutDto {
  @JsonProperty("payoutId")
  private Long id;
  private String paymentMethod;
  private BigDecimal amount;
  private CurrencyType currency;
  private Long externalTransactionId;
  private CardData cardData;
  private String language;
  private String notificationUrl;
  private Customer customer;
  private PayOutStatusType status;

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
  }
}
