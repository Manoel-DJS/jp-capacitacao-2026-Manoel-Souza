package jp_2026_Manoel_Souza.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        Long usuarioId,
        BigDecimal valorTotal,
        BigDecimal desconto,
        BigDecimal frete,
        String status,
        String endereco,
        String codigoCupom,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        List<ItemPedidoResponse> itens
) {
}