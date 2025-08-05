package br.com.demo.domain.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

class OrderStatusTest {

    @Test
    void shouldContainAllExpectedStatuses() {
        assertThat(OrderStatus.values()).containsExactlyInAnyOrder(
                OrderStatus.RECEIVED,
                OrderStatus.PROCESSING,
                OrderStatus.COMPLETED,
                OrderStatus.FAILED,
                OrderStatus.CANCELLED
        );
    }

    @Test
    void shouldHaveCorrectNumberOfStatuses() {
        assertThat(OrderStatus.values()).hasSize(5);
    }

    @ParameterizedTest
    @EnumSource(OrderStatus.class)
    void shouldReturnCorrectEnumForValidString(OrderStatus status) {
        String statusName = status.name();

        OrderStatus resolvedStatus = OrderStatus.valueOf(statusName);

        assertThat(resolvedStatus).isEqualTo(status);
    }

    @Test
    void shouldThrowExceptionForInvalidString() {
        String invalidStatusName = "UNKNOWN_STATUS";

        assertThatThrownBy(() -> OrderStatus.valueOf(invalidStatusName))
                .isInstanceOf(IllegalArgumentException.class);
    }
}