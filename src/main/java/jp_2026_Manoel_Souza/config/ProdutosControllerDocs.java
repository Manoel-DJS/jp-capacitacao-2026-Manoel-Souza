package jp_2026_Manoel_Souza.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.dto.request.AtualizarProdutoRequest;
import jp_2026_Manoel_Souza.dto.request.CriarProdutoRequest;
import jp_2026_Manoel_Souza.dto.response.ProdutoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
public interface ProdutosControllerDocs {

    @Operation(summary = "Criar produto", description = "Cria um novo produto")
    @PostMapping
    ResponseEntity<ProdutoResponse> criarProduto(@Valid @RequestBody CriarProdutoRequest request);

    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos ativos")
    @GetMapping
    ResponseEntity<List<ProdutoResponse>> getAll();

    @Operation(summary = "Buscar produto por id", description = "Retorna um produto pelo id")
    @GetMapping("/{id}")
    ResponseEntity<ProdutoResponse> getById(@PathVariable Long id);

    @Operation(summary = "Buscar produtos", description = "Busca produtos por nome e/ou categoria")
    @GetMapping("/buscar")
    ResponseEntity<List<ProdutoResponse>> buscar(@RequestParam(required = false) String nome,
                                                 @RequestParam(required = false) Long categoriaId);

    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto")
    @PutMapping("/{id}")
    ResponseEntity<ProdutoResponse> atualizarProduto(@PathVariable Long id,
                                                     @Valid @RequestBody AtualizarProdutoRequest request);

    @Operation(summary = "Atualizar preço do produto", description = "Atualiza apenas o preço do produto")
    @PatchMapping("/{id}/preco")
    ResponseEntity<ProdutoResponse> atualizarProdutoParcial(@PathVariable Long id,
                                                            @RequestParam BigDecimal preco);

    @Operation(summary = "Excluir produto", description = "Realiza a exclusão lógica de um produto")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarProduto(@PathVariable Long id);
}