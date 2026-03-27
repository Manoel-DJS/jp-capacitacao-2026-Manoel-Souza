package jp_2026_Manoel_Souza.controller;

import jp_2026_Manoel_Souza.config.RelatorioControllerDocs;
import jp_2026_Manoel_Souza.dto.response.ProdutoEstoqueBaixoResponse;
import jp_2026_Manoel_Souza.dto.response.ProdutoMaisVendidoResponse;
import jp_2026_Manoel_Souza.dto.response.PromocaoMaisUtilizadaResponse;
import jp_2026_Manoel_Souza.dto.response.RelatorioVendasResponse;
import jp_2026_Manoel_Souza.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/relatorios")
public class RelatorioController implements RelatorioControllerDocs {

    private final RelatorioService relatorioService;

    @GetMapping("/vendas")
    public ResponseEntity<RelatorioVendasResponse> buscarRelatorioVendas(
            @RequestParam LocalDate dataInicio,
            @RequestParam LocalDate dataFim
    ) {
        return ResponseEntity.ok(relatorioService.buscarRelatorioVendas(dataInicio, dataFim));
    }

    @GetMapping("/produtos-mais-vendidos")
    public ResponseEntity<List<ProdutoMaisVendidoResponse>> buscarProdutosMaisVendidos() {
        return ResponseEntity.ok(relatorioService.buscarProdutosMaisVendidos());
    }

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ProdutoEstoqueBaixoResponse>> buscarProdutosComEstoqueBaixo() {
        return ResponseEntity.ok(relatorioService.buscarProdutosComEstoqueBaixo());
    }

    @GetMapping("/promocoes-mais-utilizadas")
    public ResponseEntity<List<PromocaoMaisUtilizadaResponse>> buscarPromocoesMaisUtilizadas() {
        return ResponseEntity.ok(relatorioService.buscarPromocoesMaisUtilizadas());
    }
}