package org.paymentprovider.controller;

import org.paymentprovider.dto.TransactionDto;
import org.paymentprovider.dto.TransactionResponse;
import org.paymentprovider.dto.TransactionSearchFilter;
import org.paymentprovider.entity.Transaction;
import org.paymentprovider.mapper.MapStructMapper;
import org.paymentprovider.security.MerchantDetails;
import org.paymentprovider.service.api.TransactionService;
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
@RequestMapping("/api/v1/payments/transaction")
public class TransactionController {
  private final TransactionService transactionService;
  private final MapStructMapper mapper;

  @PostMapping
  public Mono<TransactionResponse> saveTransaction(@RequestBody Transaction transaction,
                                                   @AuthenticationPrincipal MerchantDetails userDetails) {
    transaction.setMerchantId(userDetails.getMerchantId());
    return transactionService.save(transaction)
      .map(transactionSaved -> TransactionResponse.builder()
        .transactionId(transactionSaved.getId())
        .status(transactionSaved.getStatus())
        .message("OK")
        .build());
  }

  @GetMapping("/list")
  public Flux<TransactionDto> transactionList(@RequestParam(name = "start_date", required = false) String startDate,
                                              @RequestParam(name = "end_date", required = false) String endDate) {
    return transactionService.getAllTransaction(new TransactionSearchFilter(startDate, endDate))
      .map(mapper::transactionToDto);
  }

  @GetMapping("/{transactionId}/details")
  public Mono<TransactionDto> transactionDetail(@PathVariable("transactionId") Long transactionId) {
    return transactionService.getTransactionById(transactionId)
      .map(mapper::transactionToDto);
  }
}
