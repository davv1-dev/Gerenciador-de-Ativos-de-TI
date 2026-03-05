package br.com.reservasti.domain.categoria.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(
        @NotBlank(message = "O nome da categoria é obrigatório")
        String nome,
        String descricao
) {}
