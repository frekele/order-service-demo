package br.com.demo.application.usecase.get;

import java.util.UUID;

public record GetOrderByIdInput(
        UUID id
) {
}