package jp_2026_Manoel_Souza.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarAvaliacaoRequest(

        @NotNull(message = "O usuário é obrigatório")
        Long usuarioId,

        @NotNull(message = "O pedido é obrigatório")
        Long pedidoId,

        @NotNull(message = "O produto é obrigatório")
        Long produtoId,

        @NotNull(message = "A nota é obrigatória")
        @Min(value = 1, message = "A nota deve ser no mínimo 1")
        @Max(value = 5, message = "A nota deve ser no máximo 5")
        Integer nota,

        @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
        String comentario
) {
}