package org.paymentprovider.repository;

import org.paymentprovider.dto.CurrencyType;
import org.paymentprovider.entity.Balance;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Mono;

public interface BalanceRepository extends R2dbcRepository<Balance, Long> {
  Mono<Balance> findByCurrencyAndMerchantId(CurrencyType currencyType, Long merchantId);
}
