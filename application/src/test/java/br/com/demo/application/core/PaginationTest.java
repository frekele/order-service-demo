package br.com.demo.application.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PaginationTest {

    @Test
    @DisplayName("Should create pagination and get properties correctly")
    void shouldCreatePaginationAndGetProperties() {
        List<String> items = List.of("item1", "item2");
        int currentPage = 1;
        int perPage = 10;
        long total = 2;

        Pagination<String> pagination = new Pagination<>(currentPage, perPage, total, items);

        assertThat(pagination.currentPage()).isEqualTo(currentPage);
        assertThat(pagination.perPage()).isEqualTo(perPage);
        assertThat(pagination.total()).isEqualTo(total);
        assertThat(pagination.items()).isEqualTo(items);
        assertThat(pagination.items()).hasSize(2);
    }

    @Test
    @DisplayName("Should be equal for two instances with same values")
    void shouldBeEqualForSameValues() {
        List<String> items = List.of("item1", "item2");
        Pagination<String> pagination1 = new Pagination<>(1, 10, 2, items);
        Pagination<String> pagination2 = new Pagination<>(1, 10, 2, items);

        assertThat(pagination1).isEqualTo(pagination2);
        assertThat(pagination1.hashCode()).isEqualTo(pagination2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal for two instances with different values")
    void shouldNotBeEqualForDifferentValues() {
        List<String> items = List.of("item1", "item2");
        Pagination<String> basePagination = new Pagination<>(1, 10, 2, items);

        Pagination<String> differentCurrentPage = new Pagination<>(2, 10, 2, items);
        Pagination<String> differentPerPage = new Pagination<>(1, 20, 2, items);
        Pagination<String> differentTotal = new Pagination<>(1, 10, 3, items);
        Pagination<String> differentItems = new Pagination<>(1, 10, 2, List.of("item3"));

        assertThat(basePagination).isNotEqualTo(differentCurrentPage);
        assertThat(basePagination).isNotEqualTo(differentPerPage);
        assertThat(basePagination).isNotEqualTo(differentTotal);
        assertThat(basePagination).isNotEqualTo(differentItems);
    }

    @Test
    @DisplayName("Should not be equal to null or other types")
    void shouldNotBeEqualToNullOrOtherTypes() {
        Pagination<String> pagination = new Pagination<>(1, 10, 1, List.of("item"));

        assertThat(pagination).isNotEqualTo(null);
        assertThat(pagination).isNotEqualTo(new Object());
    }

    @Test
    @DisplayName("Should handle an empty list of items")
    void shouldHandleEmptyItemsList() {
        Pagination<String> pagination = new Pagination<>(1, 10, 0, Collections.emptyList());

        assertThat(pagination.items()).isNotNull().isEmpty();
        assertThat(pagination.total()).isZero();
    }

    @Test
    @DisplayName("Should handle a null list of items")
    void shouldHandleNullItemsList() {
        Pagination<String> pagination = new Pagination<>(1, 10, 0, null);

        assertThat(pagination.items()).isNull();
    }

    @Test
    @DisplayName("Should have a correct toString representation")
    void shouldHaveCorrectToStringFormat() {
        Pagination<String> pagination = new Pagination<>(1, 5, 1, List.of("item1"));
        String expectedToString = "Pagination[currentPage=1, perPage=5, total=1, items=[item1]]";

        assertThat(pagination.toString()).isEqualTo(expectedToString);
    }
}