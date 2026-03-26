package jp_2026_Manoel_Souza.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CriarPromocaoRequest(

        @NotBlank(message = "O código da promoção é obrigatório")
        String codigo,

        @NotBlank(message = "O tipo da promoção é obrigatório")
        String tipo,

        @NotNull(message = "O valor da promoção é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor da promoção deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "A data de início é obrigatória")
        LocalDateTime dataInicio,

        @NotNull(message = "A data de fim é obrigatória")
        LocalDateTime dataFim,

        @NotNull(message = "O limite de uso é obrigatório")
        Integer limiteUso,

        Long categoriaId,

        Long produtoId
) {
}