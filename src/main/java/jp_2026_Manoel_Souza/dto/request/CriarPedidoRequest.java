package jp_2026_Manoel_Souza.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarPedidoRequest(

        @NotNull(message = "O usuário é obrigatório")
        Long usuarioId,

        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        BigDecimal desconto,

        BigDecimal frete
) {
}