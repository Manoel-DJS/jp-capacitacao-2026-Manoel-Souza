package jp_2026_Manoel_Souza.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovimentarEstoqueRequest(

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade,

        @NotBlank(message = "O motivo é obrigatório")
        String motivo,

        Long referenciaId,

        String criadoPor
) {
}