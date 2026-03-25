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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutosService {

    private final ProdutosRepository produtosRepository;
    private final CategoriaRepository categoriaRepository;
    private final HistoricoPrecoRepository historicoPrecoRepository;
    private final ProdutoMapper produtoMapper;

    public List<ProdutoResponse> getAll() {
        List<Produtos> produtos = produtosRepository.findAll();
        List<ProdutoResponse> response = new ArrayList<>();

        for (Produtos produto : produtos) {
            response.add(produtoMapper.paraResponse(produto));
        }

        return response;
    }

    public ProdutoResponse getById(Long id) {
        Produtos produto = buscarProduto(id);
        return produtoMapper.paraResponse(produto);
    }

    public ProdutoResponse createdProduto(CriarProdutoRequest request) {
        Categoria categoria = buscarCategoria(request.categoriaId());

        Produtos produto = new Produtos();
        produto.setNome(request.nome().trim());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());
        produto.setCodigoBarras(request.codigoBarras());
        produto.setCategoria(categoria);

        produto = produtosRepository.save(produto);

        return produtoMapper.paraResponse(produto);
    }

    public ProdutoResponse atualiza(Long id, AtualizarProdutoRequest request) {
        Produtos produto = buscarProduto(id);
        Categoria categoria = buscarCategoria(request.categoriaId());

        produto.setNome(request.nome().trim());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());
        produto.setCodigoBarras(request.codigoBarras());
        produto.setCategoria(categoria);

        produto = produtosRepository.save(produto);

        return produtoMapper.paraResponse(produto);
    }

    public void deletarProduto(Long id) {
        Produtos produto = buscarProduto(id);
        produtosRepository.delete(produto);
    }

    public ProdutoResponse atualizaPreco(Long id, BigDecimal preco) {
        Produtos produto = buscarProduto(id);

        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Preço deve ser maior que zero");
        }

        BigDecimal precoAntigo = produto.getPreco();

        HistoricoPreco historico = new HistoricoPreco();
        historico.setProdutos(produto);
        historico.setPrecoAntigo(precoAntigo);
        historico.setPrecoNovo(preco);

        produto.setPreco(preco);

        historicoPrecoRepository.save(historico);
        produto = produtosRepository.saveAndFlush(produto);

        return produtoMapper.paraResponse(produto);
    }

    public List<ProdutoResponse> buscar(String nome, Long categoriaId) {
        List<Produtos> produtos;
        List<ProdutoResponse> response = new ArrayList<>();

        if (nome != null && !nome.trim().isEmpty() && categoriaId != null) {
            produtos = produtosRepository.findByNomeContainingIgnoreCaseAndCategoriaId(nome.trim(), categoriaId);
        } else if (nome != null && !nome.trim().isEmpty()) {
            produtos = produtosRepository.findByNomeContainingIgnoreCase(nome.trim());
        } else if (categoriaId != null) {
            produtos = produtosRepository.findByCategoriaId(categoriaId);
        } else {
            produtos = produtosRepository.findAll();
        }

        for (Produtos produto : produtos) {
            response.add(produtoMapper.paraResponse(produto));
        }

        return response;
    }

    private Produtos buscarProduto(Long id) {
        return produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    private Categoria buscarCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }
}