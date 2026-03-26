package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.Carrinho;
import jp_2026_Manoel_Souza.model.enums.StatusCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    Optional<Carrinho> findByUsuarioIdAndStatus(Long usuarioId, StatusCarrinho status);
}