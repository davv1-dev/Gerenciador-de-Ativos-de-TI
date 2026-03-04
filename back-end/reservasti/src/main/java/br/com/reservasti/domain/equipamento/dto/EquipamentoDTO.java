package br.com.reservasti.domain.equipamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipamentoDTO(

        @NotBlank(message = "O nome do equipamento é obrigatório")
        String nome,

        @NotBlank(message = "A marca é obrigatória")
        String marca,

        @NotBlank(message = "O modelo é obrigatório")
        String modelo,

        @NotBlank(message = "O número de patrimônio (etiqueta) é obrigatório")
        String numeroPatrimonio,

        @NotNull(message = "A categoria é obrigatória")
        Long categoriaId
) {
}
