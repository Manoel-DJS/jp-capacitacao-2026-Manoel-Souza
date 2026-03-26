package jp_2026_Manoel_Souza.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp_2026_Manoel_Souza.dto.response.AuditoriaResponse;
import jp_2026_Manoel_Souza.mapper.AuditoriaMapper;
import jp_2026_Manoel_Souza.model.Auditoria;
import jp_2026_Manoel_Souza.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;
    private final ObjectMapper objectMapper;

    public void registrar(String entidade,
                          Long entidadeId,
                          String acao,
                          Object antes,
                          Object depois,
                          String usuario) {

        Auditoria auditoria = new Auditoria();
        auditoria.setEntidade(entidade);
        auditoria.setEntidadeId(entidadeId);
        auditoria.setAcao(acao);
        auditoria.setAntesJson(paraJson(antes));
        auditoria.setDepoisJson(paraJson(depois));
        auditoria.setUsuario(usuario);

        auditoriaRepository.save(auditoria);
    }

    public List<AuditoriaResponse> buscarPorEntidade(String entidade) {
        List<Auditoria> auditorias = auditoriaRepository.findByEntidadeOrderByDataHoraDesc(entidade);
        List<AuditoriaResponse> response = new ArrayList<>();

        for (Auditoria auditoria : auditorias) {
            response.add(auditoriaMapper.paraResponse(auditoria));
        }

        return response;
    }

    private String paraJson(Object objeto) {
        if (objeto == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(objeto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter auditoria para JSON");
        }
    }
}