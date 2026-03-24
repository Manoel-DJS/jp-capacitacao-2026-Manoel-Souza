package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> {
}