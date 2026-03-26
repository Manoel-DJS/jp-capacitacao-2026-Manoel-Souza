package jp_2026_Manoel_Souza.dto.response;

import java.time.LocalDateTime;

public record AvaliacaoResponse(
        Long id,
        Long produtoId,
        Long pedidoId,
        Long usuarioId,
        Integer nota,
        String comentario,
        LocalDateTime dataCriacao
) {
}