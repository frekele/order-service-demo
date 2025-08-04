package br.com.demo.infrastructure.config;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.application.usecase.create.CreateOrderUseCase;
import br.com.demo.application.usecase.create.DefaultCreateOrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderGateway orderGateway) {
        return new DefaultCreateOrderUseCase(orderGateway);
    }

}
