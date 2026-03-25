package jp_2026_Manoel_Souza.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CriarProdutoRequest(

        @NotBlank(message = "O nome do produto é obrigatório")
        @Size(max = 150, message = "O nome do produto deve ter no máximo 150 caracteres")
        String nome,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String descricao,

        @NotNull(message = "O preço do produto é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço do produto deve ser maior que zero")
        BigDecimal preco,

        @Size(max = 100, message = "O código de barras deve ter no máximo 100 caracteres")
        String codigoBarras,

        @NotNull(message = "A categoria do produto é obrigatória")
        Long categoriaId
) {
}