package jp_2026_Manoel_Souza.service;

import jp_2026_Manoel_Souza.dto.request.AplicarCupomRequest;
import jp_2026_Manoel_Souza.dto.response.CupomAplicadoResponse;
import jp_2026_Manoel_Souza.model.*;
import jp_2026_Manoel_Souza.model.enums.StatusCarrinho;
import jp_2026_Manoel_Souza.model.enums.TipoPromocao;
import jp_2026_Manoel_Souza.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromocaoServiceTest {

    @InjectMocks
    private PromocaoService promocaoService;

    @Mock
    private PromocaoRepository promocaoRepository;
    @Mock
    private UsoCupomRepository usoCupomRepository;
    @Mock
    private CarrinhoRepository carrinhoRepository;
    @Mock
    private ItemCarrinhoRepository itemCarrinhoRepository;

    @Test
    @DisplayName("Deve aplicar cupom de desconto percentual com sucesso")
    void aplicarCupom_ComCupomPercentualValido_DeveAplicarDesconto() {
        // Arrange
        Long usuarioId = 1L;
        String codigoCupom = "PROMO10";
        AplicarCupomRequest request = new AplicarCupomRequest(usuarioId, codigoCupom);

        Promocao promocao = new Promocao();
        promocao.setId(1L);
        promocao.setCodigo(codigoCupom);
        promocao.setTipo(TipoPromocao.PERCENTUAL);
        promocao.setValor(BigDecimal.TEN); // 10%
        promocao.setDataInicio(LocalDateTime.now().minusDays(1));
        promocao.setDataFim(LocalDateTime.now().plusDays(1));
        promocao.setLimiteUso(100);
        promocao.setQuantidadeUsada(10);
        promocao.setAtivo(true);

        Carrinho carrinho = new Carrinho();
        carrinho.setId(1L);

        Produtos produto = new Produtos();
        produto.setId(1L);
        produto.setPreco(new BigDecimal("200.00"));

        ItemCarrinho item = new ItemCarrinho();
        item.setProduto(produto);
        item.setQuantidade(1);
        item.setPrecoMomento(produto.getPreco());

        when(promocaoRepository.findByCodigoIgnoreCaseAndAtivoTrue(codigoCupom.trim())).thenReturn(Optional.of(promocao));
        when(usoCupomRepository.existsByPromocaoIdAndUsuarioId(promocao.getId(), usuarioId)).thenReturn(false);
        when(carrinhoRepository.findByUsuarioIdAndStatus(usuarioId, StatusCarrinho.ATIVO)).thenReturn(Optional.of(carrinho));
        when(itemCarrinhoRepository.findByCarrinhoId(carrinho.getId())).thenReturn(List.of(item));

        // Act
        CupomAplicadoResponse response = promocaoService.aplicarCupom(request);

        // Assert
        assertNotNull(response);
        assertEquals(0, new BigDecimal("200.00").compareTo(response.valorCarrinho()));
        assertEquals(0, new BigDecimal("20.00").compareTo(response.valorDesconto())); // 10% de 200
        assertEquals(0, new BigDecimal("180.00").compareTo(response.valorFinal()));
        verify(carrinhoRepository).save(any(Carrinho.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aplicar cupom expirado")
    void aplicarCupom_ComCupomExpirado_DeveLancarExcecao() {
        // Arrange
        String codigoCupom = "EXPIRADO";
        AplicarCupomRequest request = new AplicarCupomRequest(1L, codigoCupom);

        Promocao promocao = new Promocao();
        promocao.setDataInicio(LocalDateTime.now().minusDays(10));
        promocao.setDataFim(LocalDateTime.now().minusDays(1)); // Expirado ontem

        when(promocaoRepository.findByCodigoIgnoreCaseAndAtivoTrue(codigoCupom.trim())).thenReturn(Optional.of(promocao));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> promocaoService.aplicarCupom(request));
        assertEquals("Cupom expirado ou fora do período de validade", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao aplicar cupom em carrinho sem itens válidos para a promoção")
    void aplicarCupom_SemItensValidos_DeveLancarExcecao() {
        // Arrange
        Long usuarioId = 1L;
        String codigoCupom = "PRODUTO_X";
        AplicarCupomRequest request = new AplicarCupomRequest(usuarioId, codigoCupom);

        Produtos produtoPromocao = new Produtos();
        produtoPromocao.setId(10L);

        Promocao promocao = new Promocao();
        promocao.setId(1L);
        promocao.setCodigo(codigoCupom);
        promocao.setTipo(TipoPromocao.PERCENTUAL);
        promocao.setValor(BigDecimal.TEN);
        promocao.setDataInicio(LocalDateTime.now().minusDays(1));
        promocao.setDataFim(LocalDateTime.now().plusDays(1));
        promocao.setLimiteUso(100);
        promocao.setQuantidadeUsada(10);
        promocao.setProduto(produtoPromocao); // Promoção para produto específico

        Carrinho carrinho = new Carrinho();
        carrinho.setId(1L);

        Produtos produtoNoCarrinho = new Produtos();
        produtoNoCarrinho.setId(20L); // Produto diferente

        ItemCarrinho item = new ItemCarrinho();
        item.setProduto(produtoNoCarrinho);

        when(promocaoRepository.findByCodigoIgnoreCaseAndAtivoTrue(codigoCupom.trim())).thenReturn(Optional.of(promocao));
        when(carrinhoRepository.findByUsuarioIdAndStatus(usuarioId, StatusCarrinho.ATIVO)).thenReturn(Optional.of(carrinho));
        when(itemCarrinhoRepository.findByCarrinhoId(carrinho.getId())).thenReturn(List.of(item));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> promocaoService.aplicarCupom(request));
        assertEquals("Cupom sem relação com os produtos do carrinho", exception.getMessage());
    }

    @Test
    @DisplayName("Deve limitar o desconto ao valor base dos itens quando o desconto fixo for maior")
    void aplicarCupom_ComDescontoFixoMaiorQueValorBase_DeveLimitarDesconto() {
        // Arrange
        AplicarCupomRequest request = new AplicarCupomRequest(1L, "DESCONTAO");

        Promocao promocao = new Promocao();
        promocao.setTipo(TipoPromocao.FIXO);
        promocao.setValor(new BigDecimal("50.00")); // Desconto de R$50
        promocao.setDataInicio(LocalDateTime.now().minusDays(1));
        promocao.setDataFim(LocalDateTime.now().plusDays(1));
        promocao.setLimiteUso(100);

        Carrinho carrinho = new Carrinho();
        carrinho.setId(1L);

        ItemCarrinho item = new ItemCarrinho();
        item.setPrecoMomento(new BigDecimal("30.00")); // Produto custa R$30
        item.setQuantidade(1);

        when(promocaoRepository.findByCodigoIgnoreCaseAndAtivoTrue(anyString())).thenReturn(Optional.of(promocao));
        when(carrinhoRepository.findByUsuarioIdAndStatus(anyLong(), any())).thenReturn(Optional.of(carrinho));
        when(itemCarrinhoRepository.findByCarrinhoId(anyLong())).thenReturn(List.of(item));

        // Act
        CupomAplicadoResponse response = promocaoService.aplicarCupom(request);

        // Assert
        assertEquals(0, new BigDecimal("30.00").compareTo(response.valorDesconto())); // Desconto limitado a R$30
        assertEquals(0, BigDecimal.ZERO.compareTo(response.valorFinal())); // Valor final é R$0
    }
}