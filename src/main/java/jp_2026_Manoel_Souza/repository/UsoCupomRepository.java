package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.UsoCupom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsoCupomRepository extends JpaRepository<UsoCupom, Long> {

    boolean existsByPromocaoIdAndUsuarioId(Long promocaoId, Long usuarioId);
}