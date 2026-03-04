package br.com.reservasti.domain.equipamento.dto;

import br.com.reservasti.domain.equipamento.Equipamento;

public record EquipamentoReturnDTO(
        Long id,
        String nome,
        String marca,
        String numeroPatrimonio,
        String status,
        String nomeCategoria
) {

    public EquipamentoReturnDTO(Equipamento equipamento) {
        this(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getMarca(),
                equipamento.getNumeroPatrimonio(),
                equipamento.getStatus().name(),
                equipamento.getCategoria().getNome()
        );
    }
}
