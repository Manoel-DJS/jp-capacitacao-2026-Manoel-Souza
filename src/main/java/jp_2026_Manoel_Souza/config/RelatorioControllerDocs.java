package jp_2026_Manoel_Souza.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jp_2026_Manoel_Souza.dto.response.ProdutoEstoqueBaixoResponse;
import jp_2026_Manoel_Souza.dto.response.ProdutoMaisVendidoResponse;
import jp_2026_Manoel_Souza.dto.response.PromocaoMaisUtilizadaResponse;
import jp_2026_Manoel_Souza.dto.response.RelatorioVendasResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Relatórios", description = "Endpoints para relatórios e métricas do sistema")
public interface RelatorioControllerDocs {

    @Operation(summary = "Relatório de vendas", description = "Retorna o faturamento por período")
    @GetMapping("/vendas")
    ResponseEntity<RelatorioVendasResponse> buscarRelatorioVendas(@RequestParam LocalDate dataInicio,
                                                                  @RequestParam LocalDate dataFim);

    @Operation(summary = "Produtos mais vendidos", description = "Retorna a lista dos produtos mais vendidos")
    @GetMapping("/produtos-mais-vendidos")
    ResponseEntity<List<ProdutoMaisVendidoResponse>> buscarProdutosMaisVendidos();

    @Operation(summary = "Produtos com estoque baixo", description = "Retorna a lista dos produtos com estoque baixo")
    @GetMapping("/estoque-baixo")
    ResponseEntity<List<ProdutoEstoqueBaixoResponse>> buscarProdutosComEstoqueBaixo();

    @Operation(summary = "Promoções mais utilizadas", description = "Retorna a lista das promoções mais utilizadas")
    @GetMapping("/promocoes-mais-utilizadas")
    ResponseEntity<List<PromocaoMaisUtilizadaResponse>> buscarPromocoesMaisUtilizadas();
}