package jp_2026_Manoel_Souza.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.dto.request.AtualizarCategoriaRequest;
import jp_2026_Manoel_Souza.dto.request.CriarCategoriaRequest;
import jp_2026_Manoel_Souza.dto.response.CategoriaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias")
public interface CategoriaControllerDocs {

    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria")
    @PostMapping
    ResponseEntity<CategoriaResponse> criar(@Valid @RequestBody CriarCategoriaRequest request);

    @Operation(summary = "Listar categorias", description = "Retorna todas as categorias cadastradas")
    @GetMapping
    ResponseEntity<List<CategoriaResponse>> listar();

    @Operation(summary = "Buscar categoria por id", description = "Retorna os dados de uma categoria pelo id")
    @GetMapping("/{id}")
    ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id);

    @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria existente")
    @PutMapping("/{id}")
    ResponseEntity<CategoriaResponse> atualizar(@PathVariable Long id,
                                                @Valid @RequestBody AtualizarCategoriaRequest request);

    @Operation(summary = "Excluir categoria", description = "Exclui uma categoria pelo id")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> excluir(@PathVariable Long id);
}