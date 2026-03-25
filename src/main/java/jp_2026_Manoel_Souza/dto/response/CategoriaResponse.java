package jp_2026_Manoel_Souza.dto.response;

import java.time.LocalDateTime;

public record CategoriaResponse(
        Long id,
        String nome,
        Long categoriaPrincipalId,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
}