package jp_2026_Manoel_Souza.service;

import jp_2026_Manoel_Souza.dto.request.AtualizarProdutoRequest;
import jp_2026_Manoel_Souza.dto.request.CriarProdutoRequest;
import jp_2026_Manoel_Souza.dto.response.ProdutoResponse;
import jp_2026_Manoel_Souza.mapper.ProdutoMapper;
import jp_2026_Manoel_Souza.model.Categoria;
import jp_2026_Manoel_Souza.model.HistoricoPreco;
import jp_2026_Manoel_Souza.model.Produtos;
import jp_2026_Manoel_Souza.repository.CategoriaRepository;
import jp_2026_Manoel_Souza.repository.HistoricoPrecoRepository;
import jp_2026_Manoel_Souza.repository.ProdutosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutosServiceTest {

    @InjectMocks
    private ProdutosService produtosService;

    @Mock
    private ProdutosRepository produtosRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private HistoricoPrecoRepository historicoPrecoRepository;
    @Mock
    private ProdutoMapper produtoMapper;

    @Test
    @DisplayName("Deve desativar um produto com sucesso")
    void deletarProduto_Success() {
        // Arrange
        Long produtoId = 1L;
        Produtos produtoExistente = new Produtos();
        produtoExistente.setId(produtoId);
        produtoExistente.setAtivo(true);

        when(produtosRepository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        // Act
        produtosService.deletarProduto(produtoId);

        // Assert
        assertFalse(produtoExistente.getAtivo());
        verify(produtosRepository, times(1)).save(produtoExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar produto não encontrado")
    void deletarProduto_ProdutoNotFound_ThrowsException() {
        // Arrange
        Long produtoId = 1L;
        when(produtosRepository.findById(produtoId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> produtosService.deletarProduto(produtoId));
        assertEquals("Produto não encontrado", exception.getMessage());
        verify(produtosRepository, never()).save(any(Produtos.class));
    }

    @Test
    @DisplayName("Deve atualizar o preço do produto e registrar no histórico")
    void atualizaPreco_Success() {
        // Arrange
        Long produtoId = 1L;
        BigDecimal novoPreco = new BigDecimal("150.00");
        Produtos produtoExistente = new Produtos();
        produtoExistente.setId(produtoId);
        produtoExistente.setPreco(new BigDecimal("100.00"));
        produtoExistente.setAtivo(true);

        when(produtosRepository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));
        // O preço do produto existente é atualizado antes de ser salvo
        produtoExistente.setPreco(novoPreco);
        when(produtosRepository.saveAndFlush(any(Produtos.class))).thenReturn(produtoExistente);
        when(produtoMapper.paraResponse(any(Produtos.class))).thenReturn(new ProdutoResponse(
                produtoId, "Nome Produto", "Desc Produto", "Codigo", novoPreco, null, 1L, 10, 5, true, false));

        // Act
        ProdutoResponse response = produtosService.atualizaPreco(produtoId, novoPreco);

        // Assert
        assertNotNull(response);
        assertEquals(0, novoPreco.compareTo(response.preco()));
        verify(historicoPrecoRepository, times(1)).save(any(HistoricoPreco.class));
        verify(produtosRepository, times(1)).saveAndFlush(any(Produtos.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar preço com valor inválido")
    void atualizaPreco_InvalidPrice_ThrowsException() {
        // Arrange
        Long produtoId = 1L;
        BigDecimal precoInvalido = BigDecimal.ZERO;
        Produtos produtoExistente = new Produtos();
        produtoExistente.setId(produtoId);
        produtoExistente.setAtivo(true);

        when(produtosRepository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> produtosService.atualizaPreco(produtoId, precoInvalido));
        assertEquals("Preço deve ser maior que zero", exception.getMessage());
        verify(historicoPrecoRepository, never()).save(any(HistoricoPreco.class));
        verify(produtosRepository, never()).saveAndFlush(any(Produtos.class));
    }

    @Test
    @DisplayName("Deve buscar produtos por nome e categoria")
    void buscar_ByNameAndCategory_ReturnsFilteredProducts() {
        // Arrange
        String nome = "Produto";
        Long categoriaId = 1L;
        when(produtosRepository.findByNomeContainingIgnoreCaseAndCategoriaIdAndAtivoTrue(anyString(), anyLong()))
                .thenReturn(List.of(new Produtos())); // Retorna uma lista com um produto mock
        when(produtoMapper.paraResponse(any(Produtos.class))).thenReturn(new ProdutoResponse(
                1L, "Produto 1", "Descrição 1", "COD1", new BigDecimal("10.00"), null, 1L, 10, 5, true, false));

        // Act
        List<ProdutoResponse> response = produtosService.buscar(nome, categoriaId);

        // Assert
        assertFalse(response.isEmpty());
        verify(produtosRepository, times(1)).findByNomeContainingIgnoreCaseAndCategoriaIdAndAtivoTrue(nome.trim(), categoriaId);
    }

    @Test
    @DisplayName("Deve buscar produtos apenas por nome")
    void buscar_ByNameOnly_ReturnsFilteredProducts() {
        // Arrange
        String nome = "Produto";
        when(produtosRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(anyString()))
                .thenReturn(List.of(new Produtos()));
        when(produtoMapper.paraResponse(any(Produtos.class))).thenReturn(new ProdutoResponse(
                1L, "Produto 1", "Descrição 1", "COD1", new BigDecimal("10.00"), null, 1L, 10, 5, true, false));

        // Act
        List<ProdutoResponse> response = produtosService.buscar(nome, null);

        // Assert
        assertFalse(response.isEmpty());
        verify(produtosRepository, times(1)).findByNomeContainingIgnoreCaseAndAtivoTrue(nome.trim());
    }

    @Test
    @DisplayName("Deve buscar produtos apenas por categoria")
    void buscar_ByCategoryOnly_ReturnsFilteredProducts() {
        // Arrange
        Long categoriaId = 1L;
        when(produtosRepository.findByCategoriaIdAndAtivoTrue(anyLong()))
                .thenReturn(List.of(new Produtos()));
        when(produtoMapper.paraResponse(any(Produtos.class))).thenReturn(new ProdutoResponse(
                1L, "Produto 1", "Descrição 1", "COD1", new BigDecimal("10.00"), null, 1L, 10, 5, true, false));

        // Act
        List<ProdutoResponse> response = produtosService.buscar(null, categoriaId);

        // Assert
        assertFalse(response.isEmpty());
        verify(produtosRepository, times(1)).findByCategoriaIdAndAtivoTrue(categoriaId);
    }

    @Test
    @DisplayName("Deve buscar todos os produtos ativos quando nenhum filtro é fornecido")
    void buscar_AllActiveProducts_ReturnsAllProducts() {
        // Arrange
        when(produtosRepository.findByAtivoTrue()).thenReturn(List.of(new Produtos(), new Produtos())); // Retorna uma lista com dois produtos mock
        when(produtoMapper.paraResponse(any(Produtos.class))).thenReturn(new ProdutoResponse(
                1L, "Produto 1", "Descrição 1", "COD1", new BigDecimal("10.00"), null, 1L, 10, 5, true, false));

        // Act
        List<ProdutoResponse> response = produtosService.buscar(null, null);
        // Assert
        assertFalse(response.isEmpty());
        assertEquals(2, response.size());
        verify(produtosRepository, times(1)).findByAtivoTrue();
    }
}