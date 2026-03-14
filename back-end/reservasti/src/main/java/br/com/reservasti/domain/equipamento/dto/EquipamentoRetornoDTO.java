package br.com.reservasti.domain.equipamento.dto;

import br.com.reservasti.domain.equipamento.Equipamento;

import java.time.LocalDate;

public record EquipamentoRetornoDTO(
        Long id,
        String nome,
        String marca,
        String numeroPatrimonio,
        String status,
        String nomeCategoria,
        LocalDate dataFimGarantia
) {

    public EquipamentoRetornoDTO(Equipamento equipamento) {
        this(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getMarca(),
                equipamento.getNumeroPatrimonio(),
                equipamento.getStatus().name(),
                equipamento.getCategoria().getNome(),
                equipamento.getDataFimGarantia()
        );
    }
}
