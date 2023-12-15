package org.paymentprovider.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayOutResponse {
  private Long payOutId;
  private PayOutStatusType status;
  private String message;
}
