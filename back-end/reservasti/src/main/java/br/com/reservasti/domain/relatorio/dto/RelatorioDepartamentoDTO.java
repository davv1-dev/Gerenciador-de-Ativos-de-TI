package br.com.reservasti.domain.relatorio.dto;

public record RelatorioDepartamentoDTO(
        String nomeDepartamento,
        Long totalEquipamentos,
        Long emUso,
        Long disponiveis,
        Long emManutencao

) {
}
