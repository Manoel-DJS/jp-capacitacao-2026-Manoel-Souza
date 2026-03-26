package jp_2026_Manoel_Souza.mapper;

import jp_2026_Manoel_Souza.dto.response.ItemPedidoResponse;
import jp_2026_Manoel_Souza.dto.response.PedidoResponse;
import jp_2026_Manoel_Souza.model.ItemPedido;
import jp_2026_Manoel_Souza.model.Pedido;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class PedidoMapper {

    public PedidoResponse paraResponse(Pedido pedido, List<ItemPedido> itensPedido) {
        List<ItemPedidoResponse> itensResponse = new ArrayList<>();

        for (ItemPedido item : itensPedido) {
            BigDecimal subtotal = item.getPrecoMomento()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));

            itensResponse.add(new ItemPedidoResponse(
                    item.getId(),
                    item.getProduto().getId(),
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getPrecoMomento(),
                    subtotal
            ));
        }

        return new PedidoResponse(
                pedido.getId(),
                pedido.getUsuarioId(),
                pedido.getValorTotal(),
                pedido.getDesconto(),
                pedido.getFrete(),
                pedido.getStatus().name(),
                pedido.getEndereco(),
                pedido.getDataCriacao(),
                pedido.getDataAtualizacao(),
                itensResponse
        );
    }
}