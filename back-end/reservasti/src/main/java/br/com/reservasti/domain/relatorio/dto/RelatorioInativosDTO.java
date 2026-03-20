package br.com.reservasti.domain.relatorio.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RelatorioInativosDTO(String patrimonio,
                                   String nomeEquipamento,
                                   String departamento,
                                   LocalDateTime dataUltimaMovimentacao) {
}
