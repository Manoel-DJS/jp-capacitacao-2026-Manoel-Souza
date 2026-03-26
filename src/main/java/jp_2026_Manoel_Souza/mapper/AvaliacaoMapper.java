package jp_2026_Manoel_Souza.mapper;

import jp_2026_Manoel_Souza.dto.response.AvaliacaoResponse;
import jp_2026_Manoel_Souza.model.Avaliacao;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoMapper {

    public AvaliacaoResponse paraResponse(Avaliacao avaliacao) {
        return new AvaliacaoResponse(
                avaliacao.getId(),
                avaliacao.getProduto().getId(),
                avaliacao.getPedido().getId(),
                avaliacao.getUsuarioId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getDataCriacao()
        );
    }
}