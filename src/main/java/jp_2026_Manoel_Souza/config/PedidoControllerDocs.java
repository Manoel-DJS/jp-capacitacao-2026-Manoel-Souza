package jp_2026_Manoel_Souza.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.dto.request.CriarPedidoRequest;
import jp_2026_Manoel_Souza.dto.response.PedidoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pedidos", description = "Endpoints para criação, consulta e atualização de status de pedidos")
public interface PedidoControllerDocs {

    @Operation(summary = "Criar pedido", description = "Cria um pedido a partir do carrinho ativo do usuário")
    @PostMapping
    ResponseEntity<PedidoResponse> criarPedido(@Valid @RequestBody CriarPedidoRequest request);

    @Operation(summary = "Buscar pedido por id", description = "Retorna os dados de um pedido pelo id")
    @GetMapping("/{id}")
    ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id);

    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido permitido e devolve o estoque quando aplicável")
    @PostMapping("/{id}/cancelar")
    ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable Long id);

    @Operation(summary = "Marcar pedido como pago", description = "Altera o status do pedido para PAID")
    @PostMapping("/{id}/pagar")
    ResponseEntity<PedidoResponse> marcarComoPago(@PathVariable Long id);

    @Operation(summary = "Marcar pedido como enviado", description = "Altera o status do pedido para SHIPPED")
    @PostMapping("/{id}/enviar")
    ResponseEntity<PedidoResponse> marcarComoEnviado(@PathVariable Long id);

    @Operation(summary = "Marcar pedido como entregue", description = "Altera o status do pedido para DELIVERED")
    @PostMapping("/{id}/entregar")
    ResponseEntity<PedidoResponse> marcarComoEntregue(@PathVariable Long id);
}