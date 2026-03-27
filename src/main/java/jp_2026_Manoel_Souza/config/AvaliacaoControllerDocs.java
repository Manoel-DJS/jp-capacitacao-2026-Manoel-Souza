package jp_2026_Manoel_Souza.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.dto.request.CriarAvaliacaoRequest;
import jp_2026_Manoel_Souza.dto.response.AvaliacaoResponse;
import jp_2026_Manoel_Souza.dto.response.AvaliacoesProdutoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Avaliações", description = "Endpoints para criação e consulta de avaliações de produtos")
public interface AvaliacaoControllerDocs {

    @Operation(summary = "Criar avaliação", description = "Cria uma avaliação para um produto vinculado a um pedido entregue")
    @PostMapping
    ResponseEntity<AvaliacaoResponse> criarAvaliacao(@Valid @RequestBody CriarAvaliacaoRequest request);

    @Operation(summary = "Buscar avaliações por produto", description = "Retorna a lista de avaliações e o resumo de um produto")
    @GetMapping("/produto/{produtoId}")
    ResponseEntity<AvaliacoesProdutoResponse> buscarAvaliacoesPorProduto(@PathVariable Long produtoId);
}