package jp_2026_Manoel_Souza.controller;

import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.dto.request.AjustarEstoqueRequest;
import jp_2026_Manoel_Souza.dto.request.MovimentarEstoqueRequest;
import jp_2026_Manoel_Souza.dto.response.EstoqueProdutoResponse;
import jp_2026_Manoel_Souza.service.MovimentacaoEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/estoque")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    @PostMapping("/{produtoId}/adicionar")
    public ResponseEntity<Void> adicionarEstoque(@PathVariable Long produtoId,
                                                 @Valid @RequestBody MovimentarEstoqueRequest request) {
        movimentacaoEstoqueService.adicionarEstoque(produtoId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{produtoId}/remover")
    public ResponseEntity<Void> removerEstoque(@PathVariable Long produtoId,
                                               @Valid @RequestBody MovimentarEstoqueRequest request) {
        movimentacaoEstoqueService.removerEstoque(produtoId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{produtoId}/ajustar")
    public ResponseEntity<Void> ajustarEstoque(@PathVariable Long produtoId,
                                               @Valid @RequestBody AjustarEstoqueRequest request) {
        movimentacaoEstoqueService.ajustarEstoque(produtoId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{produtoId}/devolver")
    public ResponseEntity<Void> devolverEstoque(@PathVariable Long produtoId,
                                                @Valid @RequestBody MovimentarEstoqueRequest request) {
        movimentacaoEstoqueService.devolverEstoque(produtoId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<EstoqueProdutoResponse> buscarEstoqueProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(movimentacaoEstoqueService.buscarEstoqueProduto(produtoId));
    }
}


