package org.paymentprovider.entity;

import java.math.BigDecimal;
import java.util.Map;

import org.leantech.common.dto.PayOutStatusType;
import org.paymentprovider.dto.CurrencyType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table("payout")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayOut {
  @Id
  private Long id;
  private String paymentMethod;
  private BigDecimal amount;
  private CurrencyType currency;
  private Long externalTransactionId;
  private String cardNumber;
  private String language;
  private String notificationUrl;
  private String firstName;
  private String lastName;
  private String country;
  private PayOutStatusType status;
  private Long merchantId;

  @JsonProperty("card_data")
  private void unpackNestedCard(Map<String,Object> cardData) {
    this.cardNumber = (String)cardData.get("card_number");
  }

  @JsonProperty("customer")
  private void unpackNestedCustomer(Map<String,Object> cardData) {
    this.firstName = (String)cardData.get("first_name");
    this.lastName = (String)cardData.get("last_name");
    this.country = (String)cardData.get("country");
  }
}
