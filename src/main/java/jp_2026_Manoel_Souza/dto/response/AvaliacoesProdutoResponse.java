package jp_2026_Manoel_Souza.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record AvaliacoesProdutoResponse(
        Long produtoId,
        BigDecimal mediaAvaliacoes,
        Integer quantidadeAvaliacoes,
        List<AvaliacaoResponse> avaliacoes
) {
}