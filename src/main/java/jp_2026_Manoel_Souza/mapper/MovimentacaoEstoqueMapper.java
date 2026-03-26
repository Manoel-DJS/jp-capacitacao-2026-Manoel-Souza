package jp_2026_Manoel_Souza.mapper;

import jp_2026_Manoel_Souza.dto.response.MovimentacaoEstoqueResponse;
import jp_2026_Manoel_Souza.model.MovimentacaoEstoque;
import org.springframework.stereotype.Component;

@Component
public class MovimentacaoEstoqueMapper {

    public MovimentacaoEstoqueResponse paraResponse(MovimentacaoEstoque movimentacao) {
        return new MovimentacaoEstoqueResponse(
                movimentacao.getId(),
                movimentacao.getProduto().getId(),
                movimentacao.getQuantidade(),
                movimentacao.getTipo().name(),
                movimentacao.getMotivo(),
                movimentacao.getReferenciaId(),
                movimentacao.getCriadoPor(),
                movimentacao.getDataCriacao()
        );
    }
}