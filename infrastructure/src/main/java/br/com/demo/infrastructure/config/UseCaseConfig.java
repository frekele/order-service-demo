package br.com.demo.infrastructure.config;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.application.usecase.create.CreateOrderUseCase;
import br.com.demo.application.usecase.create.DefaultCreateOrderUseCase;
import br.com.demo.application.usecase.get.DefaultGetOrderByIdUseCase;
import br.com.demo.application.usecase.get.GetOrderByIdUseCase;
import br.com.demo.application.usecase.list.DefaultListOrdersUseCase;
import br.com.demo.application.usecase.list.ListOrdersUseCase;
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

    @Bean
    public ListOrdersUseCase listOrdersUseCase(OrderGateway orderGateway) {
        return new DefaultListOrdersUseCase(orderGateway);
    }

}
