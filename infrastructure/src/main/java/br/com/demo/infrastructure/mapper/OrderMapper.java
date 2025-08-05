package br.com.demo.infrastructure.mapper;

import br.com.demo.application.core.Pagination;
import br.com.demo.domain.model.Order;
import br.com.demo.domain.model.OrderItem;
import br.com.demo.infrastructure.openapi.order.model.OrderListPage;
import br.com.demo.infrastructure.persistence.document.OrderDocument;
import br.com.demo.infrastructure.persistence.document.OrderItemDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MoneyMapper.class, OffsetDateTimeMapper.class})
public abstract class OrderMapper {

    @Mapping(source = "totalValue", target = "totalValue", qualifiedByName = "moneyToBigDecimal")
    public abstract OrderDocument toDocument(Order order);

    @Mapping(source = "totalValue", target = "totalValue", qualifiedByName = "bigDecimalToMoney")
    public abstract Order toDomain(OrderDocument document);

    @Mapping(source = "unitPrice", target = "unitPrice", qualifiedByName = "moneyToBigDecimal")
    public abstract OrderItemDocument toOrderItemDocument(OrderItem orderItem);

    @Mapping(source = "unitPrice", target = "unitPrice", qualifiedByName = "bigDecimalToMoney")
    public abstract OrderItem toOrderItemDomain(OrderItemDocument orderItemDocument);

    @Mapping(source = "totalValue", target = "totalValue", qualifiedByName = "moneyToBigDecimal")
    public abstract br.com.demo.infrastructure.openapi.order.model.OrderResponse toOrderResponse(Order order);

    @Mapping(source = "unitPrice", target = "unitPrice", qualifiedByName = "moneyToBigDecimal")
    public abstract br.com.demo.infrastructure.openapi.order.model.OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    public abstract OrderListPage toOrderListPage(Pagination<Order> pagination);
}
