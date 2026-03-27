package jp_2026_Manoel_Souza.service;


import jp_2026_Manoel_Souza.dto.response.ProdutoEstoqueBaixoResponse;
import jp_2026_Manoel_Souza.dto.response.ProdutoMaisVendidoResponse;

import jp_2026_Manoel_Souza.dto.response.PromocaoMaisUtilizadaResponse;
import jp_2026_Manoel_Souza.dto.response.RelatorioVendasResponse;
import jp_2026_Manoel_Souza.model.Promocao;
import jp_2026_Manoel_Souza.model.Produtos;
import jp_2026_Manoel_Souza.repository.ItemPedidoRepository;
import jp_2026_Manoel_Souza.repository.PedidoRepository;
import jp_2026_Manoel_Souza.repository.PromocaoRepository;
import jp_2026_Manoel_Souza.repository.ProdutosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutosRepository produtosRepository;
    private final PromocaoRepository promocaoRepository;

    public RelatorioVendasResponse buscarRelatorioVendas(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(23, 59, 59);

        Long quantidadePedidos = pedidoRepository.contarPedidosFaturadosPorPeriodo(inicio, fim);
        BigDecimal valorTotal = pedidoRepository.somarFaturamentoPorPeriodo(inicio, fim);

        if (quantidadePedidos == null) {
            quantidadePedidos = 0L;
        }

        if (valorTotal == null) {
            valorTotal = BigDecimal.ZERO;
        }

        return new RelatorioVendasResponse(
                dataInicio,
                dataFim,
                quantidadePedidos,
                valorTotal
        );
    }

    public List<ProdutoMaisVendidoResponse> buscarProdutosMaisVendidos() {
        List<Object[]> resultado = itemPedidoRepository.buscarProdutosMaisVendidos();
        List<ProdutoMaisVendidoResponse> response = new ArrayList<>();

        for (Object[] linha : resultado) {
            Long produtoId = (Long) linha[0];
            String nomeProduto = (String) linha[1];
            Long quantidadeLong = (Long) linha[2];

            response.add(new ProdutoMaisVendidoResponse(
                    produtoId,
                    nomeProduto,
                    quantidadeLong.intValue()
            ));
        }

        return response;
    }

    public List<ProdutoEstoqueBaixoResponse> buscarProdutosComEstoqueBaixo() {
        List<Produtos> produtos = produtosRepository.findByAtivoTrueAndEstoqueBaixoTrueOrderByQuantidadeEstoqueAsc();
        List<ProdutoEstoqueBaixoResponse> response = new ArrayList<>();

        for (Produtos produto : produtos) {
            response.add(new ProdutoEstoqueBaixoResponse(
                    produto.getId(),
                    produto.getNome(),
                    produto.getQuantidadeEstoque(),
                    produto.getEstoqueMinimo()
            ));
        }

        return response;
    }

    public List<PromocaoMaisUtilizadaResponse> buscarPromocoesMaisUtilizadas() {
        List<Promocao> promocoes = promocaoRepository.findByAtivoTrueOrderByQuantidadeUsadaDesc();
        List<PromocaoMaisUtilizadaResponse> response = new ArrayList<>();

        for (Promocao promocao : promocoes) {
            response.add(new PromocaoMaisUtilizadaResponse(
                    promocao.getId(),
                    promocao.getCodigo(),
                    promocao.getQuantidadeUsada()
            ));
        }

        return response;
    }
}