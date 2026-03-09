package br.com.reservasti.domain.relatorio.dto;

public record RelatorioAtrasoDTO(String nomeFuncionario,
                                 String departamento,
                                 String patrimonioEquipamento,
                                 long diasAtraso) {
}
