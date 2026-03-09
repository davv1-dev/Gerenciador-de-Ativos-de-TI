package br.com.reservasti.domain.relatorio.dto;

import java.time.LocalDate;

public record RelatorioInativosDTO(String patrimonio,
                                   String nomeEquipamento,
                                   String departamento,
                                   LocalDate dataUltimaMovimentacao) {
}
