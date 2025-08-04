package br.com.demo.infrastructure.mapper;

import br.com.demo.domain.model.Order;
import br.com.demo.domain.model.OrderItem;
import br.com.demo.infrastructure.persistence.document.OrderDocument;
import br.com.demo.infrastructure.persistence.document.OrderItemDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MoneyMapper.class})
public abstract class OrderMapper {

    public static final OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "totalValue", target = "totalValue", qualifiedByName = "moneyToBigDecimal")
    public abstract OrderDocument toDocument(Order order);

    @Mapping(source = "totalValue", target = "totalValue", qualifiedByName = "bigDecimalToMoney")
    public abstract Order toDomain(OrderDocument document);

    @Mapping(source = "unitPrice", target = "unitPrice", qualifiedByName = "moneyToBigDecimal")
    public abstract OrderItemDocument toOrderItemDocument(OrderItem orderItem);

    @Mapping(source = "unitPrice", target = "unitPrice", qualifiedByName = "bigDecimalToMoney")
    public abstract OrderItem toOrderItemDomain(OrderItemDocument orderItemDocument);

}
