package br.com.demo.infrastructure.mapper;

import br.com.demo.domain.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.*;

class MoneyMapperTest {

    private MoneyMapper moneyMapper;

    @BeforeEach
    void setUp() {
        moneyMapper = new MoneyMapper();
    }

    @Test
    @DisplayName("should convert Money to BigDecimal when Money is not null")
    void moneyToBigDecimal_whenMoneyIsNotNull_shouldReturnBigDecimal() {
        BigDecimal amount = new BigDecimal("123.45");
        Money money = Money.of(amount);

        BigDecimal result = moneyMapper.moneyToBigDecimal(money);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(new BigDecimal("123.45").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("should return null when converting Money to BigDecimal and Money is null")
    void moneyToBigDecimal_whenMoneyIsNull_shouldReturnNull() {
        BigDecimal result = moneyMapper.moneyToBigDecimal(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("should convert BigDecimal to Money when BigDecimal is not null")
    void bigDecimalToMoney_whenBigDecimalIsNotNull_shouldReturnMoney() {
        BigDecimal amount = new BigDecimal("987.65");

        Money result = moneyMapper.bigDecimalToMoney(amount);

        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("987.65").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("should return null when converting BigDecimal to Money and BigDecimal is null")
    void bigDecimalToMoney_whenBigDecimalIsNull_shouldReturnNull() {
        Money result = moneyMapper.bigDecimalToMoney(null);

        assertThat(result).isNull();
    }
}