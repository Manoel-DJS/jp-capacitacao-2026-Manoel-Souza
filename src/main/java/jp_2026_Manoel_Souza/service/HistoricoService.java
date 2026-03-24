package jp_2026_Manoel_Souza.service;


import jp_2026_Manoel_Souza.repository.HistoricoPrecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoPrecoRepository historicoPrecoRepository;

    /**
     *
     * @param produtoId
     * @return


    public HistoricoProdutoDTO getHistoricoByProdutoId(Long produtoId) {
    Set<HistoricoPreco> historicoPrecos = historicoPrecoRepository.findByProdutosId(produtoId)
    .stream().flatMap(/*desenvolveria o mapeamento*/
    //)
            //.toList();
    /**
     * Existe várias forma de se mappear, map, flatmap, for, stream, mapperstruct, projection
     */
         // return new HistoricoProdutoDTO(
           // historicoPrecos.getId(),
         //       historicoPrecos.getProdutos().getNome(),
       //         historicoPrecos.getPrecoAntigo(),
     //                   historicoPrecos.getPrecoNovo(),
   //                     historicoPrecos.getDataAlteracao()
 //                       );

}

