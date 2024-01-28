package org.paymentprovider.controller;

import org.leantech.common.dto.PayOutResponse;
import org.paymentprovider.dto.PayOutDto;
import org.paymentprovider.dto.PayOutSearchFilter;
import org.paymentprovider.entity.PayOut;
import org.paymentprovider.mapper.MapStructMapper;
import org.paymentprovider.security.MerchantDetails;
import org.paymentprovider.service.api.PayOutService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/payout")
public class PayOutController {
  private final PayOutService payOutService;
  private final MapStructMapper mapper;

  @PostMapping
  public Mono<PayOutResponse> savePayOut(@RequestBody PayOut payOut,
                                         @AuthenticationPrincipal MerchantDetails userDetails) {
    payOut.setMerchantId(userDetails.getMerchantId());
    return payOutService.save(payOut)
      .map(payOutSaved -> PayOutResponse.builder()
        .payOutId(payOutSaved.getId())
        .status(payOutSaved.getStatus())
        .message("OK")
        .build());
  }

  @GetMapping("/list")
  public Flux<PayOutDto> transactionList(@RequestParam(name = "start_date", required = false) String startDate,
                                         @RequestParam(name = "end_date", required = false) String endDate) {
    return payOutService.getAllPayOut(new PayOutSearchFilter(startDate, endDate))
      .map(mapper::payOutToDto);
  }

  @GetMapping("/{payoutId}/details")
  public Mono<PayOutDto> transactionDetail(@PathVariable("payoutId") Long payOutId) {
    return payOutService.getPayOutById(payOutId)
      .map(mapper::payOutToDto);
  }
}
