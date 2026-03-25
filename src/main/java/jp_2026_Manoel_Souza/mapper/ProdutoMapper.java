package jp_2026_Manoel_Souza.mapper;

import jp_2026_Manoel_Souza.dto.response.ProdutoResponse;
import jp_2026_Manoel_Souza.model.Produtos;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public ProdutoResponse paraResponse(Produtos produto) {
        Long categoriaId = null;

        if (produto.getCategoria() != null) {
            categoriaId = produto.getCategoria().getId();
        }

        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCodigoBarras(),
                categoriaId
        );
    }
}