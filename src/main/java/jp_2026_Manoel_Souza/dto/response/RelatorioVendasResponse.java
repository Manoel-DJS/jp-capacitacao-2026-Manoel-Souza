package jp_2026_Manoel_Souza.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RelatorioVendasResponse(
        LocalDate dataInicio,
        LocalDate dataFim,
        Long quantidadePedidos,
        BigDecimal valorTotal
) {
}