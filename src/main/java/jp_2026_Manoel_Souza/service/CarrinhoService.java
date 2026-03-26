package jp_2026_Manoel_Souza.service;

import jp_2026_Manoel_Souza.dto.request.AdicionarItemCarrinhoRequest;
import jp_2026_Manoel_Souza.dto.request.AtualizarItemCarrinhoRequest;
import jp_2026_Manoel_Souza.dto.response.CarrinhoResponse;
import jp_2026_Manoel_Souza.mapper.CarrinhoMapper;
import jp_2026_Manoel_Souza.model.Carrinho;
import jp_2026_Manoel_Souza.model.ItemCarrinho;
import jp_2026_Manoel_Souza.model.Produtos;
import jp_2026_Manoel_Souza.model.enums.StatusCarrinho;
import jp_2026_Manoel_Souza.repository.CarrinhoRepository;
import jp_2026_Manoel_Souza.repository.ItemCarrinhoRepository;
import jp_2026_Manoel_Souza.repository.ProdutosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final ProdutosRepository produtosRepository;
    private final CarrinhoMapper carrinhoMapper;

    public CarrinhoResponse buscarCarrinho(Long usuarioId) {
        Carrinho carrinho = buscarOuCriarCarrinhoAtivo(usuarioId);
        List<ItemCarrinho> itens = itemCarrinhoRepository.findByCarrinhoId(carrinho.getId());

        return carrinhoMapper.paraResponse(carrinho, itens);
    }

    @Transactional
    public CarrinhoResponse adicionarItem(AdicionarItemCarrinhoRequest request) {
        Carrinho carrinho = buscarOuCriarCarrinhoAtivo(request.usuarioId());
        Produtos produto = buscarProdutoAtivo(request.produtoId());

        validarEstoque(produto, request.quantidade());

        ItemCarrinho itemCarrinho = itemCarrinhoRepository
                .findByCarrinhoIdAndProdutoId(carrinho.getId(), produto.getId())
                .orElse(null);

        if (itemCarrinho == null) {
            itemCarrinho = new ItemCarrinho();
            itemCarrinho.setCarrinho(carrinho);
            itemCarrinho.setProduto(produto);
            itemCarrinho.setQuantidade(request.quantidade());
            itemCarrinho.setPrecoMomento(produto.getPreco());
        } else {
            Integer novaQuantidade = itemCarrinho.getQuantidade() + request.quantidade();
            validarEstoque(produto, novaQuantidade);
            itemCarrinho.setQuantidade(novaQuantidade);
        }

        itemCarrinhoRepository.save(itemCarrinho);

        List<ItemCarrinho> itens = itemCarrinhoRepository.findByCarrinhoId(carrinho.getId());
        return carrinhoMapper.paraResponse(carrinho, itens);
    }

    @Transactional
    public CarrinhoResponse atualizarItem(Long itemId, AtualizarItemCarrinhoRequest request) {
        ItemCarrinho itemCarrinho = buscarItemCarrinho(itemId);
        Produtos produto = itemCarrinho.getProduto();

        validarEstoque(produto, request.quantidade());

        itemCarrinho.setQuantidade(request.quantidade());
        itemCarrinhoRepository.save(itemCarrinho);

        Carrinho carrinho = itemCarrinho.getCarrinho();
        List<ItemCarrinho> itens = itemCarrinhoRepository.findByCarrinhoId(carrinho.getId());

        return carrinhoMapper.paraResponse(carrinho, itens);
    }

    @Transactional
    public void removerItem(Long itemId) {
        ItemCarrinho itemCarrinho = buscarItemCarrinho(itemId);
        itemCarrinhoRepository.delete(itemCarrinho);
    }

    private Carrinho buscarOuCriarCarrinhoAtivo(Long usuarioId) {
        Carrinho carrinho = carrinhoRepository
                .findByUsuarioIdAndStatus(usuarioId, StatusCarrinho.ATIVO)
                .orElse(null);

        if (carrinho != null) {
            return carrinho;
        }

        Carrinho novoCarrinho = new Carrinho();
        novoCarrinho.setUsuarioId(usuarioId);
        novoCarrinho.setStatus(StatusCarrinho.ATIVO);

        return carrinhoRepository.save(novoCarrinho);
    }

    private Produtos buscarProdutoAtivo(Long produtoId) {
        return produtosRepository.findByIdAndAtivoTrue(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    private ItemCarrinho buscarItemCarrinho(Long itemId) {
        return itemCarrinhoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item do carrinho não encontrado"));
    }

    private void validarEstoque(Produtos produto, Integer quantidadeSolicitada) {
        if (produto.getQuantidadeEstoque() < quantidadeSolicitada) {
            throw new RuntimeException("Estoque insuficiente");
        }
    }
}