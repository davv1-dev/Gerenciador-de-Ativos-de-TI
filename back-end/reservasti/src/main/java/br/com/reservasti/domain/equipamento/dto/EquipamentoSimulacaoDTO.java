package br.com.reservasti.domain.equipamento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EquipamentoSimulacaoDTO(@NotNull Long categoriaId,
                                      @NotNull @Min(1) Integer quantidadeNecessaria) {
}
