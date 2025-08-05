package br.com.demo.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Value Object: Money")
class MoneyTest {

    @Test
    @DisplayName("should create Money with correct scale and rounding")
    void of_shouldCreateMoneyWithCorrectScaleAndRounding() {
        BigDecimal valueToRoundUp = new BigDecimal("10.125");
        BigDecimal valueToRoundDown = new BigDecimal("10.124");
        BigDecimal valueWithNoDecimals = new BigDecimal("10");

        Money moneyA = Money.of(valueToRoundUp);
        Money moneyB = Money.of(valueToRoundDown);
        Money moneyC = Money.of(valueWithNoDecimals);

        assertEquals(new BigDecimal("10.13"), moneyA.getAmount());
        assertEquals(new BigDecimal("10.12"), moneyB.getAmount());
        assertEquals(new BigDecimal("10.00"), moneyC.getAmount());
    }

    @Test
    @DisplayName("should create Money with zero amount")
    void zero_shouldCreateMoneyWithZeroAmount() {
        Money zeroMoney = Money.zero();

        assertEquals(new BigDecimal("0.00"), zeroMoney.getAmount());
    }

    @Test
    @DisplayName("should add two Money values correctly")
    void add_shouldReturnCorrectSum() {
        Money moneyA = Money.of(new BigDecimal("10.50"));
        Money moneyB = Money.of(new BigDecimal("5.25"));

        Money result = moneyA.add(moneyB);

        assertEquals(Money.of(new BigDecimal("15.75")), result);
        assertEquals(new BigDecimal("15.75"), result.getAmount());
    }

    @Test
    @DisplayName("should multiply Money value correctly")
    void multiply_shouldReturnCorrectProduct() {
        Money money = Money.of(new BigDecimal("10.50"));
        int multiplier = 3;

        Money result = money.multiply(multiplier);

        assertEquals(Money.of(new BigDecimal("31.50")), result);
        assertEquals(new BigDecimal("31.50"), result.getAmount());
    }

    @Test
    @DisplayName("should handle equals and hashCode correctly")
    void equalsAndHashCode_shouldBehaveCorrectly() {
        Money moneyA1 = Money.of(new BigDecimal("100.00"));
        Money moneyA2 = Money.of(new BigDecimal("100.00"));
        Money moneyB = Money.of(new BigDecimal("200.00"));

        assertEquals(moneyA1, moneyA2);
        assertNotEquals(moneyA1, moneyB);
        assertEquals(moneyA1.hashCode(), moneyA2.hashCode());
        assertNotEquals(moneyA1.hashCode(), moneyB.hashCode());
        assertNotEquals(null, moneyA1);
        assertNotEquals(moneyA1, new Object());
    }

    @Test
    @DisplayName("should return plain string representation")
    void toString_shouldReturnPlainString() {
        Money money = Money.of(new BigDecimal("123.45"));

        String moneyString = money.toString();

        assertEquals("123.45", moneyString);
    }

    @Test
    @DisplayName("should throw NullPointerException when creating with null")
    void of_withNull_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> Money.of(null));
    }
}