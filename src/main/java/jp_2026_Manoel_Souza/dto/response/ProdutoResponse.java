package jp_2026_Manoel_Souza.dto.response;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        String codigoBarras,
        Long categoriaId
) {
}