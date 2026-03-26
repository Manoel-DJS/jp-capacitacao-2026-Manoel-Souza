package jp_2026_Manoel_Souza.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AplicarCupomRequest(

        @NotNull(message = "O usuário é obrigatório")
        Long usuarioId,

        @NotBlank(message = "O código do cupom é obrigatório")
        String codigo
) {
}