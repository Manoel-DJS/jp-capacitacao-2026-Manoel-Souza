package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    List<Auditoria> findByEntidadeOrderByDataHoraDesc(String entidade);
}