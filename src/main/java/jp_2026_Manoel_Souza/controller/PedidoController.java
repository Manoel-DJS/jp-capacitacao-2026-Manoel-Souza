package jp_2026_Manoel_Souza.controller;

import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.config.PedidoControllerDocs;
import jp_2026_Manoel_Souza.dto.request.CriarPedidoRequest;
import jp_2026_Manoel_Souza.dto.response.PedidoResponse;
import jp_2026_Manoel_Souza.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController implements PedidoControllerDocs {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(@Valid @RequestBody CriarPedidoRequest request) {
        return ResponseEntity.ok(pedidoService.criarPedido(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(id));
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<PedidoResponse> marcarComoPago(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.marcarComoPago(id));
    }

    @PostMapping("/{id}/enviar")
    public ResponseEntity<PedidoResponse> marcarComoEnviado(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.marcarComoEnviado(id));
    }

    @PostMapping("/{id}/entregar")
    public ResponseEntity<PedidoResponse> marcarComoEntregue(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.marcarComoEntregue(id));
    }
}