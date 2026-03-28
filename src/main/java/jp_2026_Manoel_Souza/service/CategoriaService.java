package jp_2026_Manoel_Souza.service;

import jakarta.transaction.Transactional;
import java.util.List;
import jp_2026_Manoel_Souza.dto.request.AtualizarCategoriaRequest;
import jp_2026_Manoel_Souza.dto.request.CriarCategoriaRequest;
import jp_2026_Manoel_Souza.dto.response.CategoriaResponse;
import jp_2026_Manoel_Souza.exception.DuplicidadeException;
import jp_2026_Manoel_Souza.exception.RecursoNaoEncontradoException;
import jp_2026_Manoel_Souza.exception.RegraDeNegocioException;
import jp_2026_Manoel_Souza.mapper.CategoriaMapper;
import jp_2026_Manoel_Souza.model.Categoria;
import jp_2026_Manoel_Souza.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Transactional
    public CategoriaResponse criar(CriarCategoriaRequest request) {
        Categoria categoriaPrincipal = null;

        if (request.categoriaPrincipalId() != null) {
            categoriaPrincipal = categoriaRepository.findById(request.categoriaPrincipalId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Categoria principal não encontrada com id: " + request.categoriaPrincipalId()
                    ));
        }

        validarDuplicidade(request.nome(), request.categoriaPrincipalId(), null);

        Categoria categoria = new Categoria();
        categoria.setNome(request.nome().trim());
        categoria.setCategoriaPrincipal(categoriaPrincipal);

        categoria = categoriaRepository.save(categoria);

        return categoriaMapper.paraResponse(categoria);
    }

    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::paraResponse)
                .toList();
    }

    public CategoriaResponse buscarPorId(Long id) {
        Categoria categoria = buscarCategoria(id);
        return categoriaMapper.paraResponse(categoria);
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, AtualizarCategoriaRequest request) {
        Categoria categoria = buscarCategoria(id);
        Categoria categoriaPrincipal = null;

        if (request.categoriaPrincipalId() != null) {
            categoriaPrincipal = categoriaRepository.findById(request.categoriaPrincipalId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Categoria principal não encontrada com id: " + request.categoriaPrincipalId()
                    ));
        }

        if (categoriaPrincipal != null && categoriaPrincipal.getId().equals(id)) {
            throw new RegraDeNegocioException("Uma categoria não pode ser categoria principal dela mesma");
        }

        validarHierarquia(id, categoriaPrincipal);
        validarDuplicidade(request.nome(), request.categoriaPrincipalId(), id);

        categoria.setNome(request.nome().trim());
        categoria.setCategoriaPrincipal(categoriaPrincipal);

        categoria = categoriaRepository.save(categoria);

        return categoriaMapper.paraResponse(categoria);
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = buscarCategoria(id);

        if (categoriaRepository.existsByCategoriaPrincipalId(id)) {
            throw new RegraDeNegocioException("Não é permitido excluir categoria que possui subcategorias");
        }

        categoriaRepository.delete(categoria);
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com id: " + id));
    }

    private void validarDuplicidade(String nome, Long categoriaPrincipalId, Long idAtual) {
        String nomeTratado = nome.trim();
        boolean existe = false;

        if (categoriaPrincipalId == null) {
            if (idAtual == null) {
                existe = categoriaRepository.existsByNomeIgnoreCaseAndCategoriaPrincipalIsNull(nomeTratado);
            } else {
                existe = categoriaRepository.existsByNomeIgnoreCaseAndCategoriaPrincipalIsNullAndIdNot(nomeTratado, idAtual);
            }
        } else {
            if (idAtual == null) {
                existe = categoriaRepository.existsByNomeIgnoreCaseAndCategoriaPrincipalId(nomeTratado, categoriaPrincipalId);
            } else {
                existe = categoriaRepository.existsByNomeIgnoreCaseAndCategoriaPrincipalIdAndIdNot(nomeTratado, categoriaPrincipalId, idAtual);
            }
        }

        if (existe) {
            throw new DuplicidadeException("Já existe uma categoria com esse nome neste nível");
        }
    }

    private void validarHierarquia(Long idCategoria, Categoria categoriaPrincipal) {
        Categoria atual = categoriaPrincipal;

        while (atual != null) {
            if (atual.getId().equals(idCategoria)) {
                throw new RegraDeNegocioException("Não é permitido criar ciclo na hierarquia de categorias");
            }

            atual = atual.getCategoriaPrincipal();
        }
    }
}