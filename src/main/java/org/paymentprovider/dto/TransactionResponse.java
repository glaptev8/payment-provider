package org.paymentprovider.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponse {
  private Long transactionId;
  private TransactionStatusType status;
  private String message;
}
