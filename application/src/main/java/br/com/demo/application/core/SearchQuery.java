package br.com.demo.application.core;

public record SearchQuery(
        int page,
        int perPage,
        String status,
        String externalOrderId
) {
}
