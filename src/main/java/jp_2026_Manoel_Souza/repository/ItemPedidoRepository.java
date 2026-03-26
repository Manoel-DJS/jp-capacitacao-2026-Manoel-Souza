package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedidoId(Long pedidoId);

    boolean existsByPedidoIdAndProdutoId(Long pedidoId, Long produtoId);
}