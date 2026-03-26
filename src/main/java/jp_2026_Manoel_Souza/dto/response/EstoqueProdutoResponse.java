package jp_2026_Manoel_Souza.dto.response;

import java.util.List;

public record EstoqueProdutoResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidadeEstoque,
        Integer estoqueMinimo,
        Boolean estoqueBaixo,
        List<MovimentacaoEstoqueResponse> movimentacoes
) {
}