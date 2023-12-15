package org.paymentprovider.repository;

import org.paymentprovider.entity.Merchant;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Mono;

public interface MerchantRepository extends R2dbcRepository<Merchant, Long> {
  Mono<Merchant> findByMerchantName(String merchantName);
}
