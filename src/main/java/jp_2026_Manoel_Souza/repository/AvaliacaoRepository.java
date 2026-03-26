package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByProdutoIdOrderByDataCriacaoDesc(Long produtoId);

    boolean existsByPedidoIdAndProdutoId(Long pedidoId, Long produtoId);

    Integer countByProdutoId(Long produtoId);

    @Query("select avg(a.nota) from Avaliacao a where a.produto.id = :produtoId")
    BigDecimal calcularMediaPorProduto(Long produtoId);
}