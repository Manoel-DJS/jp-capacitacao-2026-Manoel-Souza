package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

    List<Produtos> findByNomeContainingIgnoreCase(String nome);

    List<Produtos> findByCategoriaId(Long categoriaId);

    List<Produtos> findByNomeContainingIgnoreCaseAndCategoriaId(String nome, Long categoriaId);
}