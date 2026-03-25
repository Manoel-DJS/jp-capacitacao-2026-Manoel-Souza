package jp_2026_Manoel_Souza.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarCategoriaRequest(

        @NotBlank(message = "O nome da categoria é obrigatório")
        @Size(max = 150, message = "O nome da categoria deve ter no máximo 150 caracteres")
        String nome,

        Long categoriaPrincipalId
) {
}