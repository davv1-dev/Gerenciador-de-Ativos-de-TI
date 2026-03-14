package br.com.reservasti.domain.equipamento.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SimulacaoEquipamentosDTO(@NotNull List<EquipamentoSimulacaoDTO> itens) {
}
