package jp_2026_Manoel_Souza.mapper;

import jp_2026_Manoel_Souza.dto.response.CarrinhoResponse;
import jp_2026_Manoel_Souza.dto.response.ItemCarrinhoResponse;
import jp_2026_Manoel_Souza.model.Carrinho;
import jp_2026_Manoel_Souza.model.ItemCarrinho;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CarrinhoMapper {

    public CarrinhoResponse paraResponse(Carrinho carrinho, List<ItemCarrinho> itensCarrinho) {
        List<ItemCarrinhoResponse> itensResponse = new ArrayList<>();
        BigDecimal totalItens = BigDecimal.ZERO;

        for (ItemCarrinho item : itensCarrinho) {
            BigDecimal subtotal = item.getPrecoMomento()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));

            itensResponse.add(new ItemCarrinhoResponse(
                    item.getId(),
                    item.getProduto().getId(),
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getPrecoMomento(),
                    subtotal
            ));

            totalItens = totalItens.add(subtotal);
        }

        BigDecimal descontoAplicado = carrinho.getDescontoAplicado() == null
                ? BigDecimal.ZERO
                : carrinho.getDescontoAplicado();

        BigDecimal valorFinal = totalItens.subtract(descontoAplicado);

        if (valorFinal.compareTo(BigDecimal.ZERO) < 0) {
            valorFinal = BigDecimal.ZERO;
        }

        String codigoCupom = null;

        if (carrinho.getPromocao() != null) {
            codigoCupom = carrinho.getPromocao().getCodigo();
        }

        return new CarrinhoResponse(
                carrinho.getId(),
                carrinho.getUsuarioId(),
                carrinho.getStatus().name(),
                codigoCupom,
                descontoAplicado,
                itensResponse,
                valorFinal
        );
    }
}