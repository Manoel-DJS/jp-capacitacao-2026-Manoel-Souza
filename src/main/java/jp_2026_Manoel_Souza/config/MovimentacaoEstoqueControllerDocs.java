package jp_2026_Manoel_Souza.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.dto.request.AjustarEstoqueRequest;
import jp_2026_Manoel_Souza.dto.request.MovimentarEstoqueRequest;
import jp_2026_Manoel_Souza.dto.response.EstoqueProdutoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Estoque", description = "Endpoints para movimentação e consulta de estoque")
public interface MovimentacaoEstoqueControllerDocs {

    @Operation(summary = "Adicionar estoque", description = "Adiciona quantidade ao estoque do produto")
    @PostMapping("/{produtoId}/adicionar")
    ResponseEntity<Void> adicionarEstoque(@PathVariable Long produtoId,
                                          @Valid @RequestBody MovimentarEstoqueRequest request);

    @Operation(summary = "Remover estoque", description = "Remove quantidade do estoque do produto")
    @PostMapping("/{produtoId}/remover")
    ResponseEntity<Void> removerEstoque(@PathVariable Long produtoId,
                                        @Valid @RequestBody MovimentarEstoqueRequest request);

    @Operation(summary = "Ajustar estoque", description = "Ajusta o estoque do produto para uma nova quantidade final")
    @PostMapping("/{produtoId}/ajustar")
    ResponseEntity<Void> ajustarEstoque(@PathVariable Long produtoId,
                                        @Valid @RequestBody AjustarEstoqueRequest request);

    @Operation(summary = "Devolver estoque", description = "Devolve quantidade ao estoque do produto")
    @PostMapping("/{produtoId}/devolver")
    ResponseEntity<Void> devolverEstoque(@PathVariable Long produtoId,
                                         @Valid @RequestBody MovimentarEstoqueRequest request);

    @Operation(summary = "Buscar estoque do produto", description = "Retorna o resumo de estoque e o histórico de movimentações do produto")
    @GetMapping("/{produtoId}")
    ResponseEntity<EstoqueProdutoResponse> buscarEstoqueProduto(@PathVariable Long produtoId);
}