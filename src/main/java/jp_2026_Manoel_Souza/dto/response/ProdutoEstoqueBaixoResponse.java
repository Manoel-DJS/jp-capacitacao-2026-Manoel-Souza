package jp_2026_Manoel_Souza.dto.response;

public record ProdutoEstoqueBaixoResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidadeEstoque,
        Integer estoqueMinimo
) {
}