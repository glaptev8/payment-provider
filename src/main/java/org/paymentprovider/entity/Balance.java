package org.paymentprovider.entity;

import java.math.BigDecimal;

import org.paymentprovider.dto.CurrencyType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Balance {
  @Id
  private Long id;
  private BigDecimal balance;
  private CurrencyType currency;
  private Long merchantId;
}
