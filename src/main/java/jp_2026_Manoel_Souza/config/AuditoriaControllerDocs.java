package jp_2026_Manoel_Souza.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jp_2026_Manoel_Souza.dto.response.AuditoriaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Auditorias", description = "Endpoints para consulta de logs de auditoria")
public interface AuditoriaControllerDocs {

    @Operation(summary = "Buscar auditorias por entidade", description = "Retorna os registros de auditoria de uma entidade informada")
    @GetMapping
    ResponseEntity<List<AuditoriaResponse>> buscarPorEntidade(@RequestParam String entidade);
}