package jp_2026_Manoel_Souza.dto.response;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        String codigoBarras,
        BigDecimal preco,
        BigDecimal precoCusto,
        Long categoriaId,
        Integer quantidadeEstoque,
        Integer estoqueMinimo,
        Boolean estoqueBaixo,
        Boolean ativo
) {
}