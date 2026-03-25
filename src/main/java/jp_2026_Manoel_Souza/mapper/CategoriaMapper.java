package jp_2026_Manoel_Souza.mapper;

import jp_2026_Manoel_Souza.dto.response.CategoriaResponse;
import jp_2026_Manoel_Souza.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public CategoriaResponse paraResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getCategoriaPrincipal() != null ? categoria.getCategoriaPrincipal().getId() : null,
                categoria.getDataCriacao(),
                categoria.getDataAtualizacao()
        );
    }
}