package jp_2026_Manoel_Souza.controller;

import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.config.CarrinhoControllerDocs;
import jp_2026_Manoel_Souza.dto.request.AdicionarItemCarrinhoRequest;
import jp_2026_Manoel_Souza.dto.request.AtualizarItemCarrinhoRequest;
import jp_2026_Manoel_Souza.dto.response.CarrinhoResponse;
import jp_2026_Manoel_Souza.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carrinho")
public class CarrinhoController implements CarrinhoControllerDocs {

    private final CarrinhoService carrinhoService;

    @GetMapping
    public ResponseEntity<CarrinhoResponse> buscarCarrinho(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(carrinhoService.buscarCarrinho(usuarioId));
    }

    @PostMapping("/itens")
    public ResponseEntity<CarrinhoResponse> adicionarItem(@Valid @RequestBody AdicionarItemCarrinhoRequest request) {
        return ResponseEntity.ok(carrinhoService.adicionarItem(request));
    }

    @PutMapping("/itens/{itemId}")
    public ResponseEntity<CarrinhoResponse> atualizarItem(@PathVariable Long itemId,
                                                          @Valid @RequestBody AtualizarItemCarrinhoRequest request) {
        return ResponseEntity.ok(carrinhoService.atualizarItem(itemId, request));
    }

    @DeleteMapping("/itens/{itemId}")
    public ResponseEntity<Void> removerItem(@PathVariable Long itemId) {
        carrinhoService.removerItem(itemId);
        return ResponseEntity.noContent().build();
    }
}