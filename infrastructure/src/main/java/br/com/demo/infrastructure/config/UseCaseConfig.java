package br.com.demo.infrastructure.config;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.application.usecase.create.CreateOrderUseCase;
import br.com.demo.application.usecase.create.DefaultCreateOrderUseCase;
import br.com.demo.application.usecase.get.DefaultGetOrderByIdUseCase;
import br.com.demo.application.usecase.get.GetOrderByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderGateway orderGateway) {
        return new DefaultCreateOrderUseCase(orderGateway);
    }

    @Bean
    public GetOrderByIdUseCase getOrderByIdUseCase(OrderGateway orderGateway) {
        return new DefaultGetOrderByIdUseCase(orderGateway);
    }


}
