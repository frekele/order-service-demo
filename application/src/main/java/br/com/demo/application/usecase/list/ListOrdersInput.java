package br.com.demo.application.usecase.list;

public record ListOrdersInput(
        String status,
        String externalOrderId,
        int page,
        int size) {
    public static ListOrdersInput from(String status, String externalOrderId, int page, int size) {
        return new ListOrdersInput(status, externalOrderId, page, size);
    }
}