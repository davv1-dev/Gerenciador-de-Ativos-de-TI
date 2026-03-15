package br.com.reservasti.domain.equipamento.dto;

import br.com.reservasti.domain.equipamento.Equipamento;

import java.time.LocalDate;

public record EquipamentoRetornoDTO(
        Long id,
        String nome,
        String marca,
        String modelo,
        String numeroPatrimonio,
        Integer quantidade,
        String status,
        Long categoriaid,
        LocalDate dataFimGarantia
) {

    public EquipamentoRetornoDTO(Equipamento equipamento) {
        this(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getMarca(),
                equipamento.getModelo(),
                equipamento.getNumeroPatrimonio(),
                equipamento.getQuantidade(),
                equipamento.getStatus().name(),
                equipamento.getCategoria().getId(),
                equipamento.getDataFimGarantia()
        );
    }
}
