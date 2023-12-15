package org.paymentprovider.service;

import org.paymentprovider.entity.PayOut;
import org.paymentprovider.exception.AccountException;
import org.paymentprovider.exception.BalanceException;
import org.paymentprovider.repository.BalanceRepository;
import org.paymentprovider.repository.PayOutRepository;
import org.paymentprovider.service.api.MessageSourceService;
import org.paymentprovider.service.api.PayOutDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayOutDaoImpl implements PayOutDao {

  private final PayOutRepository payOutRepository;
  private final BalanceRepository balanceRepository;
  private final MessageSourceService messageSource;
  private final TransactionalOperator transactionalOperator;

  @Override
  public Mono<PayOut> save(PayOut payOut) {
    return transactionalOperator.transactional(
      balanceRepository
        .findByCurrencyAndMerchantId(payOut.getCurrency(), payOut.getMerchantId())
        .switchIfEmpty(Mono.defer(() -> {
          log.error(messageSource.getMessage("account.not.found.id.currency", payOut.getMerchantId(), payOut.getCurrency()));
          return Mono.error(new AccountException(messageSource.getMessage("account.not.found.id.currency", payOut.getMerchantId(), payOut.getCurrency()),
                                                 messageSource.getMessage("account.not.found.code")));
        }))
        .flatMap(balance -> {
          if (balance.getBalance().compareTo(payOut.getAmount()) < 0) {
            return Mono.error(new BalanceException(messageSource.getMessage("balance.not.enough"),
                                                   messageSource.getMessage("balance.not.enough.code")));
          }
          balance.setBalance(balance.getBalance().subtract(payOut.getAmount()));
          return balanceRepository
            .save(balance)
            .then(payOutRepository.save(payOut));
        })
    ).then(Mono.just(payOut));
  }
}
