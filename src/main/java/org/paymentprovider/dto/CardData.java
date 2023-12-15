package org.paymentprovider.dto;

import java.time.LocalDate;

import org.paymentprovider.deserializer.ExpLocalDateDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardData {
  private String cardNumber;
  private String cvv;
  @JsonDeserialize(using = ExpLocalDateDeserializer.class)
  private LocalDate expDate;
}
