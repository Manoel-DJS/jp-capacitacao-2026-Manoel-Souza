package jp_2026_Manoel_Souza.service;

import jp_2026_Manoel_Souza.dto.request.AtualizarCategoriaRequest;
import jp_2026_Manoel_Souza.dto.request.CriarCategoriaRequest;
import jp_2026_Manoel_Souza.exception.DuplicidadeException;
import jp_2026_Manoel_Souza.exception.RegraDeNegocioException;
import jp_2026_Manoel_Souza.mapper.CategoriaMapper;
import jp_2026_Manoel_Souza.model.Categoria;
import jp_2026_Manoel_Souza.repository.CategoriaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService categoriaService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar categoria com nome duplicado no nível raiz")
    void criar_ComNomeDuplicadoEmNivelRaiz_DeveLancarDuplicidadeException() {
        // Arrange
        CriarCategoriaRequest request = new CriarCategoriaRequest("Eletrônicos", null);
        when(categoriaRepository.existsByNomeIgnoreCaseAndCategoriaPrincipalIsNull(request.nome().trim())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicidadeException.class, () -> {
            categoriaService.criar(request);
        }, "Já existe uma categoria com esse nome neste nível");

        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar categoria criando uma hierarquia cíclica")
    void atualizar_CriandoHierarquiaCiclica_DeveLancarRegraDeNegocioException() {
        // Arrange
        Long categoriaAId = 1L;
        Long categoriaCId = 3L;

        Categoria categoriaA = new Categoria();
        categoriaA.setId(categoriaAId);
        categoriaA.setNome("A");

        Categoria categoriaB = new Categoria();
        categoriaB.setId(2L);
        categoriaB.setNome("B");
        categoriaB.setCategoriaPrincipal(categoriaA);

        Categoria categoriaC = new Categoria();
        categoriaC.setId(categoriaCId);
        categoriaC.setNome("C");
        categoriaC.setCategoriaPrincipal(categoriaB);

        AtualizarCategoriaRequest request = new AtualizarCategoriaRequest("A", categoriaCId);

        when(categoriaRepository.findById(categoriaAId)).thenReturn(Optional.of(categoriaA));
        when(categoriaRepository.findById(categoriaCId)).thenReturn(Optional.of(categoriaC));

        // Act & Assert
        assertThrows(RegraDeNegocioException.class, () -> categoriaService.atualizar(categoriaAId, request), "Não é permitido criar ciclo na hierarquia de categorias");

        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir categoria que possui subcategorias")
    void excluir_CategoriaComSubcategorias_DeveLancarRegraDeNegocioException() {
        // Arrange
        Long categoriaId = 1L;
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(new Categoria()));
        when(categoriaRepository.existsByCategoriaPrincipalId(categoriaId)).thenReturn(true);

        // Act & Assert
        assertThrows(RegraDeNegocioException.class, () -> categoriaService.excluir(categoriaId), "Não é permitido excluir categoria que possui subcategorias");

        verify(categoriaRepository, never()).delete(any(Categoria.class));
    }
}