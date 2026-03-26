package jp_2026_Manoel_Souza.mapper;

import jp_2026_Manoel_Souza.dto.response.PromocaoResponse;
import jp_2026_Manoel_Souza.model.Promocao;
import org.springframework.stereotype.Component;

@Component
public class PromocaoMapper {

    public PromocaoResponse paraResponse(Promocao promocao) {
        Long categoriaId = null;
        Long produtoId = null;

        if (promocao.getCategoria() != null) {
            categoriaId = promocao.getCategoria().getId();
        }

        if (promocao.getProduto() != null) {
            produtoId = promocao.getProduto().getId();
        }

        return new PromocaoResponse(
                promocao.getId(),
                promocao.getCodigo(),
                promocao.getTipo().name(),
                promocao.getValor(),
                promocao.getDataInicio(),
                promocao.getDataFim(),
                promocao.getLimiteUso(),
                promocao.getQuantidadeUsada(),
                categoriaId,
                produtoId,
                promocao.getAtivo()
        );
    }
}