package jp_2026_Manoel_Souza.dto.response;

public record ProdutoMaisVendidoResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidadeVendida
) {
}