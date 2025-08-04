package br.com.demo.infrastructure.mapper;

import br.com.demo.domain.valueobject.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public class MoneyMapper {

    @Named("moneyToBigDecimal")
    public BigDecimal moneyToBigDecimal(Money money) {
        return money != null ? money.getAmount() : null;
    }

    @Named("bigDecimalToMoney")
    public Money bigDecimalToMoney(BigDecimal amount) {
        return amount != null ? Money.of(amount) : null;
    }
}
