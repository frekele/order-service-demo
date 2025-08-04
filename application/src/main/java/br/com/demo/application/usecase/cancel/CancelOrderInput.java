package br.com.demo.application.usecase.cancel;

import java.util.UUID;

public record CancelOrderInput(
        UUID id
) {
}
