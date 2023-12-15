package org.paymentprovider.config;

import org.paymentprovider.properties.integration.payout.PayOutProperty;
import org.paymentprovider.properties.integration.transaction.TransactionProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "integration")
public class IntegrationConfig {
  private TransactionProperty transactionProperty;
  private PayOutProperty payOutProperty;
}
