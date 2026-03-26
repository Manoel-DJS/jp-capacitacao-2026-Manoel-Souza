package jp_2026_Manoel_Souza.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AjustarEstoqueRequest(

        @NotNull(message = "A nova quantidade é obrigatória")
        @Min(value = 0, message = "A nova quantidade não pode ser negativa")
        Integer novaQuantidade,

        @NotBlank(message = "O motivo é obrigatório")
        String motivo,

        Long referenciaId,

        String criadoPor
) {
}