package jp_2026_Manoel_Souza.dto.response;

import java.math.BigDecimal;

public record CupomAplicadoResponse(
        String codigo,
        String tipo,
        BigDecimal valorCarrinho,
        BigDecimal valorDesconto,
        BigDecimal valorFinal
) {
}