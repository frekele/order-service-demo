package br.com.demo.application.core;

import java.util.List;

public record Pagination<T>(
        int currentPage,
        int perPage,
        long total,
        List<T> items
) {
}