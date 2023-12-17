package org.paymentprovider.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paymentprovider.entity.Merchant;
import org.paymentprovider.repository.MerchantRepository;
import org.paymentprovider.service.MessageSourceServiceImpl;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MerchantDetailsServiceTest {
  @Mock
  private MerchantRepository merchantRepository;
  @InjectMocks
  private MessageSourceServiceImpl messageSource;

  @InjectMocks
  private MerchantDetailsService merchantDetailsService;

  @Test
  public void findByUsernameTest() {
    String merchantName = "test";
    var merchant = Merchant.builder()
      .id(1L)
      .merchantName(merchantName)
      .secret("secret")
      .build();
    var merchantDetails = MerchantDetails.builder()
      .merchantId(merchant.getId())
      .merchantName(merchantName)
      .secretKey(merchant.getSecret())
      .build();

    when(merchantRepository.findByMerchantName(merchantName)).thenReturn(Mono.just(merchant));

    StepVerifier.create(merchantDetailsService.findByUsername(merchantName))
      .expectNext(merchantDetails)
      .verifyComplete();
  }
}
