package jp_2026_Manoel_Souza.controller;

import jp_2026_Manoel_Souza.dto.response.AuditoriaResponse;
import jp_2026_Manoel_Souza.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<AuditoriaResponse>> buscarPorEntidade(@RequestParam String entidade) {
        return ResponseEntity.ok(auditoriaService.buscarPorEntidade(entidade));
    }
}