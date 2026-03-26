package jp_2026_Manoel_Souza.service;

import jp_2026_Manoel_Souza.dto.request.AjustarEstoqueRequest;
import jp_2026_Manoel_Souza.dto.request.MovimentarEstoqueRequest;
import jp_2026_Manoel_Souza.dto.response.EstoqueProdutoResponse;
import jp_2026_Manoel_Souza.dto.response.MovimentacaoEstoqueResponse;
import jp_2026_Manoel_Souza.mapper.MovimentacaoEstoqueMapper;
import jp_2026_Manoel_Souza.model.MovimentacaoEstoque;
import jp_2026_Manoel_Souza.model.Produtos;
import jp_2026_Manoel_Souza.model.enums.TipoMovimentacaoEstoque;
import jp_2026_Manoel_Souza.repository.MovimentacaoEstoqueRepository;
import jp_2026_Manoel_Souza.repository.ProdutosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimentacaoEstoqueService {

    private final ProdutosRepository produtosRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final MovimentacaoEstoqueMapper movimentacaoEstoqueMapper;

    @Transactional
    public void adicionarEstoque(Long produtoId, MovimentarEstoqueRequest request) {
        Produtos produto = buscarProdutoAtivo(produtoId);

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + request.quantidade());
        atualizarFlagEstoqueBaixo(produto);

        produtosRepository.save(produto);
        salvarMovimentacao(produto, request, TipoMovimentacaoEstoque.ENTRADA);
    }

    @Transactional
    public void removerEstoque(Long produtoId, MovimentarEstoqueRequest request) {
        Produtos produto = buscarProdutoAtivo(produtoId);

        if (produto.getQuantidadeEstoque() < request.quantidade()) {
            throw new RuntimeException("Estoque insuficiente");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - request.quantidade());
        atualizarFlagEstoqueBaixo(produto);

        produtosRepository.save(produto);
        salvarMovimentacao(produto, request, TipoMovimentacaoEstoque.SAIDA);
    }

    public EstoqueProdutoResponse buscarEstoqueProduto(Long produtoId) {
        Produtos produto = buscarProdutoAtivo(produtoId);
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findByProdutoIdOrderByDataCriacaoDesc(produtoId);
        List<MovimentacaoEstoqueResponse> movimentacoesResponse = new ArrayList<>();

        for (MovimentacaoEstoque movimentacao : movimentacoes) {
            movimentacoesResponse.add(movimentacaoEstoqueMapper.paraResponse(movimentacao));
        }

        return new EstoqueProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getQuantidadeEstoque(),
                produto.getEstoqueMinimo(),
                produto.getEstoqueBaixo(),
                movimentacoesResponse
        );
    }

    private Produtos buscarProdutoAtivo(Long produtoId) {
        return produtosRepository.findByIdAndAtivoTrue(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    private void atualizarFlagEstoqueBaixo(Produtos produto) {
        produto.setEstoqueBaixo(produto.getQuantidadeEstoque() <= produto.getEstoqueMinimo());
    }

    private void salvarMovimentacao(Produtos produto,
                                    MovimentarEstoqueRequest request,
                                    TipoMovimentacaoEstoque tipo) {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(request.quantidade());
        movimentacao.setTipo(tipo);
        movimentacao.setMotivo(request.motivo());
        movimentacao.setReferenciaId(request.referenciaId());
        movimentacao.setCriadoPor(request.criadoPor());

        movimentacaoEstoqueRepository.save(movimentacao);
    }

    @Transactional
    public void ajustarEstoque(Long produtoId, AjustarEstoqueRequest request) {
        Produtos produto = buscarProdutoAtivo(produtoId);
        Integer quantidadeAtual = produto.getQuantidadeEstoque();

        if (quantidadeAtual.equals(request.novaQuantidade())) {
            throw new RuntimeException("A nova quantidade deve ser diferente da quantidade atual");
        }

        Integer diferenca = Math.abs(request.novaQuantidade() - quantidadeAtual);

        produto.setQuantidadeEstoque(request.novaQuantidade());
        atualizarFlagEstoqueBaixo(produto);

        produtosRepository.save(produto);
        salvarMovimentacaoAjuste(produto, request, diferenca);
    }

    private void salvarMovimentacaoAjuste(Produtos produto,
                                          AjustarEstoqueRequest request,
                                          Integer quantidade) {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setTipo(TipoMovimentacaoEstoque.AJUSTE);
        movimentacao.setMotivo(request.motivo());
        movimentacao.setReferenciaId(request.referenciaId());
        movimentacao.setCriadoPor(request.criadoPor());

        movimentacaoEstoqueRepository.save(movimentacao);
    }

    @Transactional
    public void devolverEstoque(Long produtoId, MovimentarEstoqueRequest request) {
        Produtos produto = buscarProdutoAtivo(produtoId);

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + request.quantidade());
        atualizarFlagEstoqueBaixo(produto);

        produtosRepository.save(produto);
        salvarMovimentacao(produto, request, TipoMovimentacaoEstoque.DEVOLUCAO);
    }


}