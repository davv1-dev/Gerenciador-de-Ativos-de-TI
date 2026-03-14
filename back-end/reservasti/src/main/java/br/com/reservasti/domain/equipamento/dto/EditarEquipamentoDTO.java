package br.com.reservasti.domain.equipamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EditarEquipamentoDTO(

        String nome,

        String marca,

        String modelo,

        Long categoriaId
) {
}