package jp_2026_Manoel_Souza.service;

import jp_2026_Manoel_Souza.dto.request.CriarPedidoRequest;
import jp_2026_Manoel_Souza.dto.response.PedidoResponse;
import jp_2026_Manoel_Souza.exception.EstoqueInsuficienteException;
import jp_2026_Manoel_Souza.exception.RecursoNaoEncontradoException;
import jp_2026_Manoel_Souza.exception.RegraDeNegocioException;
import jp_2026_Manoel_Souza.mapper.PedidoMapper;
import jp_2026_Manoel_Souza.model.Carrinho;
import jp_2026_Manoel_Souza.model.ItemCarrinho;
import jp_2026_Manoel_Souza.model.ItemPedido;
import jp_2026_Manoel_Souza.model.MovimentacaoEstoque;
import jp_2026_Manoel_Souza.model.Pedido;
import jp_2026_Manoel_Souza.model.Promocao;
import jp_2026_Manoel_Souza.model.Produtos;
import jp_2026_Manoel_Souza.model.UsoCupom;
import jp_2026_Manoel_Souza.model.enums.StatusCarrinho;
import jp_2026_Manoel_Souza.model.enums.StatusPedido;
import jp_2026_Manoel_Souza.model.enums.TipoMovimentacaoEstoque;
import jp_2026_Manoel_Souza.repository.CarrinhoRepository;
import jp_2026_Manoel_Souza.repository.ItemCarrinhoRepository;
import jp_2026_Manoel_Souza.repository.ItemPedidoRepository;
import jp_2026_Manoel_Souza.repository.MovimentacaoEstoqueRepository;
import jp_2026_Manoel_Souza.repository.PedidoRepository;
import jp_2026_Manoel_Souza.repository.PromocaoRepository;
import jp_2026_Manoel_Souza.repository.ProdutosRepository;
import jp_2026_Manoel_Souza.repository.UsoCupomRepository;
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
    private final PromocaoRepository promocaoRepository;
    private final UsoCupomRepository usoCupomRepository;
    private final PedidoMapper pedidoMapper;

    @Transactional
    public PedidoResponse criarPedido(CriarPedidoRequest request) {
        Carrinho carrinho = buscarCarrinhoAtivo(request.usuarioId());
        List<ItemCarrinho> itensCarrinho = itemCarrinhoRepository.findByCarrinhoId(carrinho.getId());

        if (itensCarrinho.isEmpty()) {
            throw new RegraDeNegocioException("Não é possível criar um pedido a partir de um carrinho vazio.");
        }

        validarEstoqueItens(itensCarrinho);

        Promocao promocao = carrinho.getPromocao();
        BigDecimal desconto = carrinho.getDescontoAplicado() == null
                ? BigDecimal.ZERO
                : carrinho.getDescontoAplicado();

        BigDecimal frete = request.frete() == null ? BigDecimal.ZERO : request.frete();
        BigDecimal valorItens = calcularValorItens(itensCarrinho);
        BigDecimal valorTotal = valorItens.subtract(desconto).add(frete);

        if (valorTotal.compareTo(BigDecimal.ZERO) < 0) {
            valorTotal = BigDecimal.ZERO;
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(request.usuarioId());
        pedido.setEndereco(request.endereco().trim());
        pedido.setDesconto(desconto);
        pedido.setFrete(frete);
        pedido.setValorTotal(valorTotal);
        pedido.setStatus(StatusPedido.CREATED);
        pedido.setPromocao(promocao);

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

        consumirCupom(promocao, request.usuarioId());

        carrinho.setStatus(StatusCarrinho.FINALIZADO);
        carrinho.setPromocao(null);
        carrinho.setDescontoAplicado(BigDecimal.ZERO);
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
            throw new RegraDeNegocioException("Cancelamento permitido somente para pedidos com status CRIADO ou PAGO.");
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
            throw new RegraDeNegocioException("Somente pedidos com status CRIADO podem ser marcados como PAGOS.");
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
            throw new RegraDeNegocioException("Somente pedidos com status PAGO podem ser marcados como ENVIADOS.");
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
            throw new RegraDeNegocioException("Somente pedidos com status ENVIADO podem ser marcados como ENTREGUES.");
        }

        pedido.setStatus(StatusPedido.DELIVERED);
        pedido = pedidoRepository.save(pedido);

        List<ItemPedido> itensPedido = itemPedidoRepository.findByPedidoId(pedido.getId());

        return pedidoMapper.paraResponse(pedido, itensPedido);
    }

    private Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado com o ID: " + id));
    }

    private Carrinho buscarCarrinhoAtivo(Long usuarioId) {
        return carrinhoRepository.findByUsuarioIdAndStatus(usuarioId, StatusCarrinho.ATIVO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carrinho ativo não encontrado para o usuário ID: " + usuarioId));
    }

    private void validarEstoqueItens(List<ItemCarrinho> itensCarrinho) {
        for (ItemCarrinho itemCarrinho : itensCarrinho) {
            Produtos produto = itemCarrinho.getProduto();

            if (!Boolean.TRUE.equals(produto.getAtivo())) {
                throw new RegraDeNegocioException("Produto '" + produto.getNome() + "' está inativo e não pode ser comprado.");
            }

            if (produto.getQuantidadeEstoque() < itemCarrinho.getQuantidade()) {
                throw new EstoqueInsuficienteException("Estoque insuficiente para o produto: " + produto.getNome());
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

    private void consumirCupom(Promocao promocao, Long usuarioId) {
        if (promocao == null) {
            return;
        }

        if (promocao.getQuantidadeUsada() >= promocao.getLimiteUso()) {
            throw new RegraDeNegocioException("Cupom '" + promocao.getCodigo() + "' atingiu o limite de uso.");
        }

        if (usoCupomRepository.existsByPromocaoIdAndUsuarioId(promocao.getId(), usuarioId)) {
            throw new RegraDeNegocioException("Cupom '" + promocao.getCodigo() + "' já foi utilizado por este usuário.");
        }

        UsoCupom usoCupom = new UsoCupom();
        usoCupom.setPromocao(promocao);
        usoCupom.setUsuarioId(usuarioId);
        usoCupomRepository.save(usoCupom);

        promocao.setQuantidadeUsada(promocao.getQuantidadeUsada() + 1);
        promocaoRepository.save(promocao);
    }

    private void atualizarFlagEstoqueBaixo(Produtos produto) {
        produto.setEstoqueBaixo(produto.getQuantidadeEstoque() <= produto.getEstoqueMinimo());
    }
}