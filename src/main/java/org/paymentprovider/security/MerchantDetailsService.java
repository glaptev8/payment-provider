package org.paymentprovider.security;

import org.paymentprovider.exception.CredentialsNotPassedException;
import org.paymentprovider.exception.MerchantNotFoundException;
import org.paymentprovider.exception.SecretKeyNotValidException;
import org.paymentprovider.repository.MerchantRepository;
import org.paymentprovider.service.api.MessageSourceService;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantDetailsService implements ReactiveUserDetailsService {

  private final MerchantRepository merchantRepository;
  private final MessageSourceService messageSource;

  @Override
  public Mono<UserDetails> findByUsername(String merchantName) {
    log.info("validation merchant {}", merchantName);
    if (StringUtils.hasLength(merchantName)) {
      return merchantRepository.findByMerchantName(merchantName)
        .switchIfEmpty(Mono.defer(() -> {
          log.error(messageSource.getMessage("merchant.not.found", merchantName));
          return Mono.error(new MerchantNotFoundException(messageSource.getMessage("merchant.not.found", merchantName),
                                                          messageSource.getMessage("merchant.not.found.code")));
        }))
        .flatMap(merchant -> {
          log.info("user was valid");
          return Mono.just(MerchantDetails.builder()
                             .merchantId(merchant.getId())
                             .merchantName(merchant.getMerchantName())
                             .secretKey(merchant.getSecret())
                             .build());
        });
    }
    return Mono.error(new CredentialsNotPassedException(messageSource.getMessage("credentials.not.passed"),
                                                        messageSource.getMessage("credentials.not.passed.code")));
  }
}
