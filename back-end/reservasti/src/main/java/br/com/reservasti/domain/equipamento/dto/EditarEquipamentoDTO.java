package br.com.reservasti.domain.equipamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EditarEquipamentoDTO(

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "A marca é obrigatória")
        String marca,

        @NotBlank(message = "O modelo é obrigatório")
        String modelo,

        @NotNull(message = "O ID da categoria é obrigatório")
        Long categoriaId
) {
}