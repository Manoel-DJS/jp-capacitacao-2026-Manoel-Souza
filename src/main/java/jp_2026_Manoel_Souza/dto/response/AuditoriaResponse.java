package jp_2026_Manoel_Souza.dto.response;

import java.time.LocalDateTime;

public record AuditoriaResponse(
        Long id,
        String entidade,
        Long entidadeId,
        String acao,
        String antesJson,
        String depoisJson,
        String usuario,
        LocalDateTime dataHora
) {
}