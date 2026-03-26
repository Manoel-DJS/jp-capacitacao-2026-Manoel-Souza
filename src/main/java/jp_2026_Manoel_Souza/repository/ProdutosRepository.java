package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

    List<Produtos> findByAtivoTrue();

    Optional<Produtos> findByIdAndAtivoTrue(Long id);

    List<Produtos> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    List<Produtos> findByCategoriaIdAndAtivoTrue(Long categoriaId);

    List<Produtos> findByNomeContainingIgnoreCaseAndCategoriaIdAndAtivoTrue(String nome, Long categoriaId);

    boolean existsByCodigoBarrasIgnoreCase(String codigoBarras);

    boolean existsByCodigoBarrasIgnoreCaseAndIdNot(String codigoBarras, Long id);
}