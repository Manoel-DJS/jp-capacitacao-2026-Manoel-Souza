package jp_2026_Manoel_Souza.repository;

import jp_2026_Manoel_Souza.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNomeIgnoreCaseAndCategoriaPrincipalIsNull(String nome);

    boolean existsByNomeIgnoreCaseAndCategoriaPrincipalId(String nome, Long categoriaPrincipalId);

    boolean existsByNomeIgnoreCaseAndCategoriaPrincipalIsNullAndIdNot(String nome, Long id);

    boolean existsByNomeIgnoreCaseAndCategoriaPrincipalIdAndIdNot(String nome, Long categoriaPrincipalId, Long id);

    boolean existsByCategoriaPrincipalId(Long categoriaPrincipalId);
}