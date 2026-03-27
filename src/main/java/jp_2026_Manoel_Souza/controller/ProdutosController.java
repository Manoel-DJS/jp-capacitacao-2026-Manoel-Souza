package jp_2026_Manoel_Souza.controller;

import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.config.ProdutosControllerDocs;
import jp_2026_Manoel_Souza.dto.request.AtualizarProdutoRequest;
import jp_2026_Manoel_Souza.dto.request.CriarProdutoRequest;
import jp_2026_Manoel_Souza.dto.response.ProdutoResponse;
import jp_2026_Manoel_Souza.service.ProdutosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos")
public class ProdutosController implements ProdutosControllerDocs {

    private final ProdutosService produtosService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> criarProduto(@Valid @RequestBody CriarProdutoRequest request) {
        return ResponseEntity.ok(produtosService.createdProduto(request));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> getAll() {
        return ResponseEntity.ok(produtosService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(produtosService.getById(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponse>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long categoriaId
    ) {
        return ResponseEntity.ok(produtosService.buscar(nome, categoriaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizarProduto(@PathVariable Long id,
                                                            @Valid @RequestBody AtualizarProdutoRequest request) {
        return ResponseEntity.ok(produtosService.atualiza(id, request));
    }

    @PatchMapping("/{id}/preco")
    public ResponseEntity<ProdutoResponse> atualizarProdutoParcial(@PathVariable Long id,
                                                                   @RequestParam BigDecimal preco) {
        return ResponseEntity.ok(produtosService.atualizaPreco(id, preco));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtosService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}