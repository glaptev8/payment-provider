package org.paymentprovider.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.leantech.common.dto.TransactionStatusType;
import org.paymentprovider.deserializer.ExpLocalDateDeserializer;
import org.paymentprovider.dto.CurrencyType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
  @Id
  private Long id;
  private String paymentMethod;
  private BigDecimal amount;
  private CurrencyType currency;
  private Long externalTransactionId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String cardNumber;
  private String cvv;
  @JsonDeserialize(using = ExpLocalDateDeserializer.class)
  private LocalDate expDate;
  private String language;
  private String notificationUrl;
  private String firstName;
  private String lastName;
  private String country;
  private TransactionStatusType status;
  private Long merchantId;

  @JsonProperty("card_data")
  private void unpackNestedCard(Map<String,Object> cardData) {
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");
    this.cardNumber = (String)cardData.get("card_number");
    String dateAsString = (String)cardData.get("exp_date");;
    this.expDate = LocalDate.parse("01/" + dateAsString, FORMATTER);
    this.cvv = (String)cardData.get("cvv");
  }

  @JsonProperty("customer")
  private void unpackNestedCustomer(Map<String,Object> cardData) {
    this.firstName = (String)cardData.get("first_name");
    this.lastName = (String)cardData.get("last_name");
    this.country = (String)cardData.get("country");
  }
}
