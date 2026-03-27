package jp_2026_Manoel_Souza.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.dto.request.AplicarCupomRequest;
import jp_2026_Manoel_Souza.dto.request.CriarPromocaoRequest;
import jp_2026_Manoel_Souza.dto.response.CupomAplicadoResponse;
import jp_2026_Manoel_Souza.dto.response.PromocaoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Promoções e Cupons", description = "Endpoints para criação de promoções e aplicação de cupons")
public interface PromocaoControllerDocs {

    @Operation(summary = "Criar promoção", description = "Cria uma nova promoção ou cupom")
    @PostMapping("/promocoes")
    ResponseEntity<PromocaoResponse> criarPromocao(@Valid @RequestBody CriarPromocaoRequest request);

    @Operation(summary = "Aplicar cupom", description = "Aplica um cupom ao carrinho do usuário")
    @PostMapping("/cupons/aplicar")
    ResponseEntity<CupomAplicadoResponse> aplicarCupom(@Valid @RequestBody AplicarCupomRequest request);
}