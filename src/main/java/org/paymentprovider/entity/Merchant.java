package org.paymentprovider.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {
  @Id
  private Long id;
  private String merchantName;
  private String secret;
}
