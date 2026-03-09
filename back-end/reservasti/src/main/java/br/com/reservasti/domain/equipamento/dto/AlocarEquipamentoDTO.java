package br.com.reservasti.domain.equipamento.dto;

import jakarta.validation.constraints.NotNull;

public record AlocarEquipamentoDTO(@NotNull(message = "O id do departamento é necessario.")Long idDepartamento,@NotNull Long idEquipamento) {

}
