package br.com.demo.application.usecase.list;

import br.com.demo.application.core.Pagination;
import br.com.demo.domain.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListOrdersOutputTest {

    @Test
    @DisplayName("Should create ListOrdersOutput from a valid Pagination object")
    void givenValidPagination_whenCallingFrom_shouldReturnInstance() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final long expectedTotal = 0L;
        final List<Order> expectedItems = Collections.emptyList();
        final var aPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems);

        final var actualOutput = ListOrdersOutput.from(aPagination);

        assertNotNull(actualOutput);
        assertEquals(aPagination, actualOutput.pagination());
    }

    @Test
    @DisplayName("Should correctly instantiate ListOrdersOutput and verify its properties")
    void givenValidPagination_whenCreatingInstance_shouldHoldCorrectValues() {
        final var aPagination = new Pagination<>(1, 20, 100L, List.of(Order.builder().build()));

        final var anOutput = new ListOrdersOutput(aPagination);

        assertNotNull(anOutput.pagination());
        assertEquals(aPagination, anOutput.pagination());
    }
}