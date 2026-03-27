package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("""
       select count(p)
       from Pedido p
       where p.status in ('PAID', 'SHIPPED', 'DELIVERED')
       and p.dataCriacao >= :inicio
       and p.dataCriacao <= :fim
       """)
    Long contarPedidosFaturadosPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    @Query("""
       select coalesce(sum(p.valorTotal), 0)
       from Pedido p
       where p.status in ('PAID', 'SHIPPED', 'DELIVERED')
       and p.dataCriacao >= :inicio
       and p.dataCriacao <= :fim
       """)
    BigDecimal somarFaturamentoPorPeriodo(LocalDateTime inicio, LocalDateTime fim);


}