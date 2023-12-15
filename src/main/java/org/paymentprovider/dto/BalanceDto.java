package org.paymentprovider.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDto {
  private BigDecimal balance;
  private CurrencyType currency;
  private MerchantDto merchant;
}
