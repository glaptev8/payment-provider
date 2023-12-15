package org.paymentprovider.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TransactionSearchFilter {
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  public TransactionSearchFilter(String startDate, String endDate) {
    if (startDate != null) {
      Instant startDateInstant = Instant.ofEpochSecond(Long.parseLong(startDate));
      this.startDate = LocalDateTime.ofInstant(startDateInstant, ZoneId.systemDefault());
    }
    if (endDate != null) {
      Instant endDateInstant = Instant.ofEpochSecond(Long.parseLong(endDate));
      this.endDate = LocalDateTime.ofInstant(endDateInstant, ZoneId.systemDefault());
    }
  }

  public void setStartDate(String startDate) {
    Instant startDateInstant = Instant.ofEpochSecond(Long.parseLong(startDate));
    this.startDate = LocalDateTime.ofInstant(startDateInstant, ZoneId.systemDefault());
  }

  public void setEndDate(String endDate) {
    Instant endDateInstant = Instant.ofEpochSecond(Long.parseLong(endDate));
    this.endDate = LocalDateTime.ofInstant(endDateInstant, ZoneId.systemDefault());
  }
}
