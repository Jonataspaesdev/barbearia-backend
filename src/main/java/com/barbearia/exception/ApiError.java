package com.barbearia.exception;

import java.time.LocalDateTime;

public record ApiError(
        int status,
        String erro,
        Object mensagem,
        LocalDateTime timestamp
) {
    public static ApiError of(int status, String erro, Object mensagem) {
        return new ApiError(status, erro, mensagem, LocalDateTime.now());
    }
}
