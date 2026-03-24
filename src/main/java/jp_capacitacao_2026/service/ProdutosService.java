package jp_capacitacao_2026.service;


import jp_capacitacao_2026.model.HistoricoPreco;
import jp_capacitacao_2026.model.Produtos;
import jp_capacitacao_2026.repository.HistoricoPrecoRepository;
import jp_capacitacao_2026.repository.ProdutosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutosService {

    private final ProdutosRepository produtosRepository;
    private final HistoricoPrecoRepository historicoPrecoRepository;

    public List<Produtos> getAll() {
        return produtosRepository.findAll();
    }

    public Produtos createdProduto(Produtos produto) {
        return produtosRepository.save(produto);
    }

    public Produtos atualiza(Produtos produto) {
        return produtosRepository.save(produto);
    }

    public void deletarProduto(Long id) {
        produtosRepository.deleteById(id);
    }

    public Produtos getById(Long id) {
        return produtosRepository.findById(id).get();
    }

    public Produtos atualizaPreco(Long id, BigDecimal preco) {
//        Produtos produto = produtosRepository.findById(id).get();
        final var produto = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setPreco(preco);
        /***
         * Rastreabilidade
         * 1 - Criar um log
         * 2 - Adicionar em tabela historico de preços valores old e new
         * para cada produto atualizado
         * 3 - Antes de atualizar a tabela de produto, pegar o valor atual da tabela e adiconar
         * na tabela historico
         * 4 - Pegar novo valor da tabela e adicionar na tabela historico
         * 5 - Sempre na tabela, adicionar novo registro após atualizar tabela de produto
         * Estrutura da tabela historico de preços
         * id
         * id_produto
         * preco_antigo
         * preco_novo
         * data_alteracao
         */
        final var historico = new HistoricoPreco();
        historico.setPrecoAntigo(produto.getPreco());
        historico.setProdutos(produto);
        historico.setPrecoNovo(preco);
        //Código abaixo pode ser substituido por @CreationTimestamp
//        historico.setDataAlteracao(LocalDateTime.now());
        historicoPrecoRepository.save(historico);
        return produtosRepository.saveAndFlush(produto);

        //Exemplo de não se fazer por gerar retrabalho
//        final var historicoNovo = historicoPrecoRepository.findById(historico.getId()).get();
//        historicoNovo.setPrecoNovo(preco);
//        historicoPrecoRepository.save(historicoNovo);
        /**
         * get na tabela produtos para novo preço
         */
//        return produto;
    }
}
