package jp_2026_Manoel_Souza.service;

import jp_2026_Manoel_Souza.dto.request.AplicarCupomRequest;
import jp_2026_Manoel_Souza.dto.request.CriarPromocaoRequest;
import jp_2026_Manoel_Souza.dto.response.CupomAplicadoResponse;
import jp_2026_Manoel_Souza.dto.response.PromocaoResponse;
import jp_2026_Manoel_Souza.mapper.PromocaoMapper;
import jp_2026_Manoel_Souza.model.Carrinho;
import jp_2026_Manoel_Souza.model.Categoria;
import jp_2026_Manoel_Souza.model.ItemCarrinho;
import jp_2026_Manoel_Souza.model.Promocao;
import jp_2026_Manoel_Souza.model.Produtos;
import jp_2026_Manoel_Souza.model.enums.StatusCarrinho;
import jp_2026_Manoel_Souza.model.enums.TipoPromocao;
import jp_2026_Manoel_Souza.repository.CarrinhoRepository;
import jp_2026_Manoel_Souza.repository.CategoriaRepository;
import jp_2026_Manoel_Souza.repository.ItemCarrinhoRepository;
import jp_2026_Manoel_Souza.repository.PromocaoRepository;
import jp_2026_Manoel_Souza.repository.ProdutosRepository;
import jp_2026_Manoel_Souza.repository.UsoCupomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocaoService {

    private final PromocaoRepository promocaoRepository;
    private final UsoCupomRepository usoCupomRepository;
    private final ProdutosRepository produtosRepository;
    private final CategoriaRepository categoriaRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final PromocaoMapper promocaoMapper;

    @Transactional
    public PromocaoResponse criarPromocao(CriarPromocaoRequest request) {
        String codigo = request.codigo().trim();

        if (promocaoRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new RuntimeException("Já existe promoção com esse código");
        }

        if (request.dataFim().isBefore(request.dataInicio())) {
            throw new RuntimeException("A data fim deve ser maior que a data início");
        }

        if (request.produtoId() != null && request.categoriaId() != null) {
            throw new RuntimeException("Informe somente produto ou categoria");
        }

        Produtos produto = null;
        Categoria categoria = null;

        if (request.produtoId() != null) {
            produto = produtosRepository.findByIdAndAtivoTrue(request.produtoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        }

        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findById(request.categoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        }

        Promocao promocao = new Promocao();
        promocao.setCodigo(codigo);
        promocao.setTipo(converterTipo(request.tipo()));
        promocao.setValor(request.valor());
        promocao.setDataInicio(request.dataInicio());
        promocao.setDataFim(request.dataFim());
        promocao.setLimiteUso(request.limiteUso());
        promocao.setQuantidadeUsada(0);
        promocao.setProduto(produto);
        promocao.setCategoria(categoria);
        promocao.setAtivo(true);

        return promocaoMapper.paraResponse(promocaoRepository.save(promocao));
    }

    @Transactional
    public CupomAplicadoResponse aplicarCupom(AplicarCupomRequest request) {
        Promocao promocao = buscarPromocaoAtiva(request.codigo());
        validarPromocao(promocao, request.usuarioId());

        Carrinho carrinho = buscarCarrinhoAtivo(request.usuarioId());
        List<ItemCarrinho> itensCarrinho = itemCarrinhoRepository.findByCarrinhoId(carrinho.getId());

        if (itensCarrinho.isEmpty()) {
            throw new RuntimeException("Carrinho vazio");
        }

        List<ItemCarrinho> itensValidos = buscarItensValidos(promocao, itensCarrinho);

        if (itensValidos.isEmpty()) {
            throw new RuntimeException("Cupom sem relação com os produtos do carrinho");
        }

        BigDecimal valorCarrinho = somarItens(itensCarrinho);
        BigDecimal valorBase = somarItens(itensValidos);
        BigDecimal valorDesconto = calcularDesconto(promocao, valorBase);
        BigDecimal valorFinal = valorCarrinho.subtract(valorDesconto);

        if (valorFinal.compareTo(BigDecimal.ZERO) < 0) {
            valorFinal = BigDecimal.ZERO;
        }

        carrinho.setPromocao(promocao);
        carrinho.setDescontoAplicado(valorDesconto);
        carrinhoRepository.save(carrinho);

        return new CupomAplicadoResponse(
                promocao.getCodigo(),
                promocao.getTipo().name(),
                valorCarrinho,
                valorDesconto,
                valorFinal
        );
    }

    private Promocao buscarPromocaoAtiva(String codigo) {
        return promocaoRepository.findByCodigoIgnoreCaseAndAtivoTrue(codigo.trim())
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));
    }

    private void validarPromocao(Promocao promocao, Long usuarioId) {
        LocalDateTime agora = LocalDateTime.now();

        if (agora.isBefore(promocao.getDataInicio()) || agora.isAfter(promocao.getDataFim())) {
            throw new RuntimeException("Cupom expirado ou fora do período de validade");
        }

        if (promocao.getQuantidadeUsada() >= promocao.getLimiteUso()) {
            throw new RuntimeException("Cupom atingiu o limite de uso");
        }

        if (usoCupomRepository.existsByPromocaoIdAndUsuarioId(promocao.getId(), usuarioId)) {
            throw new RuntimeException("Cupom já utilizado pelo usuário");
        }
    }

    private Carrinho buscarCarrinhoAtivo(Long usuarioId) {
        return carrinhoRepository.findByUsuarioIdAndStatus(usuarioId, StatusCarrinho.ATIVO)
                .orElseThrow(() -> new RuntimeException("Carrinho ativo não encontrado"));
    }

    private List<ItemCarrinho> buscarItensValidos(Promocao promocao, List<ItemCarrinho> itensCarrinho) {
        List<ItemCarrinho> itensValidos = new ArrayList<>();

        if (promocao.getProduto() == null && promocao.getCategoria() == null) {
            itensValidos.addAll(itensCarrinho);
            return itensValidos;
        }

        for (ItemCarrinho item : itensCarrinho) {
            if (promocao.getProduto() != null) {
                if (item.getProduto().getId().equals(promocao.getProduto().getId())) {
                    itensValidos.add(item);
                }
                continue;
            }

            if (item.getProduto().getCategoria() != null
                    && item.getProduto().getCategoria().getId().equals(promocao.getCategoria().getId())) {
                itensValidos.add(item);
            }
        }

        return itensValidos;
    }

    private BigDecimal somarItens(List<ItemCarrinho> itens) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemCarrinho item : itens) {
            BigDecimal subtotal = item.getPrecoMomento()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(subtotal);
        }

        return total;
    }

    private BigDecimal calcularDesconto(Promocao promocao, BigDecimal valorBase) {
        BigDecimal desconto;

        if (promocao.getTipo() == TipoPromocao.PERCENTUAL) {
            desconto = valorBase.multiply(promocao.getValor())
                    .divide(BigDecimal.valueOf(100));
        } else {
            desconto = promocao.getValor();
        }

        if (desconto.compareTo(valorBase) > 0) {
            return valorBase;
        }

        return desconto;
    }

    private TipoPromocao converterTipo(String tipo) {
        try {
            return TipoPromocao.valueOf(tipo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de promoção inválido");
        }
    }
}