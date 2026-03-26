package jp_2026_Manoel_Souza.service;

import jp_2026_Manoel_Souza.dto.request.CriarPedidoRequest;
import jp_2026_Manoel_Souza.dto.response.PedidoResponse;
import jp_2026_Manoel_Souza.mapper.PedidoMapper;
import jp_2026_Manoel_Souza.model.Carrinho;
import jp_2026_Manoel_Souza.model.ItemCarrinho;
import jp_2026_Manoel_Souza.model.ItemPedido;
import jp_2026_Manoel_Souza.model.MovimentacaoEstoque;
import jp_2026_Manoel_Souza.model.Pedido;
import jp_2026_Manoel_Souza.model.Produtos;
import jp_2026_Manoel_Souza.model.enums.StatusCarrinho;
import jp_2026_Manoel_Souza.model.enums.StatusPedido;
import jp_2026_Manoel_Souza.model.enums.TipoMovimentacaoEstoque;
import jp_2026_Manoel_Souza.repository.CarrinhoRepository;
import jp_2026_Manoel_Souza.repository.ItemCarrinhoRepository;
import jp_2026_Manoel_Souza.repository.ItemPedidoRepository;
import jp_2026_Manoel_Souza.repository.MovimentacaoEstoqueRepository;
import jp_2026_Manoel_Souza.repository.PedidoRepository;
import jp_2026_Manoel_Souza.repository.ProdutosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final ProdutosRepository produtosRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final PedidoMapper pedidoMapper;

    @Transactional
    public PedidoResponse criarPedido(CriarPedidoRequest request) {
        Carrinho carrinho = buscarCarrinhoAtivo(request.usuarioId());
        List<ItemCarrinho> itensCarrinho = itemCarrinhoRepository.findByCarrinhoId(carrinho.getId());

        if (itensCarrinho.isEmpty()) {
            throw new RuntimeException("Carrinho vazio");
        }

        validarEstoqueItens(itensCarrinho);

        BigDecimal desconto = request.desconto() == null ? BigDecimal.ZERO : request.desconto();
        BigDecimal frete = request.frete() == null ? BigDecimal.ZERO : request.frete();
        BigDecimal valorItens = calcularValorItens(itensCarrinho);
        BigDecimal valorTotal = valorItens.subtract(desconto).add(frete);

        if (valorTotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("O valor total do pedido não pode ser negativo");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(request.usuarioId());
        pedido.setEndereco(request.endereco().trim());
        pedido.setDesconto(desconto);
        pedido.setFrete(frete);
        pedido.setValorTotal(valorTotal);
        pedido.setStatus(StatusPedido.CREATED);

        pedido = pedidoRepository.save(pedido);

        List<ItemPedido> itensPedido = new ArrayList<>();

        for (ItemCarrinho itemCarrinho : itensCarrinho) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(itemCarrinho.getProduto());
            itemPedido.setQuantidade(itemCarrinho.getQuantidade());
            itemPedido.setPrecoMomento(itemCarrinho.getPrecoMomento());

            itemPedido = itemPedidoRepository.save(itemPedido);
            itensPedido.add(itemPedido);

            baixarEstoque(itemCarrinho.getProduto(), itemCarrinho.getQuantidade(), pedido.getId());
        }

        carrinho.setStatus(StatusCarrinho.FINALIZADO);
        carrinhoRepository.save(carrinho);

        return pedidoMapper.paraResponse(pedido, itensPedido);
    }

    public PedidoResponse buscarPorId(Long id) {
        Pedido pedido = buscarPedido(id);
        List<ItemPedido> itensPedido = itemPedidoRepository.findByPedidoId(pedido.getId());

        return pedidoMapper.paraResponse(pedido, itensPedido);
    }

    @Transactional
    public PedidoResponse cancelarPedido(Long id) {
        Pedido pedido = buscarPedido(id);

        if (pedido.getStatus() != StatusPedido.CREATED && pedido.getStatus() != StatusPedido.PAID) {
            throw new RuntimeException("Cancelamento permitido somente para pedidos CREATED ou PAID");
        }

        List<ItemPedido> itensPedido = itemPedidoRepository.findByPedidoId(pedido.getId());

        for (ItemPedido itemPedido : itensPedido) {
            devolverEstoque(itemPedido.getProduto(), itemPedido.getQuantidade(), pedido.getId());
        }

        pedido.setStatus(StatusPedido.CANCELLED);
        pedido = pedidoRepository.save(pedido);

        return pedidoMapper.paraResponse(pedido, itensPedido);
    }

    @Transactional
    public PedidoResponse marcarComoPago(Long id) {
        Pedido pedido = buscarPedido(id);

        if (pedido.getStatus() != StatusPedido.CREATED) {
            throw new RuntimeException("Somente pedidos CREATED podem ser marcados como PAID");
        }

        pedido.setStatus(StatusPedido.PAID);
        pedido = pedidoRepository.save(pedido);

        List<ItemPedido> itensPedido = itemPedidoRepository.findByPedidoId(pedido.getId());

        return pedidoMapper.paraResponse(pedido, itensPedido);
    }

    @Transactional
    public PedidoResponse marcarComoEnviado(Long id) {
        Pedido pedido = buscarPedido(id);

        if (pedido.getStatus() != StatusPedido.PAID) {
            throw new RuntimeException("Somente pedidos PAID podem ser marcados como SHIPPED");
        }

        pedido.setStatus(StatusPedido.SHIPPED);
        pedido = pedidoRepository.save(pedido);

        List<ItemPedido> itensPedido = itemPedidoRepository.findByPedidoId(pedido.getId());

        return pedidoMapper.paraResponse(pedido, itensPedido);
    }

    @Transactional
    public PedidoResponse marcarComoEntregue(Long id) {
        Pedido pedido = buscarPedido(id);

        if (pedido.getStatus() != StatusPedido.SHIPPED) {
            throw new RuntimeException("Somente pedidos SHIPPED podem ser marcados como DELIVERED");
        }

        pedido.setStatus(StatusPedido.DELIVERED);
        pedido = pedidoRepository.save(pedido);

        List<ItemPedido> itensPedido = itemPedidoRepository.findByPedidoId(pedido.getId());

        return pedidoMapper.paraResponse(pedido, itensPedido);
    }

    private Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    private Carrinho buscarCarrinhoAtivo(Long usuarioId) {
        return carrinhoRepository.findByUsuarioIdAndStatus(usuarioId, StatusCarrinho.ATIVO)
                .orElseThrow(() -> new RuntimeException("Carrinho ativo não encontrado"));
    }

    private void validarEstoqueItens(List<ItemCarrinho> itensCarrinho) {
        for (ItemCarrinho itemCarrinho : itensCarrinho) {
            Produtos produto = itemCarrinho.getProduto();

            if (!Boolean.TRUE.equals(produto.getAtivo())) {
                throw new RuntimeException("Produto inativo no carrinho");
            }

            if (produto.getQuantidadeEstoque() < itemCarrinho.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }
        }
    }

    private BigDecimal calcularValorItens(List<ItemCarrinho> itensCarrinho) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemCarrinho itemCarrinho : itensCarrinho) {
            BigDecimal subtotal = itemCarrinho.getPrecoMomento()
                    .multiply(BigDecimal.valueOf(itemCarrinho.getQuantidade()));

            total = total.add(subtotal);
        }

        return total;
    }

    private void baixarEstoque(Produtos produto, Integer quantidade, Long pedidoId) {
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        atualizarFlagEstoqueBaixo(produto);
        produtosRepository.save(produto);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setTipo(TipoMovimentacaoEstoque.SAIDA);
        movimentacao.setMotivo("Baixa de estoque por pedido");
        movimentacao.setReferenciaId(pedidoId);
        movimentacao.setCriadoPor("sistema");

        movimentacaoEstoqueRepository.save(movimentacao);
    }

    private void devolverEstoque(Produtos produto, Integer quantidade, Long pedidoId) {
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        atualizarFlagEstoqueBaixo(produto);
        produtosRepository.save(produto);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setTipo(TipoMovimentacaoEstoque.DEVOLUCAO);
        movimentacao.setMotivo("Devolução de estoque por cancelamento de pedido");
        movimentacao.setReferenciaId(pedidoId);
        movimentacao.setCriadoPor("sistema");

        movimentacaoEstoqueRepository.save(movimentacao);
    }

    private void atualizarFlagEstoqueBaixo(Produtos produto) {
        produto.setEstoqueBaixo(produto.getQuantidadeEstoque() <= produto.getEstoqueMinimo());
    }
}