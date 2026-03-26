package jp_2026_Manoel_Souza.controller;

import jakarta.validation.Valid;
import jp_2026_Manoel_Souza.dto.request.AplicarCupomRequest;
import jp_2026_Manoel_Souza.dto.request.CriarPromocaoRequest;
import jp_2026_Manoel_Souza.dto.response.CupomAplicadoResponse;
import jp_2026_Manoel_Souza.dto.response.PromocaoResponse;
import jp_2026_Manoel_Souza.service.PromocaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class PromocaoController {

    private final PromocaoService promocaoService;

    @PostMapping("/promocoes")
    public ResponseEntity<PromocaoResponse> criarPromocao(@Valid @RequestBody CriarPromocaoRequest request) {
        return ResponseEntity.ok(promocaoService.criarPromocao(request));
    }

    @PostMapping("/cupons/aplicar")
    public ResponseEntity<CupomAplicadoResponse> aplicarCupom(@Valid @RequestBody AplicarCupomRequest request) {
        return ResponseEntity.ok(promocaoService.aplicarCupom(request));
    }
}