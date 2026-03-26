package jp_2026_Manoel_Souza.mapper;

import jp_2026_Manoel_Souza.dto.response.AuditoriaResponse;
import jp_2026_Manoel_Souza.model.Auditoria;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    public AuditoriaResponse paraResponse(Auditoria auditoria) {
        return new AuditoriaResponse(
                auditoria.getId(),
                auditoria.getEntidade(),
                auditoria.getEntidadeId(),
                auditoria.getAcao(),
                auditoria.getAntesJson(),
                auditoria.getDepoisJson(),
                auditoria.getUsuario(),
                auditoria.getDataHora()
        );
    }
}