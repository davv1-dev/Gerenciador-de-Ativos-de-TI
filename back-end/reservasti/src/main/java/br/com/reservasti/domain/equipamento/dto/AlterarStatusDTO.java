package br.com.reservasti.domain.equipamento.dto;

import br.com.reservasti.domain.equipamento.StatusEquipamento;

public record AlterarStatusDTO(StatusEquipamento statusEquipamento,
                               Integer quantidade) {}
