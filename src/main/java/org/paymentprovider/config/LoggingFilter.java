package org.paymentprovider.config;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements WebFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    System.out.println("Received request: " + exchange.getRequest());

    return chain.filter(exchange)
      .then(Mono.fromRunnable(() ->
                                System.out.println("Response sent: " + exchange.getResponse().getStatusCode())));
  }
}
