package br.com.reservasti.domain.departamento.dto;


import jakarta.validation.constraints.NotBlank;

public record DepartamentoDTO(

        @NotBlank(message = "O nome do departamento é obrigatório")
        String nome,

        @NotBlank(message = "O centro de custo é obrigatório")
        String centroDeCusto
) {
}
