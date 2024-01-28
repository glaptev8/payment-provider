package org.paymentprovider.service;

import org.leantech.common.dto.PayOutStatusType;
import org.paymentprovider.dto.PayOutSearchFilter;
import org.paymentprovider.entity.PayOut;
import org.paymentprovider.integration.payout.PayOutClient;
import org.paymentprovider.mapper.MapStructMapper;
import org.paymentprovider.repository.PayOutRepository;
import org.paymentprovider.service.api.PayOutDao;
import org.paymentprovider.service.api.PayOutService;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayOutServiceImpl implements PayOutService {

  private final PayOutDao payOutDao;
  private final PayOutRepository payOutRepository;
  private final R2dbcEntityTemplate entityTemplate;
  private final PayOutClient payOutClientClient;
  private final MapStructMapper mapper;

  @Override
  public Mono<PayOut> save(PayOut payOut) {
    log.info("saving transaction {}", payOut);
    payOut.setStatus(PayOutStatusType.COMPLETED);
    return payOutDao.save(payOut)
      .doOnSuccess(transactionSaved -> log.info("transaction {} was saved", transactionSaved))
      .onErrorResume(throwable -> {
        log.error("error {}", throwable);
        payOut.setStatus(PayOutStatusType.FAILED);
        return payOutRepository.save(payOut)
          .flatMap(payOutSaved -> payOutClientClient.notify(mapper.payOutToRequest(payOutSaved))
            .map(response -> payOutSaved))
          .then(Mono.error(throwable));
      })
      .flatMap(payOutSaved ->
                 payOutClientClient.notify(mapper.payOutToRequest(payOutSaved))
                   .map(response -> payOutSaved));
  }

  @Override
  public Mono<PayOut> getPayOutById(Long id) {
    return payOutRepository.findById(id);
  }

  @Override
  public Flux<PayOut> getAllPayOut(PayOutSearchFilter filter) {
    var criteria = payOutRepository.createPayOutFilter(filter);
    return entityTemplate
      .select(PayOut.class)
      .matching(criteria)
      .all();
  }
}
