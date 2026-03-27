package jp_2026_Manoel_Souza.controller;

import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.config.AvaliacaoControllerDocs;
import jp_2026_Manoel_Souza.dto.request.CriarAvaliacaoRequest;
import jp_2026_Manoel_Souza.dto.response.AvaliacaoResponse;
import jp_2026_Manoel_Souza.dto.response.AvaliacoesProdutoResponse;
import jp_2026_Manoel_Souza.service.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/avaliacoes")
public class AvaliacaoController implements AvaliacaoControllerDocs {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<AvaliacaoResponse> criarAvaliacao(@Valid @RequestBody CriarAvaliacaoRequest request) {
        return ResponseEntity.ok(avaliacaoService.criarAvaliacao(request));
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<AvaliacoesProdutoResponse> buscarAvaliacoesPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(avaliacaoService.buscarAvaliacoesPorProduto(produtoId));
    }
}