package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByProdutoIdOrderByDataCriacaoDesc(Long produtoId);
}