package jp_2026_Manoel_Souza.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.dto.request.AdicionarItemCarrinhoRequest;
import jp_2026_Manoel_Souza.dto.request.AtualizarItemCarrinhoRequest;
import jp_2026_Manoel_Souza.dto.response.CarrinhoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Carrinho", description = "Endpoints para gerenciamento do carrinho de compras")
public interface CarrinhoControllerDocs {

    @Operation(summary = "Buscar carrinho", description = "Retorna o carrinho ativo do usuário informado")
    @GetMapping
    ResponseEntity<CarrinhoResponse> buscarCarrinho(@RequestParam Long usuarioId);

    @Operation(summary = "Adicionar item ao carrinho", description = "Adiciona um produto ao carrinho do usuário")
    @PostMapping("/itens")
    ResponseEntity<CarrinhoResponse> adicionarItem(@Valid @RequestBody AdicionarItemCarrinhoRequest request);

    @Operation(summary = "Atualizar item do carrinho", description = "Atualiza a quantidade de um item já existente no carrinho")
    @PutMapping("/itens/{itemId}")
    ResponseEntity<CarrinhoResponse> atualizarItem(@PathVariable Long itemId,
                                                   @Valid @RequestBody AtualizarItemCarrinhoRequest request);

    @Operation(summary = "Remover item do carrinho", description = "Remove um item do carrinho")
    @DeleteMapping("/itens/{itemId}")
    ResponseEntity<Void> removerItem(@PathVariable Long itemId);
}