package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

    List<Produtos> findByAtivoTrue();

    List<Produtos> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    List<Produtos> findByCategoriaIdAndAtivoTrue(Long categoriaId);

    List<Produtos> findByNomeContainingIgnoreCaseAndCategoriaIdAndAtivoTrue(String nome, Long categoriaId);
}