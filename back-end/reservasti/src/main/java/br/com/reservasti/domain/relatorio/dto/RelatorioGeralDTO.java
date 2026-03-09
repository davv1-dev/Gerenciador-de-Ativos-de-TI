package br.com.reservasti.domain.relatorio.dto;

public record RelatorioGeralDTO(Long totalEquipamentosCadastrados,
                                Long totalDisponiveisNoEstoque,
                                Long totalEmUsoOuReservados,
                                Long totalEmManutencao) {
}
