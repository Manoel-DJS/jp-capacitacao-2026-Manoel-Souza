package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.HistoricoPreco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface HistoricoPrecoRepository extends JpaRepository<HistoricoPreco, UUID> {
    /**
     * Ele cria uma query fazendo busca por pdroduto id
     * Select * from historico_preco where produtos_id = :produtoId;
     * @param produtoId
     * @return HistoricoPreco
     */
    Set<HistoricoPreco> findByProdutosId(Long produtoId);

}
