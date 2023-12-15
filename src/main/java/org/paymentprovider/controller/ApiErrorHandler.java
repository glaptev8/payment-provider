package org.paymentprovider.controller;

import org.paymentprovider.dto.ErrorResponse;
import org.paymentprovider.dto.TransactionResponse;
import org.paymentprovider.dto.TransactionStatusType;
import org.paymentprovider.exception.AccountException;
import org.paymentprovider.exception.BalanceException;
import org.paymentprovider.exception.CredentialsNotPassedException;
import org.paymentprovider.exception.MerchantNotFoundException;
import org.paymentprovider.exception.SecretKeyNotValidException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Order(-2)
@Component
@RequiredArgsConstructor
public class ApiErrorHandler implements ErrorWebExceptionHandler {
  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    var response = exchange.getResponse();
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    if (ex instanceof AccountException e) {
      response.setStatusCode(HttpStatus.BAD_REQUEST);
      return wrapTransactionResponse(response, e.getErrorCode(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    if (ex instanceof BalanceException e) {
      response.setStatusCode(HttpStatus.BAD_REQUEST);
      return wrapTransactionResponse(response, e.getErrorCode(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    if (ex instanceof MerchantNotFoundException e) {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return wrapTransactionResponse(response, e.getErrorCode(), e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    if (ex instanceof SecretKeyNotValidException e) {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return wrapTransactionResponse(response, e.getErrorCode(), e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    if (ex instanceof CredentialsNotPassedException e) {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return wrapTransactionResponse(response, e.getErrorCode(), e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    return null;
  }

  private Mono<Void> wrapTransactionResponse(ServerHttpResponse response, String errorCode, String errorMessage, HttpStatus httpStatus) {
    try {
      return response
        .writeWith(Mono.just(response
                               .bufferFactory()
                               .wrap(objectMapper
                                       .writeValueAsString(new ResponseEntity<>(ErrorResponse.builder()
                                                                                  .code(errorCode)
                                                                                  .message(errorMessage)
                                                                                  .build(),
                                                                                httpStatus))
                                       .getBytes())));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
