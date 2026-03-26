package jp_2026_Manoel_Souza.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromocaoResponse(
        Long id,
        String codigo,
        String tipo,
        BigDecimal valor,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Integer limiteUso,
        Integer quantidadeUsada,
        Long categoriaId,
        Long produtoId,
        Boolean ativo
) {
}