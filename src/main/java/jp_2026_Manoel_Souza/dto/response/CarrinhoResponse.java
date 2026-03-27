package jp_2026_Manoel_Souza.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CarrinhoResponse(
        Long id,
        Long usuarioId,
        String status,
        String codigoCupom,
        BigDecimal descontoAplicado,
        List<ItemCarrinhoResponse> itens,
        BigDecimal valorTotal
) {
}