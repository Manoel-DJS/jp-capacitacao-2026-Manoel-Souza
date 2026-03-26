package jp_2026_Manoel_Souza.service;

import jp_2026_Manoel_Souza.dto.request.CriarAvaliacaoRequest;
import jp_2026_Manoel_Souza.dto.response.AvaliacaoResponse;
import jp_2026_Manoel_Souza.dto.response.AvaliacoesProdutoResponse;
import jp_2026_Manoel_Souza.mapper.AvaliacaoMapper;
import jp_2026_Manoel_Souza.model.Avaliacao;
import jp_2026_Manoel_Souza.model.Pedido;
import jp_2026_Manoel_Souza.model.Produtos;
import jp_2026_Manoel_Souza.model.enums.StatusPedido;
import jp_2026_Manoel_Souza.repository.AvaliacaoRepository;
import jp_2026_Manoel_Souza.repository.ItemPedidoRepository;
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
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutosRepository produtosRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final AvaliacaoMapper avaliacaoMapper;

    @Transactional
    public AvaliacaoResponse criarAvaliacao(CriarAvaliacaoRequest request) {
        Pedido pedido = pedidoRepository.findById(request.pedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (!pedido.getUsuarioId().equals(request.usuarioId())) {
            throw new RuntimeException("O pedido não pertence ao usuário informado");
        }

        if (pedido.getStatus() != StatusPedido.DELIVERED) {
            throw new RuntimeException("Só é permitido avaliar pedidos com status DELIVERED");
        }

        Produtos produto = produtosRepository.findByIdAndAtivoTrue(request.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!itemPedidoRepository.existsByPedidoIdAndProdutoId(request.pedidoId(), request.produtoId())) {
            throw new RuntimeException("O produto não pertence ao pedido informado");
        }

        if (avaliacaoRepository.existsByPedidoIdAndProdutoId(request.pedidoId(), request.produtoId())) {
            throw new RuntimeException("Já existe avaliação para esse produto nesse pedido");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setPedido(pedido);
        avaliacao.setProduto(produto);
        avaliacao.setUsuarioId(request.usuarioId());
        avaliacao.setNota(request.nota());
        avaliacao.setComentario(request.comentario());

        avaliacao = avaliacaoRepository.save(avaliacao);

        atualizarResumoProduto(produto.getId());

        return avaliacaoMapper.paraResponse(avaliacao);
    }

    public AvaliacoesProdutoResponse buscarAvaliacoesPorProduto(Long produtoId) {
        Produtos produto = produtosRepository.findByIdAndAtivoTrue(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        List<Avaliacao> avaliacoes = avaliacaoRepository.findByProdutoIdOrderByDataCriacaoDesc(produtoId);
        List<AvaliacaoResponse> lista = new ArrayList<>();

        for (Avaliacao avaliacao : avaliacoes) {
            lista.add(avaliacaoMapper.paraResponse(avaliacao));
        }

        return new AvaliacoesProdutoResponse(
                produto.getId(),
                produto.getMediaAvaliacoes(),
                produto.getQuantidadeAvaliacoes(),
                lista
        );
    }

    private void atualizarResumoProduto(Long produtoId) {
        Produtos produto = produtosRepository.findByIdAndAtivoTrue(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Integer quantidade = avaliacaoRepository.countByProdutoId(produtoId);
        BigDecimal media = avaliacaoRepository.calcularMediaPorProduto(produtoId);

        if (quantidade == null) {
            quantidade = 0;
        }

        if (media == null) {
            media = BigDecimal.ZERO;
        }

        produto.setQuantidadeAvaliacoes(quantidade);
        produto.setMediaAvaliacoes(media);
        produtosRepository.save(produto);
    }
}