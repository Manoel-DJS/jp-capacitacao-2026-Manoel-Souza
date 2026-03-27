package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedidoId(Long pedidoId);

    boolean existsByPedidoIdAndProdutoId(Long pedidoId, Long produtoId);

    @Query("""
       select ip.produto.id, ip.produto.nome, sum(ip.quantidade)
       from ItemPedido ip
       where ip.pedido.status in ('PAID', 'SHIPPED', 'DELIVERED')
       group by ip.produto.id, ip.produto.nome
       order by sum(ip.quantidade) desc
       """)
    List<Object[]> buscarProdutosMaisVendidos();
}