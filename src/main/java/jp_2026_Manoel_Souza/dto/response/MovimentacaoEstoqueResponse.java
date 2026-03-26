package jp_2026_Manoel_Souza.dto.response;

import java.time.LocalDateTime;

public record MovimentacaoEstoqueResponse(
        Long id,
        Long produtoId,
        Integer quantidade,
        String tipo,
        String motivo,
        Long referenciaId,
        String criadoPor,
        LocalDateTime dataCriacao
) {
}