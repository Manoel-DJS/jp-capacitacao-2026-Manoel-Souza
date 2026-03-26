package jp_2026_Manoel_Souza.dto.response;

import java.math.BigDecimal;

public record ItemCarrinhoResponse(
        Long id,
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoMomento,
        BigDecimal subtotal
) {
}