package jp_2026_Manoel_Souza.dto.response;

public record PromocaoMaisUtilizadaResponse(
        Long promocaoId,
        String codigo,
        Integer quantidadeUsada
) {
}