package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {

    Optional<Promocao> findByCodigoIgnoreCaseAndAtivoTrue(String codigo);

    boolean existsByCodigoIgnoreCase(String codigo);
}