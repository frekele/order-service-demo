package br.com.demo.application.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SearchQueryTest {

    @Test
    @DisplayName("Should correctly create a SearchQuery and retrieve its properties")
    void testConstructorAndAccessors() {
        int page = 1;
        int perPage = 20;
        String status = "APPROVED";
        String externalOrderId = "ext-order-12345";

        var searchQuery = new SearchQuery(page, perPage, status, externalOrderId);

        assertThat(searchQuery.page()).isEqualTo(page);
        assertThat(searchQuery.perPage()).isEqualTo(perPage);
        assertThat(searchQuery.status()).isEqualTo(status);
        assertThat(searchQuery.externalOrderId()).isEqualTo(externalOrderId);
    }

    @Test
    @DisplayName("Should correctly compare two SearchQuery instances")
    void testEqualsAndHashCode() {
        var query1 = new SearchQuery(1, 10, "PENDING", "ext-123");
        var query2 = new SearchQuery(1, 10, "PENDING", "ext-123");
        var query3 = new SearchQuery(2, 10, "PENDING", "ext-123");
        var query4 = new SearchQuery(1, 10, "APPROVED", "ext-456");

        assertThat(query1)
                .isEqualTo(query2)
                .hasSameHashCodeAs(query2);

        assertThat(query1)
                .isNotEqualTo(query3)
                .doesNotHaveSameHashCodeAs(query3);

        assertThat(query1)
                .isNotEqualTo(query4);

        assertThat(query1.equals(null)).isFalse();
        assertThat(query1.equals(new Object())).isFalse();
    }

    @Test
    @DisplayName("Should generate a correct string representation")
    void testToString() {
        var searchQuery = new SearchQuery(1, 15, "DELIVERED", "ext-789");

        String toStringResult = searchQuery.toString();

        assertThat(toStringResult).contains("page=1", "perPage=15", "status=DELIVERED", "externalOrderId=ext-789");
    }
}